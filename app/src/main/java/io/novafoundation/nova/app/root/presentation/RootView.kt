package io.novafoundation.nova.app.root.presentation

import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface RootView: BaseView {
    fun showMessage(text: String)
}
