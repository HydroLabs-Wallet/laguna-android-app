package io.novafoundation.nova.app.root.presentation.dashboard.сhain_setting

import io.novafoundation.nova.common.base.BaseView
import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ChainSettingsView : BaseView {
    fun submitList(data: List<AssetModel>)
}
