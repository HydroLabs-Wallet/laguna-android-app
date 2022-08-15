package io.novafoundation.nova.feature_account_impl.presentation.password

import io.novafoundation.nova.common.view.InputFieldView
import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface CreatePasswordView: BaseView {
    fun initView()
    fun updateConfirmPasswordState(data: InputFieldView.InputFieldData)
    fun updatePasswordState(data: InputFieldView.InputFieldData)
    fun enableButton(isEnabled: Boolean)
}
