package io.novafoundation.nova.feature_assets.presentation.send.asset_choose

import io.novafoundation.nova.common.base.BasePresenter
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
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class SendAssetChoosePresenter @Inject constructor(
    private val router: WalletRouter,
    private val interactor: WalletInteractor,
) : BasePresenter<SendAssetChooseView>(), WithCoroutineScopeExtensions {

    override val coroutineScope: CoroutineScope = presenterScope

    private val balancesFlow = interactor.balancesFlow()
        .inBackground()
        .share()

    private val showValuesFlow = interactor.assetValueVisibleFlow()
        .inBackground()
        .share()

    val assetsFlow: Flow<List<AssetModel>> = combine(balancesFlow, showValuesFlow) { balances, showValues ->
        balances.assets
            .map { entry ->
                entry.value
                    .map { mapAssetToAssetModel(entry.key.chain, it, showValues) }
            }
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
                val queryLower = query.lowercase()
                val filtered = assets.filter {
                    it.token.configuration.symbol.lowercase().contains(queryLower) ||
                        it.token.configuration.chainId.lowercase().contains(queryLower) ||
                        it.token.configuration.name.lowercase().contains(queryLower) ||
                        it.token.configuration.priceId?.lowercase()?.contains(queryLower) == true
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
        router.toSendAddressChooser(payload)
    }

    fun onBackCommandClick() {
        router.back()
    }
}
