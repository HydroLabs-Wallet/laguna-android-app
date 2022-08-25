package io.novafoundation.nova.feature_account_impl.presentation.login

import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface LoginView : BaseView {
    fun showImportSnack()
    fun showSupportSnack()
    fun enableButton(enable: Boolean)
}
