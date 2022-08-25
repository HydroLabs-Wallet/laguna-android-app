package io.novafoundation.nova.feature_assets.presentation.asset_transactions.details.transfer

import io.novafoundation.nova.common.base.BaseView
import io.novafoundation.nova.feature_assets.presentation.model.OperationParcelizeModel
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface TransferDetailsView : BaseView {
    fun setTransaction(data: OperationParcelizeModel.Transfer,showValue:Boolean)
    fun copyToClipboard(text: String)
//    fun showSaveMessage(hash: String)
}
