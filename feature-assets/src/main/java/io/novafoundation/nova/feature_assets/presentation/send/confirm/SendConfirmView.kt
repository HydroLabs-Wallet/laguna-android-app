package io.novafoundation.nova.feature_assets.presentation.send.confirm

import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import io.novafoundation.nova.feature_assets.presentation.send.TransferDraft
import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SendConfirmView: BaseView {

    fun setReceiver(data: TransferDraft, assetModel: AssetModel, account: String)
    fun startEditMode(edit: Boolean)
    fun setNote(data: String)

    fun showLoader(show: Boolean)
    fun enableButton(enabled: Boolean)
}
