package io.novafoundation.nova.feature_assets.presentation.asset_choose

import io.novafoundation.nova.common.base.BaseView
import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AssetChooseView : BaseView {
    fun submitList(data: List<AssetModel>)
}
