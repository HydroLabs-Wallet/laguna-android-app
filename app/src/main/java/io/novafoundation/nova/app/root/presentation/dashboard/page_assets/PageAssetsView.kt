package io.novafoundation.nova.app.root.presentation.dashboard.page_assets

import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface PageAssetsView : MvpView {
    fun submitList(data: List<AssetModel>)
}