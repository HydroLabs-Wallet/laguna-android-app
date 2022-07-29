package io.novafoundation.nova.feature_assets.presentation.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import io.novafoundation.nova.common.utils.backgroundTint
import io.novafoundation.nova.common.utils.inflater
import io.novafoundation.nova.common.utils.tint
import io.novafoundation.nova.feature_assets.R
import io.novafoundation.nova.feature_assets.databinding.ViewTransactionStatusBinding
import io.novafoundation.nova.feature_assets.presentation.model.OperationStatusAppearance
import io.novafoundation.nova.feature_wallet_api.domain.model.Operation

class TransactionStatusView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: ViewTransactionStatusBinding =
        ViewTransactionStatusBinding.inflate(inflater, this, true)

    fun setStatus(statusAppearance: OperationStatusAppearance, operation: Operation) {
        with(binding) {
            when (statusAppearance) {
                OperationStatusAppearance.COMPLETED -> {
                    if (operation.type is Operation.Type.Transfer) {
                        val transfer = operation.type as Operation.Type.Transfer
                        if (transfer.myAddress == transfer.receiver) {
                            imStatus.setImageResource(R.drawable.ic_size_24_transaction_receive)
                            imStatus.tint(R.color.green500)
                            imStatus.backgroundTint(R.color.green100)
                            tvStatus.text = context.getString(R.string.receive)
                        } else {
                            imStatus.setImageResource(R.drawable.ic_size_24_transaction_send)
                            imStatus.tint(R.color.neutral500)
                            imStatus.backgroundTint(R.color.neutral100)
                            tvStatus.text = context.getString(R.string.sent)
                        }
                    } else {
                        imStatus.setImageResource(R.drawable.ic_size_24__transaction_failed)
                        imStatus.tint(R.color.red500)
                        imStatus.backgroundTint(R.color.red100)
                        tvStatus.text = context.getString(R.string.common_unknown)
                    }
                }
                OperationStatusAppearance.PENDING -> {
                    imStatus.setImageResource(R.drawable.ic_size_24_transaction_pending)
                    imStatus.tint(R.color.yellow500)
                    imStatus.backgroundTint(R.color.yellow100)
                    tvStatus.text = context.getString(R.string.sending)
                }
                OperationStatusAppearance.FAILED -> {
                    imStatus.setImageResource(R.drawable.ic_size_24__transaction_failed)
                    imStatus.tint(R.color.red500)
                    imStatus.backgroundTint(R.color.red100)
                    tvStatus.text = context.getString(R.string.failed)

                }
                else -> {
                    imStatus.setImageResource(R.drawable.ic_size_24__transaction_failed)
                    imStatus.tint(R.color.red500)
                    imStatus.backgroundTint(R.color.red100)
                    tvStatus.text = context.getString(R.string.common_unknown)
                }

//
//                TransactionStatus.APPROVED -> {
//                    imStatus.setImageResource(R.drawable.ic_size_24_transaction_approved)
//                    imStatus.tint(R.color.neutral500)
//                    imStatus.backgroundTint(R.color.neutral100)
//                    tvStatus.text = context.getString(R.string.approve_token)
//                }
//
//                TransactionStatus.SWAP -> {
//                    imStatus.setImageResource(R.drawable.ic_size_24_transaction_swap)
//                    imStatus.tint(R.color.green500)
//                    imStatus.backgroundTint(R.color.green100)
//                    tvStatus.text = context.getString(R.string.swap)
//                }


            }
        }
    }
}
