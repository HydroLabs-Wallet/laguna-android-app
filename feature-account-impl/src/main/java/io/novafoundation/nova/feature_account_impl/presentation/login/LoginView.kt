package io.novafoundation.nova.feature_account_impl.presentation.login

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface LoginView : MvpView {
    fun showImportSnack()
    fun showSupportSnack()
    fun enableButton(enable: Boolean)
    fun showPasswordError()

}
