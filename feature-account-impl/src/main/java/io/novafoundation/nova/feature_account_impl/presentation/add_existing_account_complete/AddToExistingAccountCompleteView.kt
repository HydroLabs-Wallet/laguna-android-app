package io.novafoundation.nova.feature_account_impl.presentation.add_existing_account_complete

import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AddToExistingAccountCompleteView: BaseView {

}
