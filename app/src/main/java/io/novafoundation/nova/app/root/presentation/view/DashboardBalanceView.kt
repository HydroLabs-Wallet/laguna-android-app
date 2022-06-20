package io.novafoundation.nova.app.root.presentation.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import io.novafoundation.nova.app.databinding.ViewDashbordBalanceViewBinding
import io.novafoundation.nova.common.utils.inflater

class DashboardBalanceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: ViewDashbordBalanceViewBinding =
        ViewDashbordBalanceViewBinding.inflate(inflater, this, true)

    fun updateValue(data: DashboardBalanceValue) {
        with(binding) {
            with(data) {
                holderNew.isVisible = !hasAccounts
                holderBalance.isVisible = hasAccounts
                tvAmount.text = balance
                tvDelta.text = delta
            }
        }
    }

    data class DashboardBalanceValue(
        val isHidden: Boolean,
        val hasAccounts: Boolean,
        val balance: String,
        val delta: String
    )
}
