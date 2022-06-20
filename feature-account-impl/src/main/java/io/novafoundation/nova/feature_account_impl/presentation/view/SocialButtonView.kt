package io.novafoundation.nova.feature_account_impl.presentation.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import io.novafoundation.nova.common.utils.inflater
import io.novafoundation.nova.feature_account_impl.R
import io.novafoundation.nova.feature_account_impl.databinding.ViewSocialButtonBinding

class SocialButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: ViewSocialButtonBinding =
        ViewSocialButtonBinding.inflate(inflater, this, true)

    init {
        applyAttributes(attrs)
    }

    private fun applyAttributes(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SocialButtonView)
            setTextFromAttributes(
                binding.tvTitle,
                typedArray,
                R.styleable.SocialButtonView_social_text
            )
            val imId = typedArray.getResourceId(R.styleable.SocialButtonView_social_icon, 0)
            binding.image.setImageResource(imId)
            typedArray.recycle()
        }
    }

    private fun setTextFromAttributes(textView: TextView, typedArray: TypedArray, reference: Int) {
        val textId = typedArray.getResourceId(reference, 0)
        val text = typedArray.getText(reference)
        if (textId != 0) {
            textView.setText(textId)
        } else {
            textView.text = text
        }
        textView.isVisible = textView.text.isNotEmpty()
    }

    fun setText(text: String?) {
        binding.tvTitle.text = text
    }

    fun setText(@StringRes id: Int) {
        binding.tvTitle.setText(id)
    }
}
