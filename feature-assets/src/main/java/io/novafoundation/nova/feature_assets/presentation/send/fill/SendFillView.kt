package io.novafoundation.nova.feature_assets.presentation.send.fill

import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import io.novafoundation.nova.feature_assets.presentation.send.ContactUi
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SendFillView : MvpView {

    fun setReceiver(data: ContactUi)
    fun setSource(data: AssetModel)
    fun setFee(currency: String, token: String)
    fun setValueTopTransfer(data: String?)
    fun setValueBottomTransfer(data: String?)
    fun showError(data:String)
    fun showLoader(show: Boolean)
    fun setAmountName(data: String)
    fun enableButton(enabled: Boolean)
}
