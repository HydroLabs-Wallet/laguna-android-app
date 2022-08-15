package io.novafoundation.nova.feature_account_impl.presentation.select_account

import io.novafoundation.nova.feature_account_impl.presentation.model.LightMetaAccountUi
import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SelectAccountView: BaseView {
    fun submitList(data: List<LightMetaAccountUi>)
    fun showAddButton(show: Boolean)
}
