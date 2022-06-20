package io.novafoundation.nova.common.view

import android.content.Context
import android.content.res.TypedArray
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import io.novafoundation.nova.common.R


import io.novafoundation.nova.common.databinding.ViewInputFieldBinding
import io.novafoundation.nova.common.utils.compatColor
import io.novafoundation.nova.common.utils.forceKeyboardPan
import io.novafoundation.nova.common.utils.hideKeyboardPan
import io.novafoundation.nova.common.utils.inflater
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class InputFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var isError = false
    private var isSuccess = false
    private var tempHintMessage: SpannableString = SpannableString("")
    protected var lastHint: SpannableString = SpannableString("")
    private val binding: ViewInputFieldBinding =
        ViewInputFieldBinding.inflate(inflater, this, true)

    private var showClearButton = true
    private var showTitle = true

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
                    isError = false
                    updateTextState()
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
        binding.tvText.setOnFocusChangeListener { v, hasFocus -> updateTextState() }
        binding.imClear.setOnClickListener { binding.tvText.setText("") }
    }

    private fun applyAttributes(attrs: AttributeSet?) {
        attrs?.let {
            val a = context.obtainStyledAttributes(attrs, R.styleable.InputFieldView)
            showClearButton = a.getBoolean(R.styleable.InputFieldView_if_show_clear, true)
            showTitle = a.getBoolean(R.styleable.InputFieldView_if_show_title, true)
            binding.tvTitle.isVisible = showTitle
            setTextFromAttributes(binding.tvTitle, a, R.styleable.InputFieldView_if_title)
            setTextFromAttributes(binding.tvText, a, R.styleable.InputFieldView_if_text)
            setHintFromAttributes(binding.tvText, a, R.styleable.InputFieldView_if_text_hint)

            setTextFromAttributes(binding.tvHint, a, R.styleable.InputFieldView_if_label_hint)
            binding.tvHint.isVisible =
                a.getBoolean(R.styleable.InputFieldView_if_label_visible, true)
            val inputTypeRaw = a.getInt(R.styleable.InputFieldView_if_input_type, 0)
            val inputType = IfInputType.values()[inputTypeRaw]
            setInputType(inputType)

            setImage(binding.imStart, a, R.styleable.InputFieldView_if_button_ic_start)
            setImage(binding.imEnd, a, R.styleable.InputFieldView_if_button_ic_end)

            lastHint = SpannableString(binding.tvHint.text)
            val imeOption =
                ImeOptions.values()[a.getInt(R.styleable.InputFieldView_if_ime_option, 0)]
            setImeOptions(imeOption)
            a.recycle()
            updateTextState()
        }
    }

    private fun setImage(imageView: ImageView, typedArray: TypedArray, reference: Int) {
        val resId = typedArray.getResourceId(reference, 0)
        imageView.setImageResource(resId)
        imageView.isVisible = resId != 0
    }

    private fun setInputType(type: IfInputType) {
        when (type) {
            IfInputType.TEXT -> {
                binding.tvText.inputType = EditorInfo.TYPE_CLASS_TEXT
            }
            IfInputType.PASSWORD -> {
                binding.tvText.inputType =
                    EditorInfo.TYPE_CLASS_TEXT or EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
    }

    private fun setImeOptions(imeOption: ImeOptions) {
        if (imeOption == ImeOptions.DONE) {
            binding.tvText.imeOptions = EditorInfo.IME_ACTION_DONE
            binding.tvText.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    binding.tvText.hideKeyboardPan()
                    true
                } else {
                    false
                }
            }
        } else {
            binding.tvText.imeOptions = EditorInfo.IME_ACTION_NEXT
        }
    }

    private fun updateTextState() {
        with(binding) {
            val isTextEmpty = tvText.text.toString().trim().isEmpty()
            val isFocused = tvText.isFocused
            imClear.isVisible = showClearButton && !isTextEmpty
            if (isSuccess) {
                tvHint.setTextColor(context.compatColor(R.color.green500))
                tvHint.text = tempHintMessage
                holderInput.setBackgroundResource(R.drawable.bg_input_field_success)
            } else if (isError) {
                tvHint.setTextColor(context.compatColor(R.color.red500))
                tvHint.text = tempHintMessage
                holderInput.setBackgroundResource(R.drawable.bg_input_field_error)
            } else if (isFocused || !isTextEmpty) {
                tvHint.setTextColor(context.compatColor(R.color.contentTertiary))
                tvHint.text = lastHint
                holderInput.setBackgroundResource(R.drawable.bg_input_field_focused)
            } else {
                tvHint.setTextColor(context.compatColor(R.color.contentTertiary))
                tvHint.text = lastHint
                holderInput.setBackgroundResource(R.drawable.bg_input_field_normal)
            }
        }
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

    fun updateState(data: InputFieldData) {
        isError = data.isError
        isSuccess = data.isSuccess
        tempHintMessage = data.hint
        updateTextState()
    }

    data class InputFieldData(
        var isError: Boolean = false,
        var isSuccess: Boolean = false,
        var text: String = "",
        var hint: SpannableString = SpannableString("")
    )

    private enum class IfInputType { TEXT, PASSWORD }
    private enum class ImeOptions {
        DONE, NEXT
    }
}
