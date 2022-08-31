package io.novafoundation.nova.app.root.presentation.menu

import io.novafoundation.nova.app.R
import io.novafoundation.nova.app.root.presentation.RootRouter
import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.common.data.model.EditFieldPayload
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.common.utils.ellipsis
import io.novafoundation.nova.common.utils.formatAsCurrency
import io.novafoundation.nova.common.utils.inBackground
import io.novafoundation.nova.feature_account_api.domain.interfaces.SelectedAccountUseCase
import io.novafoundation.nova.feature_account_api.domain.model.MetaAccount
import io.novafoundation.nova.feature_account_api.domain.model.defaultSubstrateAddress
import io.novafoundation.nova.feature_account_impl.domain.account.details.AccountDetailsInteractor
import io.novafoundation.nova.feature_assets.domain.WalletInteractor
import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import io.novafoundation.nova.feature_assets.presentation.balance.list.model.TotalBalanceModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class NavDrawerPresenter @Inject constructor(
    private val router: WalletRouter,
    private val rootRouter: RootRouter,
    private val accountInteractor: AccountDetailsInteractor,
    private val interactor: WalletInteractor,
    private val selectedAccountUseCase: SelectedAccountUseCase,
    private val walletInteractor: WalletInteractor,
    private val resourceManager: ResourceManager
) : BasePresenter<NavDrawerView>() {

    private val selectedMetaAccount = selectedAccountUseCase.selectedMetaAccountFlow().share()
    private val valueVisibleFlow = interactor.assetValueVisibleFlow()
        .inBackground()
        .share()

    private val balancesFlow = interactor.balancesFlow()
        .inBackground()
        .share()
    private lateinit var metaAccount: MetaAccount
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

    fun toggleValueVisibility() {
        presenterScope.launch { walletInteractor.toggleValueVisible() }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        selectedMetaAccount.onEach {
            this.metaAccount = it
            val name = it.name.ifEmpty { it.defaultSubstrateAddress.ellipsis() }
            it.avatar?.let { viewState.setAvatar(it) }
            viewState.setName(name)
        }.launchIn(presenterScope)
        totalBalanceFlow.onEach {
            viewState.setBalance(it.totalBalanceFiat)
        }.launchIn(presenterScope)
        valueVisibleFlow.onEach { viewState.setShowValues(it) }
            .launchIn(presenterScope)
    }

    fun onLockClicked() {
        router.lockApp()
    }

    fun onAvatarClick() {
        rootRouter.toChangeAvatar()
    }

    fun editName() {
        val tag = "NavDraverEditName"
        router.setResultListener(tag) {
            val text = it as String
            presenterScope.launch { accountInteractor.updateName(metaAccount.id, text) }
        }
        val payload = EditFieldPayload(
            tag = tag,
            title = resourceManager.getString(R.string.enter_new_Account_name),
            text = metaAccount.name.ifEmpty { metaAccount.defaultSubstrateAddress.ellipsis() },
            hint = resourceManager.getString(R.string.your_account_name)

        )
        router.toEditField(payload)
    }

    fun toChangePassword() {
        rootRouter.toChangePassword()
    }

    fun onBackCommandClick() {
        router.back()
    }
}
