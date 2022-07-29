package io.novafoundation.nova.feature_assets.presentation.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import io.novafoundation.nova.common.utils.backgroundTint
import io.novafoundation.nova.common.utils.inflater
import io.novafoundation.nova.feature_assets.R
import io.novafoundation.nova.feature_assets.databinding.ViewTransactionDetailsStatusBinding
import io.novafoundation.nova.feature_assets.presentation.model.OperationStatusAppearance

class TransactionDetailsStatusView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: ViewTransactionDetailsStatusBinding =
        ViewTransactionDetailsStatusBinding.inflate(inflater, this, true)

    fun setStatus(status: OperationStatusAppearance) {
        with(binding) {
            when (status) {
                OperationStatusAppearance.PENDING -> {

                    tvTitle.backgroundTint(R.color.yellow500)
                    tvTitle.text = context.getString(R.string.pending)
                }
                OperationStatusAppearance.COMPLETED -> {
                    tvTitle.backgroundTint(R.color.green500)
                    tvTitle.text = context.getString(R.string.successful)
                }

                OperationStatusAppearance.FAILED -> {
                    tvTitle.backgroundTint(R.color.red500)
                    tvTitle.text = context.getString(R.string.failed)
                }
            }
        }
    }
}
