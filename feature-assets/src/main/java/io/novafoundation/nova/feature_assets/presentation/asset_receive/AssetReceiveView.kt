package io.novafoundation.nova.feature_assets.presentation.asset_receive

import io.novafoundation.nova.feature_assets.presentation.receive.model.TokenReceiver
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AssetReceiveView : MvpView {
    fun onAssetChanged(data: TokenReceiver)
}
