package io.novafoundation.nova.feature_assets.presentation.asset_transactions.details.transfer

import io.novafoundation.nova.common.utils.equalTo
import io.novafoundation.nova.feature_assets.presentation.transaction.history.model.OperationMarker
import io.novafoundation.nova.feature_wallet_api.domain.model.Operation

class TransactionTransferModel(
    val id: String,
    val hash: String?,
    val nonce: Int,
    val date: Long,
    val webUrl: String,
    val tokenAmount: String,
    val currencyAmount: String,
    val myAddress: String,
    val sender: String,
    val receiver: String,
    val feeToken: String,
    val feeCurrency: String,
    val totalToken: String,
    val totalCurrency: String,
    val status: Operation.Status,
) : OperationMarker {
    override fun isItemSame(other: Any?): Boolean {
        return equalTo(other, { id })
    }

    override fun isContentSame(other: Any?): Boolean {
        return equalTo(
            other,
            { status },
            { tokenAmount },
            { currencyAmount },
            { feeToken },
            { feeCurrency },
            { totalToken },
            { totalCurrency })
    }
}
