package io.novafoundation.nova.app.root.presentation

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface RootView : MvpView {
    fun showMessage(text: String)
}
