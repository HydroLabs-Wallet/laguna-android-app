package io.novafoundation.nova.feature_assets.presentation.asset_receive

import io.novafoundation.nova.feature_assets.presentation.receive.model.TokenReceiver
import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AssetReceiveView: BaseView {
    fun onAssetChanged(data: TokenReceiver)
}
