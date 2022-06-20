package io.novafoundation.nova.feature_assets.presentation.asset_transactions

import io.novafoundation.nova.feature_assets.presentation.transaction.history.model.OperationMarker
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AssetTransactionsView : MvpView {
    fun setTitle(name: String)
    fun submitList(data: List<OperationMarker>)
}
