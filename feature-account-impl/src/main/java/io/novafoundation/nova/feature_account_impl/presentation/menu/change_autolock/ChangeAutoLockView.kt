package io.novafoundation.nova.feature_account_impl.presentation.menu.change_autolock

import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ChangeAutoLockView : BaseView {
    fun setCurrentTime(data:String)
    fun enableButton(enable: Boolean)
}
