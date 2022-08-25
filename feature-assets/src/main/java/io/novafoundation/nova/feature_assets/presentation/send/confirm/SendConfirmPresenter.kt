package io.novafoundation.nova.feature_assets.presentation.send.confirm

import androidx.lifecycle.asFlow
import io.novafoundation.nova.common.data.model.ConfirmPayload
import io.novafoundation.nova.common.mixin.api.Validatable
import io.novafoundation.nova.common.mixin.api.ValidationFailureUi
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.common.utils.WithCoroutineScopeExtensions
import io.novafoundation.nova.common.validation.ProgressConsumer
import io.novafoundation.nova.common.validation.ValidationExecutor
import io.novafoundation.nova.common.validation.ValidationSystem
import io.novafoundation.nova.common.validation.progressConsumer
import io.novafoundation.nova.feature_account_api.domain.interfaces.SelectedAccountUseCase
import io.novafoundation.nova.feature_account_api.domain.model.defaultSubstrateAddress
import io.novafoundation.nova.feature_assets.R
import io.novafoundation.nova.feature_assets.data.mappers.mappers.mapAssetToAssetModel
import io.novafoundation.nova.feature_assets.domain.WalletInteractor
import io.novafoundation.nova.feature_assets.domain.send.SendInteractor
import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import io.novafoundation.nova.feature_assets.presentation.send.TransferDraft
import io.novafoundation.nova.feature_assets.presentation.send.mapAssetTransferValidationFailureToUI
import io.novafoundation.nova.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransfer
import io.novafoundation.nova.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransferPayload
import io.novafoundation.nova.runtime.multiNetwork.ChainRegistry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import io.novafoundation.nova.common.base.BasePresenter
import moxy.presenterScope
import java.math.BigDecimal
import javax.inject.Inject

typealias TitleAndMessage = Pair<String, String>

@InjectViewState
class SendConfirmPresenter @Inject constructor(
    private val selectedAccountUseCase: SelectedAccountUseCase,
    private val walletInteractor: WalletInteractor,
    private val chainRegistry: ChainRegistry,
    private val resourceManager: ResourceManager,
    private val sendInteractor: SendInteractor,
    private val router: WalletRouter,
    private val payload: TransferDraft,
    private val validationExecutor: ValidationExecutor,

    ) : BasePresenter<SendConfirmView>(), WithCoroutineScopeExtensions, Validatable by validationExecutor {
    override val coroutineScope = presenterScope

    var isEditMode = false
    var note = ""
    fun onBackCommandClick() {
        router.back()
    }

    var sendPayLoad: AssetTransferPayload? = null
    private val _transferSubmittingLiveData = MutableStateFlow(false)

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.startEditMode(isEditMode)
        validationFailureEvent.asFlow()
            .onEach {
                val failure = it.peekContent()
                when (failure) {
                    is ValidationFailureUi.Custom -> viewState.showError(failure.payload.message)
                    is ValidationFailureUi.Default -> viewState.showError(failure.message)
                }

            }
            .launchIn(presenterScope)
        _transferSubmittingLiveData
            .onEach { viewState.showLoader(it) }
            .launchIn(
                presenterScope
            )
        presenterScope.launch {
            val metaAccount = selectedAccountUseCase.getSelectedMetaAccount()
            val name = metaAccount.name.ifEmpty { metaAccount.defaultSubstrateAddress }
            val asset = walletInteractor.getCurrentAsset(payload.assetPayload.chainId, payload.assetPayload.chainAssetId)
            val chain = chainRegistry.getChain(payload.assetPayload.chainId)
            val assetModel = mapAssetToAssetModel(chain, asset,true)
            viewState.setReceiver(payload, assetModel, name)
            val commissionAssetFlow = walletInteractor.commissionAssetFlow(payload.assetPayload.chainId)
            sendPayLoad = AssetTransferPayload(
                transfer = AssetTransfer(
                    sender = metaAccount,
                    recipient = payload.contact.address,
                    chain = chain,
                    chainAsset = assetModel.token.configuration,
                    amount = payload.amount
                ),
                fee = payload.fee,
                commissionAsset = commissionAssetFlow.first(),
                usedAsset = asset
            )
        }

    }


    fun onTextChanged(data: String) {
        note = data
        viewState.setNote(note)
    }

    fun onAddNoteClick() {
        isEditMode = !isEditMode
        viewState.startEditMode(isEditMode)
    }

    fun onNextClicked() {
        if (isEditMode) {
            isEditMode = !isEditMode
            viewState.startEditMode(isEditMode)
        } else {
            presenterScope.launch {
                sendPayLoad?.let { confirmPayload ->
                    validationExecutor.requireValid(
                        validationSystem = sendInteractor.validationSystemFor(confirmPayload.transfer.chainAsset),
                        payload = confirmPayload,
                        progressConsumer = _transferSubmittingLiveData.progressConsumer(),
                        validationFailureTransformer = { mapAssetTransferValidationFailureToUI(resourceManager, it) }
                    ) { validPayload ->
                        val confirmPayload = ConfirmPayload("SendConfirmPresenter")
                        router.setResultListener(confirmPayload.tag) {
                            performTransfer(validPayload.transfer, validPayload.fee)
                        }
                        router.toPasswordConfirm(confirmPayload)
                    }
                }
            }
        }
    }

    private fun performTransfer(transfer: AssetTransfer, fee: BigDecimal) = presenterScope.launch {
        sendInteractor.performTransfer(transfer, fee)
            .onSuccess {
                viewState.showSuccess(resourceManager.getString(R.string.transaction_sent))
                router.backToDashBoard()
            }.onFailure(::showError)

    }
}
