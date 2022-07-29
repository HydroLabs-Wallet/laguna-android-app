package io.novafoundation.nova.common.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import io.novafoundation.nova.common.R
import io.novafoundation.nova.common.databinding.ViewPillButtonBinding
import io.novafoundation.nova.common.utils.inflater

class PillButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val binding: ViewPillButtonBinding =
        ViewPillButtonBinding.inflate(inflater, this, true)

    private var btnBackground: Drawable? = null
    private var btnBackgroundDisabled: Drawable? = null

    private var contentColor: Int = 0
    private var contentColorDisabled = ContextCompat.getColor(context, R.color.contentStateDisabled)

    init {
        applyAttributes(attrs)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        val onClickListener = OnClickListener { v ->
            if (isEnabled) {
                l?.onClick(v)
            }
        }
        binding.holder.setOnClickListener(onClickListener)
    }

    private fun applyAttributes(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PillButtonView)
            val type = PillButtonType.values()[typedArray.getInt(
                R.styleable.PillButtonView_pill_button_type,
                0
            )]
            setType(type)
            val size = PillButtonSize.values()[typedArray.getInt(
                R.styleable.PillButtonView_pill_button_size,
                0
            )]
            setSize(size)
            val gravity = PillButtonGravity.values()[typedArray.getInt(
                R.styleable.PillButtonView_pill_button_gravity,
                1
            )]
            setGravity(gravity)
            setImage(binding.imStart, typedArray, R.styleable.PillButtonView_pill_button_ic_start)
            setImage(binding.imEnd, typedArray, R.styleable.PillButtonView_pill_button_ic_end)
            setTextFromAttributes(
                binding.tvTitle,
                typedArray,
                R.styleable.PillButtonView_pill_button_text
            )
            ViewGroup.LayoutParams.WRAP_CONTENT
            setIsProgress(
                typedArray.getBoolean(
                    R.styleable.PillButtonView_pill_button_is_progress,
                    false
                )
            )
            binding.holder.isClickable = true
            binding.holder.isFocusable = true
            isEnabled = typedArray.getBoolean(R.styleable.PillButtonView_pill_button_enabled, true)
            typedArray.recycle()
        }
    }

    private fun setSize(size: PillButtonSize) {
        with(binding) {
            holder.orientation = LinearLayout.HORIZONTAL
            var buttonHeight = 0
            var imPadding = 0
            var progressSize = 0
            when (size) {
                PillButtonSize.LARGE -> {
                    buttonHeight = resources.getDimensionPixelSize(R.dimen.button_pill_large)
                    imPadding = resources.getDimensionPixelSize(R.dimen.button_img_padding_small)
                    progressSize = resources.getDimensionPixelSize(R.dimen.button_progress_large)
                    tvTitle.setTextAppearance(R.style.TextBodyBoldMedium)
                }
                PillButtonSize.MEDIUM -> {
                    buttonHeight = resources.getDimensionPixelSize(R.dimen.button_pill_medium)
                    imPadding = resources.getDimensionPixelSize(R.dimen.button_img_padding_small)
                    progressSize = resources.getDimensionPixelSize(R.dimen.button_progress_small)
                    tvTitle.setTextAppearance(R.style.TextBodyBoldSmall)

                }
                PillButtonSize.SMALL -> {
                    buttonHeight = resources.getDimensionPixelSize(R.dimen.button_pill_small)
                    imPadding = resources.getDimensionPixelSize(R.dimen.button_img_padding_small)
                    progressSize = resources.getDimensionPixelSize(R.dimen.button_progress_small)
                    tvTitle.setTextAppearance(R.style.TextCaptionBold)

                }
            }
            imStart.setPadding(imPadding)
            imEnd.setPadding(imPadding)

            val llButton = holder.layoutParams as LinearLayout.LayoutParams
            llButton.height = buttonHeight
            holder.layoutParams = llButton

            val llProgress = progress.layoutParams
            llProgress.height = progressSize
            llProgress.width = progressSize
            progress.layoutParams = llProgress
            progress.indicatorSize = progressSize
        }
    }

    private fun setGravity(gravity: PillButtonGravity) {
        with(binding) {
            val ll = tvTitle.layoutParams as LinearLayout.LayoutParams
            when (gravity) {
                PillButtonGravity.START -> {
                    ll.width = 0
                    ll.weight = 1f
                    tvTitle.gravity = Gravity.START or Gravity.CENTER_VERTICAL
                    centerSpace.isVisible = true
                }
                PillButtonGravity.CENTER -> {
                    ll.width = LayoutParams.WRAP_CONTENT
                    ll.weight = 0f
                    tvTitle.gravity = Gravity.CENTER
                    centerSpace.isVisible = false
                }
            }
            tvTitle.layoutParams = ll
        }
    }

    private fun setType(type: PillButtonType) {
        with(binding) {
            when (type) {
                PillButtonType.PRIMARY -> {
                    holder.setBackgroundResource(R.drawable.bg_btn_pill_primary_active)

                    btnBackgroundDisabled = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.bg_btn_pill_primary_disabled,
                        null
                    )
                    contentColor = ContextCompat.getColor(context, R.color.contentInversePrimary)
                }
                PillButtonType.SECONDARY -> {
                    holder.setBackgroundResource(R.drawable.bg_btn_pill_secondary_active)
                    btnBackgroundDisabled = holder.background
                    contentColor = ContextCompat.getColor(context, R.color.contentPrimary)

                }
            }
            btnBackground = holder.background
        }
    }

    private fun setIsProgress(isProgress: Boolean) {
        with(binding) {
            tvTitle.isVisible = !isProgress && tvTitle.text.isNotEmpty()
            imStart.isVisible = !isProgress && imStart.drawable != null
            imEnd.isVisible = !isProgress && imEnd.drawable != null
            progress.isVisible = isProgress
        }
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        with(binding) {
            if (enabled) {
                holder.background = btnBackground
                tvTitle.setTextColor(contentColor)
                imStart.setColorFilter(contentColor)
                imEnd.setColorFilter(contentColor)
                progress.setIndicatorColor(contentColor)

            } else {
                holder.background = btnBackgroundDisabled
                tvTitle.setTextColor(contentColorDisabled)
                imStart.setColorFilter(contentColorDisabled)
                imEnd.setColorFilter(contentColorDisabled)
                progress.setIndicatorColor(contentColorDisabled)

            }

        }
    }

    private fun setImage(imageView: ImageView, typedArray: TypedArray, reference: Int) {
        val resId = typedArray.getResourceId(reference, 0)
        imageView.setImageResource(resId)
        imageView.isVisible = resId != 0
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
        binding.tvTitle.isVisible = binding.tvTitle.text.isNotEmpty()

    }

    fun setText(@StringRes id: Int) {
        binding.tvTitle.setText(id)
    }

    enum class PillButtonType { PRIMARY, SECONDARY }
    enum class PillButtonSize { LARGE, MEDIUM, SMALL }
    enum class PillButtonGravity { START, CENTER }
}
