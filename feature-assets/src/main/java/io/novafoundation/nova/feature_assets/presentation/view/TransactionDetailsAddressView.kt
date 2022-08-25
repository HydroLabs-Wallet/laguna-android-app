package io.novafoundation.nova.feature_assets.presentation.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import io.novafoundation.nova.common.utils.inflater
import io.novafoundation.nova.feature_assets.R
import io.novafoundation.nova.feature_assets.databinding.ViewTransactionDetailesAddressBinding

class TransactionDetailsAddressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: ViewTransactionDetailesAddressBinding =
        ViewTransactionDetailesAddressBinding.inflate(inflater, this, true)

    fun setData(isFrom: Boolean, address: String) {
        with(binding) {
            if (isFrom) {
                tvTitle.text = context.getString(R.string.from)
            } else {
                tvTitle.text = context.getString(R.string.to)
            }
            tvAddress.text = address
        }
    }

}
