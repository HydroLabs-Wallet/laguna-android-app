package io.novafoundation.nova.feature_account_impl.presentation.password_confirm

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface PasswordConfirmView : MvpView {
    fun initView()
    fun enableButton(isEnabled: Boolean)
    fun showError()
}
