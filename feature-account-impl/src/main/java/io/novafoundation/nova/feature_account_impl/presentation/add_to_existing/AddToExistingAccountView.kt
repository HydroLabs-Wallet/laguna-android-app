package io.novafoundation.nova.feature_account_impl.presentation.add_to_existing

import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AddToExistingAccountView: BaseView {
    fun showImportSnack()
    fun showSupportSnack()

}
