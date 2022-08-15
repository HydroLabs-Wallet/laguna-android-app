package io.novafoundation.nova.common.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.ContentViewCallback
import io.novafoundation.nova.common.R
import io.novafoundation.nova.common.databinding.ViewCustomSnackBinding
import io.novafoundation.nova.common.databinding.ViewSearchBinding
import io.novafoundation.nova.common.utils.backgroundTint
import io.novafoundation.nova.common.utils.inflater


class CustomSnackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ContentViewCallback {
    private val binding: ViewCustomSnackBinding =
        ViewCustomSnackBinding.inflate(inflater, this, true)

    override fun animateContentIn(delay: Int, duration: Int) {
        val scaleX = ObjectAnimator.ofFloat(binding.holderSnack, View.SCALE_X, 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(binding.holderSnack, View.SCALE_Y, 0f, 1f)
        val animatorSet = AnimatorSet().apply {
            interpolator = OvershootInterpolator()
            setDuration(500)
            playTogether(scaleX, scaleY)
        }
        animatorSet.start()
    }

    override fun animateContentOut(delay: Int, duration: Int) {

    }

    fun setText(isError: Boolean, text: String) {
        if (isError) {
            binding.im.setImageResource(R.drawable.ic_close_circle)
            binding.holderSnack.backgroundTint(R.color.red500)
        } else {
            binding.im.setImageResource(R.drawable.ic_checkmark_circle_16)
            binding.holderSnack.backgroundTint(R.color.green500)

        }
        binding.tvTitle.text = text
    }
}
