package io.novafoundation.nova.feature_assets.presentation.asset_details

import io.novafoundation.nova.feature_assets.presentation.balance.detail.AssetDetailsModel
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AssetDetailsView : MvpView {
    fun setAssetData(data: AssetDetailsModel)
}
