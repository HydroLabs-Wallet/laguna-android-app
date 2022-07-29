package io.novafoundation.nova.feature_assets.presentation.send.confirm

import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import io.novafoundation.nova.feature_assets.presentation.send.TransferDraft
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SendConfirmView : MvpView {

    fun setReceiver(data: TransferDraft, assetModel: AssetModel, account: String)
    fun startEditMode(edit: Boolean)
    fun setNote(data: String)
    fun showSuccess(data: String)
    fun showError(data:String)
    fun showLoader(show: Boolean)
    fun enableButton(enabled: Boolean)
}
