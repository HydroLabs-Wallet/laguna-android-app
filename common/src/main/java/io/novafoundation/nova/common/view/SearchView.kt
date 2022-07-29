package io.novafoundation.nova.common.view


import android.content.Context
import android.content.res.TypedArray
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import io.novafoundation.nova.common.R
import io.novafoundation.nova.common.databinding.ViewSearchBinding
import io.novafoundation.nova.common.utils.compatColor
import io.novafoundation.nova.common.utils.forceKeyboardPan
import io.novafoundation.nova.common.utils.inflater
import io.novafoundation.nova.common.utils.setImageTint
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class SearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewSearchBinding =
        ViewSearchBinding.inflate(inflater, this, true)


    var onTextChanges: Flow<String> =
        callbackFlow {
            val listener = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = Unit
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    trySend(s?.toString() ?: "")
                }
            }
            binding.tvText.addTextChangedListener(listener)
            awaitClose { binding.tvText.removeTextChangedListener(listener) }
        }


    init {
        applyAttributes(attrs)
        binding.holderInput.setOnClickListener {
            binding.tvText.isVisible = true
            binding.tvText.forceKeyboardPan()
        }
    }

    private fun applyAttributes(attrs: AttributeSet?) {
        attrs?.let {
            val a = context.obtainStyledAttributes(attrs, R.styleable.SearchView)
            setTextFromAttributes(binding.tvText, a, R.styleable.SearchView_sv_text)
            setHintFromAttributes(binding.tvText, a, R.styleable.SearchView_sv_text_hint)


            setImage(binding.imStart, a, R.styleable.SearchView_sv_button_ic_start)
            setImage(binding.imEnd, a, R.styleable.SearchView_sv_button_ic_end)
            setTextFromAttributes(binding.tvEnd, a, R.styleable.SearchView_sv_button_text_end)
            binding.tvEnd.isVisible = binding.tvEnd.text.toString().isNotBlank()
            val defColor = context.compatColor(R.color.indigo500)
            val color = a.getColor(R.styleable.SearchView_sv_button_color_end, defColor)
            binding.tvEnd.setTextColor(color)
            binding.imEnd.setImageTint(color)
            a.recycle()
        }
    }

    private fun setImage(imageView: ImageView, typedArray: TypedArray, reference: Int) {
        val resId = typedArray.getResourceId(reference, 0)
        imageView.setImageResource(resId)
        imageView.isVisible = resId != 0
    }


    private fun setHintFromAttributes(textView: TextView, typedArray: TypedArray, reference: Int) {
        val textId = typedArray.getResourceId(reference, 0)
        val text = typedArray.getText(reference)
        if (textId != 0) {
            textView.setHint(textId)
        } else {
            textView.hint = text
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
    }

    fun setText(text: String?) {
        binding.tvText.setText(text)
    }

    fun setText(@StringRes id: Int) {
        binding.tvText.setText(id)
    }

    fun setTextEnd(@StringRes id: Int) {
        binding.tvEnd.setText(id)
        binding.tvEnd.isVisible = binding.tvEnd.text.toString().isNotBlank()
    }

    fun setIconEnd(@DrawableRes id: Int) {
        binding.imEnd.setImageResource(id)
        binding.imEnd.isVisible = id != 0
    }

    fun setOnEndClickListener(l: OnClickListener?) {
        binding.imEnd.setOnClickListener(l)
        binding.tvEnd.setOnClickListener(l)

    }
}
