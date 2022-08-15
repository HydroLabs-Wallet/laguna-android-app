package io.novafoundation.nova.app.root.presentation.dashboard.page_networks

import io.novafoundation.nova.common.utils.WithCoroutineScopeExtensions
import io.novafoundation.nova.common.utils.inBackground
import io.novafoundation.nova.feature_account_api.domain.interfaces.SelectedAccountUseCase
import io.novafoundation.nova.feature_account_api.domain.model.addressIn
import io.novafoundation.nova.feature_assets.domain.WalletInteractor
import io.novafoundation.nova.feature_wallet_api.domain.model.AssetGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import moxy.InjectViewState
import io.novafoundation.nova.common.base.BasePresenter
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class PageNetworksPresenter @Inject constructor(
    private val interactor: WalletInteractor,
    private val selectedAccountUseCase: SelectedAccountUseCase,

    ) :
    BasePresenter<PageNetworksView>(), WithCoroutineScopeExtensions {

    override val coroutineScope: CoroutineScope = presenterScope

    private val balancesFlow = interactor.balancesFlow()
        .inBackground()
        .share()
    val assetsFlow: Flow<List<AssetGroup>> = balancesFlow.map { balances ->
        val metaAccount = selectedAccountUseCase.selectedMetaAccountFlow().first()


        balances.assets.map {
            val group = it.key
            val address = metaAccount.addressIn(group.chain)!!
            group.address = address
            group
        }
    }
        .distinctUntilChanged()
        .inBackground()
        .share()


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        assetsFlow.onEach { viewState.submitList(it) }.launchIn(presenterScope)
    }
}
