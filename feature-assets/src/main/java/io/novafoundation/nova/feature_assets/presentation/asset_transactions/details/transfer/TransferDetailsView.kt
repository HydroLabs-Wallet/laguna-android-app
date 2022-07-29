package io.novafoundation.nova.feature_assets.presentation.asset_transactions.details.transfer

import io.novafoundation.nova.feature_assets.presentation.model.OperationParcelizeModel
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface TransferDetailsView : MvpView {
    fun setTransaction(data: OperationParcelizeModel.Transfer)
    fun copyToClipboard(text: String)
    fun showSaveMessage(hash: String)
}
