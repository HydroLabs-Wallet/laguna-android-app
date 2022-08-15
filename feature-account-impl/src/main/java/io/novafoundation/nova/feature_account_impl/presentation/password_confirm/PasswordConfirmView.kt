package io.novafoundation.nova.feature_account_impl.presentation.password_confirm

import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface PasswordConfirmView: BaseView {
    fun initView()
    fun enableButton(isEnabled: Boolean)
}
