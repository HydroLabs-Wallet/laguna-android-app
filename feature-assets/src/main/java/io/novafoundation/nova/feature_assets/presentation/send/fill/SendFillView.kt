package io.novafoundation.nova.feature_assets.presentation.send.fill

import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import io.novafoundation.nova.feature_assets.presentation.send.ContactUi
import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SendFillView: BaseView {

    fun setReceiver(data: ContactUi)
    fun setSource(data: AssetModel)
    fun showFeeLoader(show: Boolean)
    fun setFee(currency: String, token: String)
    fun setValueTopTransfer(data: String?)
    fun setValueBottomTransfer(data: String?)
    fun showLoader(show: Boolean)
    fun setForceValues(top: String, bottom: String)
    fun setAmountName(data: String)
    fun enableButton(enabled: Boolean)
}
