package io.novafoundation.nova.feature_assets.presentation.asset_receive_chooser

import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AssetReceiveChooserView : MvpView {
    fun submitList(data: List<AssetModel>)
}
