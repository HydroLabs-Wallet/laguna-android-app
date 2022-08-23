package io.novafoundation.nova.feature_account_impl.presentation.edit_field

import io.novafoundation.nova.common.base.BaseView
import io.novafoundation.nova.common.data.model.EditFieldPayload
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface EditFieldView : BaseView {
    fun initView(data: EditFieldPayload)
    fun enableButton(isEnabled: Boolean)
}
