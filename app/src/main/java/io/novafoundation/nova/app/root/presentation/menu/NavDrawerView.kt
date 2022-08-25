package io.novafoundation.nova.app.root.presentation.menu

import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface NavDrawerView : BaseView {

    fun setName(data: String)
    fun setBalance(data: String)
    fun setContactNumbers(data: String)
    fun setShowValues(data: Boolean)
}
