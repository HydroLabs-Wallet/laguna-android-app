package io.novafoundation.nova.feature_account_impl.presentation.menu.change_avatar

import io.novafoundation.nova.common.base.BaseView
import io.novafoundation.nova.common.view.InputFieldView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ChangeAvatarView : BaseView {
    fun setImages(data: List<String>)
    fun setAvatar(data: String)
    fun enableButton(enable: Boolean)

}
