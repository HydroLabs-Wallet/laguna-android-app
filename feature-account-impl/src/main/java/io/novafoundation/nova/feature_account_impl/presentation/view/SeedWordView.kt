package io.novafoundation.nova.feature_account_impl.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.view.isVisible
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.create.SeedWord
import io.novafoundation.nova.common.utils.compatColor
import io.novafoundation.nova.common.utils.inflater
import io.novafoundation.nova.feature_account_impl.R
import io.novafoundation.nova.feature_account_impl.databinding.ViewSeedWordBinding

class SeedWordView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val binding: ViewSeedWordBinding = ViewSeedWordBinding.inflate(inflater, this, true)

    override fun setBackgroundResource(resid: Int) {
        binding.bg.setBackgroundResource(resid)
    }

    fun setData(data: SeedWord, showNumber: Boolean) {
        with(binding) {
            tvLabel.text = data.number.toString()
            tvTitle.text = data.word
            tvLabel.isVisible = showNumber
            with(data) {
                if (isVerify) {
                    if (isFocused) {
                        bg.setBackgroundResource(R.drawable.bg_rounded_4_yellow)
                        tvTitle.setTextColor(
                            context.compatColor(R.color.yellow600)
                        )
                    } else if (isChecked) {
                        bg.setBackgroundResource(R.drawable.bg_rounded_4_green_selected)
                        tvTitle.setTextColor(context.compatColor(R.color.green600))
                    } else {
                        bg.setBackgroundResource(R.drawable.bg_rounded_4_grey_selected)
                        tvTitle.setTextColor(context.compatColor(R.color.contentPrimary))
                    }
                } else {
                    if (isChecked) {
                        bg.setBackgroundResource(R.drawable.bg_rounded_4_grey_selected)
                    } else {
                        bg.setBackgroundResource(R.drawable.bg_rounded_4_grey_unselected)
                    }
                }
            }
        }
    }
}
