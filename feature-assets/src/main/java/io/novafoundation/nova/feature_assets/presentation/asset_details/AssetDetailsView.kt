package io.novafoundation.nova.feature_assets.presentation.asset_details

import io.novafoundation.nova.feature_assets.presentation.balance.detail.AssetDetailsModel
import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AssetDetailsView: BaseView {
    fun setAssetData(data: AssetDetailsModel)
}
