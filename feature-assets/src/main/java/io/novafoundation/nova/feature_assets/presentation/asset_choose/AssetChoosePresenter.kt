package io.novafoundation.nova.feature_assets.presentation.asset_choose

import io.novafoundation.nova.common.utils.WithCoroutineScopeExtensions
import io.novafoundation.nova.common.utils.inBackground
import io.novafoundation.nova.feature_assets.data.mappers.mappers.mapAssetToAssetModel
import io.novafoundation.nova.feature_assets.domain.WalletInteractor
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import moxy.InjectViewState
import io.novafoundation.nova.common.base.BasePresenter
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class AssetChoosePresenter @Inject constructor(
    private val router: WalletRouter,
    private val interactor: WalletInteractor,
) : BasePresenter<AssetChooseView>(), WithCoroutineScopeExtensions {

    override val coroutineScope: CoroutineScope = presenterScope

    private val balancesFlow = interactor.balancesFlow()
        .inBackground()
        .share()

    private val showValuesFlow = interactor.assetValueVisibleFlow()
        .inBackground()
        .share()
    val assetsFlow: Flow<List<AssetModel>> = combine(balancesFlow, showValuesFlow) { balances, visible ->
        balances.assets
            .map { entry-> entry.value.map{mapAssetToAssetModel(entry.key.chain,it,visible)} }
            .flatten()
            .sortedByDescending { it.dollarAmount }

    }.distinctUntilChanged()
        .inBackground()
        .share()

    private val searchQueryFlow = MutableStateFlow("")


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        combine(
            assetsFlow,
            searchQueryFlow
        ) { assets, query ->
            if (query.isEmpty()) {
                viewState.submitList(assets)
            } else {
                val filtered = assets.filter {
                    it.token.configuration.symbol.lowercase().contains(query.lowercase()) ||
                        it.token.configuration.chainId.lowercase().contains(query.lowercase()) ||
                        it.token.configuration.name.lowercase().contains(query.lowercase())
                }
                viewState.submitList(filtered)
            }
        }.launchIn(presenterScope)
    }

    fun onSearchChanged(text: String) {
        searchQueryFlow.value = text.trimIndent()
    }

    fun onAssetClicked(asset: AssetModel) {
        val payload = AssetPayload(
            chainId = asset.token.configuration.chainId,
            chainAssetId = asset.token.configuration.id
        )
        router.toAssetReceive(payload)
    }

    fun onBackCommandClick() {
        router.back()
    }
}
