package io.novafoundation.nova.feature_account_impl.presentation.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import io.novafoundation.nova.common.utils.inflater
import io.novafoundation.nova.feature_account_impl.R
import io.novafoundation.nova.feature_account_impl.databinding.ViewRegisterIndicatorBinding


class RegisterIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: ViewRegisterIndicatorBinding =
        ViewRegisterIndicatorBinding.inflate(inflater, this, true)

    init {
        applyAttributes(attrs)
    }

    private fun applyAttributes(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.RegisterIndicatorView)

            val progress = typedArray.getInteger(R.styleable.RegisterIndicatorView_pi_progress, 0)
            setProgress(progress)
            typedArray.recycle()
        }
    }

    fun setProgress(progress: Int) {
        binding.progress.progress = progress
        val max = binding.progress.max
        binding.tvCounter.text = "$progress/$max"
        if (progress == 0 || progress == 1) {
            binding.imBack.setImageResource(R.drawable.ic_size_24_cross)
        } else {
            binding.imBack.setImageResource(R.drawable.ic_size_24_arrow_left)
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        binding.imBack.setOnClickListener(l)
    }
}
