package io.novafoundation.nova.feature_assets.presentation.send.qr

import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.common.mixin.api.Validatable
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.common.utils.WithCoroutineScopeExtensions
import io.novafoundation.nova.common.utils.flowOf
import io.novafoundation.nova.common.validation.ValidationExecutor
import io.novafoundation.nova.feature_account_api.domain.interfaces.SelectedAccountUseCase
import io.novafoundation.nova.feature_account_api.domain.model.MetaAccount
import io.novafoundation.nova.feature_assets.domain.WalletInteractor
import io.novafoundation.nova.feature_assets.domain.send.SendInteractor
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import io.novafoundation.nova.runtime.multiNetwork.ChainRegistry
import io.novafoundation.nova.runtime.multiNetwork.chain.model.Chain
import kotlinx.coroutines.flow.MutableStateFlow
import moxy.InjectViewState
import moxy.presenterScope
import java.math.BigDecimal
import javax.inject.Inject

@InjectViewState
class SendQRPresenter @Inject constructor(
    private val selectedAccountUseCase: SelectedAccountUseCase,
    private val walletInteractor: WalletInteractor,
    private val chainRegistry: ChainRegistry,
    private val resourceManager: ResourceManager,
    private val sendInteractor: SendInteractor,
    private val router: WalletRouter,
    private val payload: AssetPayload,
    private val validationExecutor: ValidationExecutor,

    ) : BasePresenter<SendQRView>(), WithCoroutineScopeExtensions, Validatable by validationExecutor {
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
    fun onBackCommandClick() {
        router.back()
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()


    }


    fun onNextClicked() {


    }

}
