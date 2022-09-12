package io.novafoundation.nova.feature_assets.presentation.asset_receive_chooser

import android.util.Log
import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.common.utils.inBackground
import io.novafoundation.nova.feature_assets.data.mappers.mappers.mapAssetToAssetModel
import io.novafoundation.nova.feature_assets.domain.WalletInteractor
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import kotlinx.coroutines.flow.*
import moxy.InjectViewState
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class AssetReceiveChooserPresenter @Inject constructor(
    private val router: WalletRouter,
    private val interactor: WalletInteractor,
    private val payload: AssetReceivePayload
) : BasePresenter<AssetReceiveChooserView>() {

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
            val filteredAssets = assets.filter {

                val nameSame = it.token.configuration.name.lowercase() == payload.priceId
                val priceIdSame = payload.priceId != null && it.token.configuration.priceId == payload.priceId
                val filtered = nameSame || priceIdSame
                Log.e("mcheck", "name $filtered")
                filtered
            }
            if (query.isEmpty()) {
                viewState.submitList(filteredAssets)
            } else {
                val filtered = filteredAssets.filter {
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
        router.back()
        val payload = AssetPayload(
            chainId = asset.token.configuration.chainId,
            chainAssetId = asset.token.configuration.id
        )
        router.setResult(AssetReceiveChooserFragment.RESULT, payload)
    }

    fun onBackCommandClick() {
        router.back()
    }
}
