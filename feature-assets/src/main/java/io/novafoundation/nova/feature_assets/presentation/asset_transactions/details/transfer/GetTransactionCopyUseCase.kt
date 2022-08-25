package io.novafoundation.nova.feature_assets.presentation.asset_transactions.details.transfer

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class GetTransactionCopyUseCase @Inject constructor() {
    operator fun invoke(data: TransactionTransferModel): String {
        val builder = StringBuilder()
        builder.append("\"${data.status.toString()}\"")
        builder.append(",")
        builder.append("\"${data.hash}\"")
        builder.append("\n")

        builder.append("\"From\"")
        builder.append(",")
        builder.append("\"${data.sender}\"")
        builder.append("\n")

        builder.append("\"To\"")
        builder.append(",")
        builder.append("\"${data.receiver}\"")
        builder.append("\n")

        builder.append("\"Nonce\"")
        builder.append(",")
        builder.append("\"${data.nonce}\"")
        builder.append("\n")

        builder.append("\"Amount\"")
        builder.append(",")
        builder.append("\"${data.tokenAmount}\"")
        builder.append("\n")

        builder.append("\"Gas Fee\"")
        builder.append(",")
        builder.append("\"${data.feeToken}\"")
        builder.append("\n")

        builder.append("\"Total\"")
        builder.append(",")
        builder.append("\"${data.totalToken}\"")
        builder.append("\n")

        builder.append("\"Total in currency\"")
        builder.append(",")
        builder.append("\"${data.totalCurrency}\"")
        builder.append("\n")


        val df = SimpleDateFormat("dd/MMM/yyyy h:mm:ss a", Locale.ENGLISH)
        val date = df.format(data.date)
        builder.append("\"Date\"")
        builder.append(",")
        builder.append("\"${date}\"")
        builder.append("\n")
        return builder.toString()
    }
}
