package io.novafoundation.nova.feature_assets.presentation.send.asset_choose

import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SendAssetChooseView : MvpView {
    fun submitList(data: List<AssetModel>)
}
