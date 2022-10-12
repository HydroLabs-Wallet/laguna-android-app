package io.novafoundation.nova.feature_assets.presentation

import androidx.lifecycle.Lifecycle
import com.github.terrakok.cicerone.ResultListener
import io.novafoundation.nova.common.data.model.ConfirmPayload
import io.novafoundation.nova.common.data.model.EditFieldPayload
import io.novafoundation.nova.common.data.model.SelectAccountPayload
import io.novafoundation.nova.feature_assets.presentation.asset_receive_chooser.AssetReceivePayload
import io.novafoundation.nova.feature_assets.presentation.model.OperationParcelizeModel
import io.novafoundation.nova.common.data.model.ContactPayload
import io.novafoundation.nova.feature_assets.presentation.send.TransferDraft
import io.novafoundation.nova.feature_assets.presentation.send_receive.SendReceivePayload

interface WalletRouter {
    fun lockApp()
    fun setResult(key: String, data: Any)
    fun setResultListener(key: String, listener: ResultListener)
    fun toPasswordConfirm(data: ConfirmPayload)
    fun toEditField(data: EditFieldPayload)
    fun backToDashBoard()

    //receive
    fun toAssetReceive(assetPayload: AssetPayload)
    fun toAssetReceiveChooser(payload: AssetReceivePayload)

    fun toAssetDetails(data: AssetPayload)
    fun showSendReceiveDialog(data: SendReceivePayload)

    //transactions
    fun toAssetTransaction(data: AssetPayload)
    fun toTransferDetails(transaction: OperationParcelizeModel.Transfer)

    //send
    fun toSendAssetChooser()
    fun toSendAddressChooser(data: AssetPayload)
    fun toSendFill(data: TransferDraft)
    fun toSendConfirm(data: TransferDraft)
    fun toCreateContact(data: ContactPayload)
    fun toSelectAccount(data: SelectAccountPayload)


    fun back()

    fun openFilter()

    fun openSend(assetPayload: AssetPayload, initialRecipientAddress: String? = null)

    fun openConfirmTransfer(transferDraft: TransferDraft)

    fun finishSendFlow()


    fun openChangeAccount()

    fun openReceive(assetPayload: AssetPayload)

    fun openAssetFilters()

    fun openNfts()
    fun toAssetSelectionToReceive()

    val currentStackEntryLifecycle: Lifecycle
}
