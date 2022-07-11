package io.novafoundation.nova.app.root.presentation.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.novafoundation.nova.app.root.presentation.RootRouter
import io.novafoundation.nova.common.address.AddressIconGenerator
import io.novafoundation.nova.common.address.createAddressModel
import io.novafoundation.nova.common.presentation.LoadingState
import io.novafoundation.nova.common.utils.*
import io.novafoundation.nova.feature_account_api.data.mappers.mapChainToUi
import io.novafoundation.nova.feature_account_api.domain.interfaces.SelectedAccountUseCase
import io.novafoundation.nova.feature_account_api.domain.model.MetaAccount
import io.novafoundation.nova.feature_account_api.domain.model.defaultSubstrateAddress
import io.novafoundation.nova.feature_assets.data.mappers.mappers.mapAssetToAssetModel
import io.novafoundation.nova.feature_assets.domain.WalletInteractor
import io.novafoundation.nova.feature_assets.domain.assets.list.AssetsListInteractor
import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import io.novafoundation.nova.feature_assets.presentation.balance.list.model.AssetGroupUi
import io.novafoundation.nova.feature_assets.presentation.balance.list.model.NftPreviewUi
import io.novafoundation.nova.feature_assets.presentation.balance.list.model.TotalBalanceModel
import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import io.novafoundation.nova.feature_assets.presentation.send_receive.SendReceivePayload
import io.novafoundation.nova.feature_nft_api.data.model.Nft
import io.novafoundation.nova.feature_wallet_api.domain.model.AssetGroup
import io.novafoundation.nova.runtime.multiNetwork.connection.ChainConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import java.math.BigDecimal
import javax.inject.Inject


private typealias SyncAction = suspend (MetaAccount) -> Unit

private const val CURRENT_ICON_SIZE = 40


@InjectViewState
class DashboardPresenter @Inject constructor(
    private val interactor: WalletInteractor,
    private val assetsListInteractor: AssetsListInteractor,
    private val addressIconGenerator: AddressIconGenerator,
    private val selectedAccountUseCase: SelectedAccountUseCase,
    private val router: WalletRouter,
    private val rootRouter: RootRouter,
    private val externalRequirements: MutableStateFlow<ChainConnection.ExternalRequirement>,

    ) :
    MvpPresenter<DashboardView>(), WithCoroutineScopeExtensions {
    private val _hideRefreshEvent = MutableLiveData<Event<Unit>>()
    val hideRefreshEvent: LiveData<Event<Unit>> = _hideRefreshEvent
    var balanceModel: TotalBalanceModel? = null

    private val fullSyncActions: List<SyncAction> = listOf(
        { interactor.syncAssetsRates() },
//        interactor::syncNfts
    )

    override val coroutineScope: CoroutineScope
        get() = presenterScope

    private val selectedMetaAccount = selectedAccountUseCase.selectedMetaAccountFlow().share()


    private val balancesFlow = interactor.balancesFlow()
        .inBackground()
        .share()


    val totalBalanceFlow = balancesFlow.map {
        TotalBalanceModel(
            shouldShowPlaceholder = it.assets.isEmpty(),
            totalBalanceFiat = it.totalBalanceFiat.formatAsCurrency(),
            lockedBalanceFiat = it.lockedBalanceFiat.formatAsCurrency(),
            balances = it
        )
    }
        .inBackground()
        .share()


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        externalRequirements.value = ChainConnection.ExternalRequirement.ALLOWED
        presenterScope.launch {
            syncWith(fullSyncActions, selectedMetaAccount.first())

            _hideRefreshEvent.value = Event(Unit)
        }
        selectedMetaAccount.onEach {
            val name = it.name.ifEmpty { it.defaultSubstrateAddress.ellipsis() }
            viewState.setAccountName(name)
        }
            .launchIn(presenterScope)
        totalBalanceFlow.onEach {
            balanceModel=it
            viewState.submitBalanceModel(it)
        }
            .launchIn(presenterScope)
    }




    fun onBackCommandClick() {
        router.back()
    }

    fun onChainSettingsCLick() {
        rootRouter.toChainsSettings()
    }

    fun onAvatarClicked() {
        rootRouter.toSelectAccount()
    }

    fun onSendReceivePopupScreen() {
        val sendEnabled = balanceModel?.totalBalance?.stripTrailingZeros()?.orZero() != BigDecimal.ZERO
        val payload = SendReceivePayload(sendEnabled)
        router.showSendReceiveDialog(payload)
    }

    private suspend fun syncWith(syncActions: List<SyncAction>, metaAccount: MetaAccount) = if (syncActions.size == 1) {
        val syncAction = syncActions.first()
        syncAction(metaAccount)
    } else {
        val syncJobs = syncActions.map { presenterScope.async { it(metaAccount) } }
        syncJobs.joinAll()
    }


}
