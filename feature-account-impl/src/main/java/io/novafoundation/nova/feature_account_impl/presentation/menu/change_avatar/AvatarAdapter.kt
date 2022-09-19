package io.novafoundation.nova.feature_account_impl.presentation.menu.change_avatar

import androidx.core.text.isDigitsOnly
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import coil.load
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import io.novafoundation.nova.feature_account_impl.databinding.ListitemAvatarBinding

private val diffUtilCallback = object : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return true
    }
}

class AvatarAdapter : AsyncListDifferDelegationAdapter<String>(diffUtilCallback) {
    var showNumber = true
    var onItemClick: ((String) -> Unit)? = null
    var selectedAvatar = ""

    init {
        delegatesManager.addDelegate(delegate())
    }

    private fun delegate() =
        adapterDelegateViewBinding<String, String, ListitemAvatarBinding>(
            { layoutInflater, root ->
                ListitemAvatarBinding.inflate(layoutInflater, root, false)
            }
        ) {
            bind {
                if (item.isDigitsOnly()) {
                    binding.imAvatar.load(item.toInt())
                } else {
                    binding.imAvatar.load(item)

                }
                if (selectedAvatar == item) {
                    binding.imSelected.isVisible = true
                } else {
                    binding.imSelected.isVisible = false
                }
                binding.root.setOnClickListener {
                    onItemClick?.invoke(item)
                }
            }
        }
}
