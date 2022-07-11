package io.novafoundation.nova.feature_assets.presentation

import androidx.lifecycle.Lifecycle
import com.github.terrakok.cicerone.ResultListener
import io.novafoundation.nova.feature_assets.presentation.asset_receive_chooser.AssetReceivePayload
import io.novafoundation.nova.feature_assets.presentation.model.OperationParcelizeModel
import io.novafoundation.nova.feature_assets.presentation.send.TransferDraft
import io.novafoundation.nova.feature_assets.presentation.send_receive.SendReceivePayload

interface WalletRouter {
    fun setResult(key: String, data: Any)
    fun setResultListener(key: String, listener: ResultListener)

    fun toAssetReceive(assetPayload: AssetPayload)
    fun toAssetReceiveChooser(payload: AssetReceivePayload)

    fun toAssetDetails(data: AssetPayload)
    fun showSendReceiveDialog(data: SendReceivePayload)
    fun toAssetTransaction(data: AssetPayload)
    fun toTransferDetails(transaction: OperationParcelizeModel.Transfer)

    fun back()

    fun openFilter()

    fun openSend(assetPayload: AssetPayload, initialRecipientAddress: String? = null)

    fun openConfirmTransfer(transferDraft: TransferDraft)

    fun finishSendFlow()

    fun openTransferDetail(transaction: OperationParcelizeModel.Transfer)

    fun openExtrinsicDetail(extrinsic: OperationParcelizeModel.Extrinsic)

    fun openRewardDetail(reward: OperationParcelizeModel.Reward)

    fun openChangeAccount()

    fun openReceive(assetPayload: AssetPayload)

    fun openAssetFilters()

    fun openNfts()
    fun toAssetSelectionToReceive()

    val currentStackEntryLifecycle: Lifecycle
}
