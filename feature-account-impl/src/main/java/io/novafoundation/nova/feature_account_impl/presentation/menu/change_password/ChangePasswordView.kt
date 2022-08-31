package io.novafoundation.nova.feature_account_impl.presentation.menu.change_password

import io.novafoundation.nova.common.base.BaseView
import io.novafoundation.nova.common.view.InputFieldView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ChangePasswordView : BaseView {
    fun updateCurrentPasswordState(data: InputFieldView.InputFieldData)
    fun updateConfirmPasswordState(data: InputFieldView.InputFieldData)
    fun updateNewPasswordState(data: InputFieldView.InputFieldData)

    fun enableButton(enable: Boolean)
}
