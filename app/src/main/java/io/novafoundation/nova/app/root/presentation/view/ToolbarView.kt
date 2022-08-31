package io.novafoundation.nova.app.root.presentation.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.isDigitsOnly
import coil.load
import io.novafoundation.nova.app.databinding.ViewToolbarBinding
import io.novafoundation.nova.common.utils.inflater

class ToolbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: ViewToolbarBinding =
        ViewToolbarBinding.inflate(inflater, this, true)

    fun setName(name: String) {
        binding.tvTitle.text = name
    }

    fun setAvatar(data: String?) {
        data?.let {
            if (data.isDigitsOnly()) {
                binding.imAvatar.load(data.toInt())
            } else {
                binding.imAvatar.load(data)
            }
        }
    }

    fun setOnAvatarClickListener(l: OnClickListener?) {
        binding.holderAccount.setOnClickListener(l)
    }

    fun setOnMenuClickListener(l: OnClickListener?) {
        binding.imMenu.setOnClickListener(l)
    }
}
