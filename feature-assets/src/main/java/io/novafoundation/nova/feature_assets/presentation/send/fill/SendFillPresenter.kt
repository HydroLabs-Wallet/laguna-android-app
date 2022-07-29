package io.novafoundation.nova.feature_assets.presentation.send.fill

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.asFlow
import io.novafoundation.nova.common.base.TitleAndMessage
import io.novafoundation.nova.common.mixin.api.Validatable
import io.novafoundation.nova.common.mixin.api.ValidationFailureUi
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.common.utils.*
import io.novafoundation.nova.common.validation.ProgressConsumer
import io.novafoundation.nova.common.validation.ValidationExecutor
import io.novafoundation.nova.common.validation.ValidationSystem
import io.novafoundation.nova.common.validation.progressConsumer
import io.novafoundation.nova.feature_account_api.domain.interfaces.SelectedAccountUseCase
import io.novafoundation.nova.feature_account_api.domain.model.MetaAccount
import io.novafoundation.nova.feature_assets.R
import io.novafoundation.nova.feature_assets.data.mappers.mappers.mapAssetToAssetModel
import io.novafoundation.nova.feature_assets.domain.WalletInteractor
import io.novafoundation.nova.feature_assets.domain.send.SendInteractor
import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import io.novafoundation.nova.feature_assets.presentation.send.TransferDraft
import io.novafoundation.nova.feature_assets.presentation.send.mapAssetTransferValidationFailureToUI
import io.novafoundation.nova.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransfer
import io.novafoundation.nova.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransferPayload
import io.novafoundation.nova.feature_wallet_api.domain.model.Asset
import io.novafoundation.nova.feature_wallet_api.domain.model.amountFromPlanks
import io.novafoundation.nova.feature_wallet_api.presentation.formatters.formatTokenAmount
import io.novafoundation.nova.runtime.multiNetwork.ChainRegistry
import io.novafoundation.nova.runtime.multiNetwork.chain.model.Chain
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import javax.inject.Inject

@InjectViewState
class SendFillPresenter @Inject constructor(
    private val selectedAccountUseCase: SelectedAccountUseCase,
    private val walletInteractor: WalletInteractor,
    private val chainRegistry: ChainRegistry,
    private val resourceManager: ResourceManager,
    private val sendInteractor: SendInteractor,
    private val router: WalletRouter,
    private val payload: TransferDraft,
    private val validationExecutor: ValidationExecutor,

    ) : MvpPresenter<SendFillView>(), WithCoroutineScopeExtensions, Validatable by validationExecutor {
    override val coroutineScope = presenterScope

    var isUsdFlow = MutableStateFlow<Boolean>(true)
    var amountTopFlow = MutableStateFlow<BigDecimal?>(null)
    var amountBottomFlow = flowOf<BigDecimal?> { null }
    var isMaxFlow = MutableStateFlow<Boolean>(false)
    private val sendInProgressFlow = MutableStateFlow(false)

    private lateinit var selectedAccount: MetaAccount
    private lateinit var chain: Chain
    private lateinit var assetModel: AssetModel
    private lateinit var chainAsset: Chain.Asset
    private lateinit var newPayload: TransferDraft
    fun onBackCommandClick() {
        router.back()
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        if (!::newPayload.isInitialized) {
            newPayload = payload
        }


        viewState.setValueTopTransfer(if (newPayload.amount.stripTrailingZeros() == BigDecimal.ZERO) null else newPayload.amount.format())
        viewState.setReceiver(newPayload.contact)
        val assetFlow = walletInteractor.assetFlow(chainId = newPayload.assetPayload.chainId, chainAssetId = newPayload.assetPayload.chainAssetId)
            .map {
                val chain = chainRegistry.getChain(newPayload.assetPayload.chainId)
                mapAssetToAssetModel(chain, it)
            }
        validationFailureEvent.asFlow()
            .onEach {
                val failure = it.peekContent()
                when (failure) {
                    is ValidationFailureUi.Custom -> viewState.showError(failure.payload.message)
                    is ValidationFailureUi.Default -> viewState.showError(failure.message)
                }

            }.launchIn(presenterScope)
        sendInProgressFlow
            .onEach { viewState.showLoader(it) }
            .launchIn(presenterScope)

        selectedAccountUseCase.selectedMetaAccountFlow()
            .flatMapLatest { metaAccount ->
                combine(assetFlow, isUsdFlow, isMaxFlow, amountTopFlow, amountBottomFlow) { asset, isUsd, isMax, amountTop, amountBottom ->
                    viewState.setSource(asset)
                    val exchangeRate = asset.token.dollarRate
                    if (isUsd) {
                        viewState.setAmountName(resourceManager.getString(R.string.usd))
                        val precision = asset.token.configuration.precision
                        val amount = amountTop.orZero().divide(exchangeRate, precision, RoundingMode.HALF_UP)
                        calculateFee(
                            AssetTransfer(
                                sender = metaAccount,
                                recipient = newPayload.contact.address,
                                asset.chain,
                                asset.token.configuration,
                                amount
                            ), asset
                        )
                        newPayload.amount = amount
                        viewState.setValueBottomTransfer(amount.formatTokenAmount(asset.token.configuration))
                    } else {
                        calculateFee(
                            AssetTransfer(
                                sender = metaAccount,
                                recipient = newPayload.contact.address,
                                asset.chain,
                                asset.token.configuration,
                                amountTop.orZero()
                            ), asset
                        )
                        newPayload.amount = amountTop.orZero()
                        viewState.setAmountName(asset.token.configuration.symbol)
                        val amount = amountTop.orZero().multiply(exchangeRate)
                        viewState.setValueBottomTransfer(amount.formatAsCurrency())
                    }
                    selectedAccount = metaAccount
                    this.assetModel=asset
                    this.chain = asset.chain
                    this.chainAsset = asset.token.configuration

                    viewState.enableButton(amountTop.orZero().stripTrailingZeros() > BigDecimal.ZERO)
                }
            }.launchIn(presenterScope)
    }

    private suspend fun calculateFee(data: AssetTransfer, assetModel: AssetModel) {

        val feePlanks =
            if (data.amount.stripTrailingZeros() == BigDecimal.ZERO) {
                BigInteger.ZERO
            } else {
                sendInteractor.getTransferFee(data)
            }
        val fee = data.chainAsset.amountFromPlanks(feePlanks)
        val exchangeRate = assetModel.token.dollarRate
        newPayload.fee = fee
        val tokenFee = fee.formatTokenAmount(data.chainAsset)
        val currencyFee = fee.multiply(exchangeRate).formatAsCurrency()
        viewState.setFee(currency = currencyFee, token = tokenFee)
    }

    fun onSwapClicked() {
        isUsdFlow.value = !isUsdFlow.value
    }

    fun onMaxClicked() {

    }

    fun onTextChanged(data: String) {
        if (data.isNotBlank())
            amountTopFlow.value = BigDecimal(data)
        else amountTopFlow.value = null
    }

    fun onNextClicked() {
        presenterScope.launch {
            val commissionAssetFlow = walletInteractor.commissionAssetFlow(newPayload.assetPayload.chainId)
            val assetFlow = walletInteractor.assetFlow(newPayload.assetPayload.chainId, newPayload.assetPayload.chainAssetId)
            val payload = AssetTransferPayload(
                transfer = buildTransfer(newPayload.amount, newPayload.contact.address),
                fee = newPayload.fee,
                commissionAsset = commissionAssetFlow.first(),
                usedAsset = assetFlow.first()
            )

            validationExecutor.requireValid(
                validationSystem = sendInteractor.validationSystemFor(chainAsset),
                payload = payload,
                progressConsumer = sendInProgressFlow.progressConsumer(),
                validationFailureTransformer = { mapAssetTransferValidationFailureToUI(resourceManager, it) }
            ) {
                sendInProgressFlow.value = false

                router.toSendConfirm(newPayload)
            }
        }

    }

    suspend fun <P, S> ValidationExecutor.requireValid(
        validationSystem: ValidationSystem<P, S>,
        payload: P,
        validationFailureTransformer: (S) -> TitleAndMessage,
        progressConsumer: ProgressConsumer? = null,
        autoFixPayload: (original: P, failureStatus: S) -> P = { original, _ -> original },
        block: (P) -> Unit,
    ) = requireValid(
        validationSystem = validationSystem,
        payload = payload,
        errorDisplayer = ::showError,
        validationFailureTransformerDefault = validationFailureTransformer,
        progressConsumer = progressConsumer,
        autoFixPayload = autoFixPayload,
        block = block
    )

    private suspend fun buildTransfer(amount: BigDecimal, address: String): AssetTransfer {

        return AssetTransfer(
            sender = selectedAccount,
            recipient = address,
            chain = chain,
            chainAsset = chainAsset,
            amount = amount
        )
    }

    fun showError(throwable: Throwable) {
        throwable.message?.let { viewState.showError(it) }
    }
}
