package io.novafoundation.nova.feature_assets.presentation.asset_receive_chooser

import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AssetReceiveChooserView: BaseView {
    fun submitList(data: List<AssetModel>)
}
