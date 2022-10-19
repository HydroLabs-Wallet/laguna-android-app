package io.novafoundation.nova.feature_assets.presentation.all_transactions

import io.novafoundation.nova.feature_assets.presentation.transaction.history.model.OperationMarker
import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AllTransactionsView: BaseView {
    fun submitList(data: List<OperationMarker>)
}
