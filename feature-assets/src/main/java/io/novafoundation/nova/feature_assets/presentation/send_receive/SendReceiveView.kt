package io.novafoundation.nova.feature_assets.presentation.send_receive

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SendReceiveView : MvpView {
    fun setSendEnabled(enabled: Boolean)
}
