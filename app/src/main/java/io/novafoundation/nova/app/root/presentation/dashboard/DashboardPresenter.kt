package io.novafoundation.nova.app.root.presentation.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.terrakok.cicerone.ResultListener
import io.novafoundation.nova.app.R
import io.novafoundation.nova.app.root.presentation.RootRouter
import io.novafoundation.nova.common.address.AddressIconGenerator
import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.common.data.model.SelectAccountPayload
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.common.utils.*
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountRepository
import io.novafoundation.nova.feature_account_api.domain.interfaces.SelectedAccountUseCase
import io.novafoundation.nova.feature_account_api.domain.model.MetaAccount
import io.novafoundation.nova.feature_account_api.domain.model.defaultSubstrateAddress
import io.novafoundation.nova.feature_assets.domain.WalletInteractor
import io.novafoundation.nova.feature_assets.domain.assets.list.AssetsListInteractor
import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import io.novafoundation.nova.feature_assets.presentation.balance.list.model.TotalBalanceModel
import io.novafoundation.nova.feature_assets.presentation.send_receive.SendReceivePayload
import io.novafoundation.nova.runtime.multiNetwork.connection.ChainConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import moxy.InjectViewState
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
    private val accountRepository: AccountRepository,
    private val resourceManager: ResourceManager


) :
    BasePresenter<DashboardView>(), WithCoroutineScopeExtensions {
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

    private val valueVisibleFlow = interactor.assetValueVisibleFlow()
        .inBackground()
        .share()

    private val totalBalanceFlow = combine(balancesFlow, valueVisibleFlow) { balances, visible ->
        with(balances) {
            val hasHistoryRecord = assets.filterValues { value -> value.filter { it.hasHistoryRecord }.isNotEmpty() }.isNotEmpty()
            TotalBalanceModel(
                shouldShowPlaceholder = !hasHistoryRecord,
                totalBalanceFiat = if (visible) totalBalanceFiat.formatAsCurrency() else {
                    resourceManager.getString(R.string.value_hidden)
                },
                lockedBalanceFiat = if (visible) lockedBalanceFiat.formatAsCurrency() else {
                    resourceManager.getString(R.string.value_hidden)
                },
                balances = this
            )
        }
    }.inBackground()
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
        }.launchIn(presenterScope)
        totalBalanceFlow.onEach {
            balanceModel = it
            viewState.submitBalanceModel(it)
        }.launchIn(presenterScope)
    }


    fun onBackCommandClick() {
        router.back()
    }

    fun onChainSettingsCLick() {
        rootRouter.toChainsSettings()
    }

    fun onMenuClick() {
        rootRouter.toMenu()
    }

    fun onAvatarClicked() {
        val payload = SelectAccountPayload("Dashboard.selectAccount", true)
        val resultListener = ResultListener {
            presenterScope.launch {
                accountRepository.selectMetaAccount(it as Long)
            }
        }
        router.setResultListener(payload.tag, resultListener)
        rootRouter.toSelectAccount(payload)
    }

    fun onReceiveClicked() {
        router.toAssetSelectionToReceive()
    }

    fun onValueVisibilityToggle() {
        Log.e("mcheck","onValueVisibilityToggle")
        presenterScope.launch { interactor.toggleValueVisible() }
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
