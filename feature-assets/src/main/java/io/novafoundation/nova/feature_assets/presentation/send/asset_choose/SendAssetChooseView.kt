package io.novafoundation.nova.feature_assets.presentation.send.asset_choose

import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SendAssetChooseView: BaseView {
    fun submitList(data: List<AssetModel>)
}
