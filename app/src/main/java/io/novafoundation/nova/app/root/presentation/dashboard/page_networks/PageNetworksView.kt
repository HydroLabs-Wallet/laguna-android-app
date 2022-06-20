package io.novafoundation.nova.app.root.presentation.dashboard.page_networks

import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface PageNetworksView : MvpView {
    fun submitList(data: List<AssetModel>)
}
