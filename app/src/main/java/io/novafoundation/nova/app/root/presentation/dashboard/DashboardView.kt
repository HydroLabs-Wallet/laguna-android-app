package io.novafoundation.nova.app.root.presentation.dashboard

import io.novafoundation.nova.feature_assets.presentation.balance.list.model.TotalBalanceModel
import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface DashboardView : MvpView {
    fun showImportSnack()
    fun showSupportSnack()
    fun showContent(text: String)
    fun setAccountName(text: String)
    fun submitBalanceModel(data: TotalBalanceModel)
}
