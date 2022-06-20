package io.novafoundation.nova.feature_account_impl.presentation.password

import io.novafoundation.nova.common.view.InputFieldView
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface CreatePasswordView : MvpView {
    fun initView()
    fun updateConfirmPasswordState(data: InputFieldView.InputFieldData)
    fun updatePasswordState(data: InputFieldView.InputFieldData)
    fun enableButton(isEnabled: Boolean)
}
