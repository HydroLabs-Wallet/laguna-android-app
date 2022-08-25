package io.novafoundation.nova.feature_assets.presentation.asset_transactions

import io.novafoundation.nova.feature_assets.presentation.transaction.history.model.OperationMarker
import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AssetTransactionsView: BaseView {
    fun setTitle(name: String)
    fun submitList(data: List<OperationMarker>)
}
