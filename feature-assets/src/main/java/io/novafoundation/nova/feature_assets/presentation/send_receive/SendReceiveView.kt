package io.novafoundation.nova.feature_assets.presentation.send_receive

import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SendReceiveView: BaseView {
    fun setSendEnabled(enabled: Boolean)
}
