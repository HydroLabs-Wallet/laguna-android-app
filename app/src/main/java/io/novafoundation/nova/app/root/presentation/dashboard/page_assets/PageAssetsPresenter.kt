package io.novafoundation.nova.app.root.presentation.dashboard.page_assets

import io.novafoundation.nova.common.address.AddressIconGenerator
import io.novafoundation.nova.common.utils.WithCoroutineScopeExtensions
import io.novafoundation.nova.common.utils.inBackground
import io.novafoundation.nova.feature_account_api.domain.interfaces.SelectedAccountUseCase
import io.novafoundation.nova.feature_assets.data.mappers.mappers.mapAssetToAssetModel
import io.novafoundation.nova.feature_assets.domain.WalletInteractor
import io.novafoundation.nova.feature_assets.domain.assets.list.AssetsListInteractor
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class PageAssetsPresenter @Inject constructor(
    private val interactor: WalletInteractor,
    private val assetsListInteractor: AssetsListInteractor,
    private val addressIconGenerator: AddressIconGenerator,
    private val selectedAccountUseCase: SelectedAccountUseCase,
    private val router: WalletRouter,
) :
    MvpPresenter<PageAssetsView>(), WithCoroutineScopeExtensions {

    override val coroutineScope: CoroutineScope = presenterScope

    private val balancesFlow = interactor.balancesFlow()
        .inBackground()
        .share()
    val assetsFlow: Flow<List<AssetModel>> = balancesFlow.map { balances ->
        balances.assets
            .map { entry-> entry.value.map{mapAssetToAssetModel(entry.key.chain,it)} }
            .flatten()
            .sortedByDescending { it.dollarAmount }

    }
        .distinctUntilChanged()
        .inBackground()
        .share()


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        assetsFlow.onEach { viewState.submitList(it) }.launchIn(presenterScope)
    }

    fun onAssetClick(asset: AssetModel) {
        val payload = AssetPayload(
            chainId = asset.token.configuration.chainId,
            chainAssetId = asset.token.configuration.id
        )
        router.toAssetDetails(payload)
    }
}
