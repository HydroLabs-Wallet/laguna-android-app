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
import io.novafoundation.nova.common.databinding.ViewRectButtonBinding
import io.novafoundation.nova.common.utils.inflater

class RectButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val binding: ViewRectButtonBinding =
        ViewRectButtonBinding.inflate(inflater, this, true)

    private var btnBackground: Drawable? = null
    private var btnBackgroundDisabled: Drawable? = null

    private var contentColor: Int = 0
    private var contentColorDisabled = ContextCompat.getColor(context, R.color.contentStateDisabled)
    private var isProgress = false

    init {
        applyAttributes(attrs)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        val onClickListener = OnClickListener { v ->
            if (isEnabled && !isProgress) {
                l?.onClick(v)
            }
        }
        binding.holder.setOnClickListener(onClickListener)
    }

    private fun applyAttributes(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RectButtonView)
            val type = RectButtonType.values()[typedArray.getInt(
                R.styleable.RectButtonView_rect_button_type,
                0
            )]
            setType(type)
            val size = RectButtonSize.values()[typedArray.getInt(
                R.styleable.RectButtonView_rect_button_size,
                0
            )]
            setSize(size)
            val gravity = RectButtonGravity.values()[typedArray.getInt(
                R.styleable.RectButtonView_rect_button_gravity,
                1
            )]
            setGravity(gravity)
            setImage(binding.imStart, typedArray, R.styleable.RectButtonView_rect_button_ic_start)
            setImage(binding.imEnd, typedArray, R.styleable.RectButtonView_rect_button_ic_end)
            setTextFromAttributes(
                binding.tvTitle,
                typedArray,
                R.styleable.RectButtonView_rect_button_text
            )
            ViewGroup.LayoutParams.WRAP_CONTENT
            setIsProgress(
                typedArray.getBoolean(
                    R.styleable.RectButtonView_rect_button_is_progress,
                    false
                )
            )
            binding.holder.isClickable = true
            binding.holder.isFocusable = true
            isEnabled = typedArray.getBoolean(R.styleable.RectButtonView_rect_button_enabled, true)
            typedArray.recycle()
        }
    }

    private fun setSize(size: RectButtonSize) {
        with(binding) {
            holder.orientation = LinearLayout.HORIZONTAL
            var buttonHeight = 0
            var imPadding = 0
            var progressSize = 0
            when (size) {
                RectButtonSize.LARGE -> {
                    buttonHeight = resources.getDimensionPixelSize(R.dimen.button_rect_large)
                    imPadding = resources.getDimensionPixelSize(R.dimen.button_img_padding_small)
                    progressSize = resources.getDimensionPixelSize(R.dimen.button_progress_large)
                    tvTitle.setTextAppearance(R.style.TextBodyBoldMedium)
                }
                RectButtonSize.MEDIUM -> {
                    buttonHeight = resources.getDimensionPixelSize(R.dimen.button_rect_medium)
                    imPadding = resources.getDimensionPixelSize(R.dimen.button_img_padding_small)
                    progressSize = resources.getDimensionPixelSize(R.dimen.button_progress_small)
                    tvTitle.setTextAppearance(R.style.TextBodyBoldSmall)

                }
                RectButtonSize.SMALL -> {
                    buttonHeight = resources.getDimensionPixelSize(R.dimen.button_rect_small)
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

    private fun setGravity(gravity: RectButtonGravity) {
        with(binding) {
            val ll = tvTitle.layoutParams as LinearLayout.LayoutParams
            when (gravity) {
                RectButtonGravity.START -> {
                    ll.width = 0
                    ll.weight = 1f
                    tvTitle.gravity = Gravity.START or Gravity.CENTER_VERTICAL
                    centerSpace.isVisible = true
                }
                RectButtonGravity.CENTER -> {
                    ll.width = LayoutParams.WRAP_CONTENT
                    ll.weight = 0f
                    tvTitle.gravity = Gravity.CENTER
                    centerSpace.isVisible = false
                }
            }
            tvTitle.layoutParams = ll
        }
    }

    private fun setType(type: RectButtonType) {
        with(binding) {
            when (type) {
                RectButtonType.PRIMARY -> {
                    holder.setBackgroundResource(R.drawable.bg_btn_rect_primary_active)
                    btnBackgroundDisabled = ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.bg_btn_rect_primary_disabled,
                        null
                    )
                    contentColor = ContextCompat.getColor(context, R.color.contentInversePrimary)
                }
                RectButtonType.SECONDARY -> {
                    holder.setBackgroundResource(R.drawable.bg_btn_rect_secondary_active)

                    btnBackgroundDisabled = holder.background
                    contentColor = ContextCompat.getColor(context, R.color.contentPrimary)

                }
                RectButtonType.TERTIARY -> {
                    holder.setBackgroundResource(R.drawable.bg_btn_rect_tertriary_active)
                    btnBackgroundDisabled = holder.background
                    contentColor = ContextCompat.getColor(context, R.color.contentPrimary)
                }
            }
            btnBackground = holder.background
        }
    }

    fun setIsProgress(isProgress: Boolean) {
        this.isProgress = isProgress
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
        invalidate()
    }

    fun setText(@StringRes id: Int) {
        binding.tvTitle.setText(id)
        invalidate()
    }

    enum class RectButtonType { PRIMARY, SECONDARY, TERTIARY }
    enum class RectButtonSize { LARGE, MEDIUM, SMALL }
    enum class RectButtonGravity { START, CENTER }
}
