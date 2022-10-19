package io.novafoundation.nova.feature_account_impl.presentation.select_account

import androidx.core.text.isDigitsOnly
import androidx.core.view.isVisible
import coil.load
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import io.novafoundation.nova.common.utils.backgroundTint
import io.novafoundation.nova.feature_account_impl.R
import io.novafoundation.nova.feature_account_impl.databinding.ListitemSelectAccountBinding
import io.novafoundation.nova.feature_account_impl.presentation.LightMetaAccountDiffCallback
import io.novafoundation.nova.feature_account_impl.presentation.model.LightMetaAccountUi

class AccountsAdapter() :
    AsyncListDifferDelegationAdapter<LightMetaAccountUi>(LightMetaAccountDiffCallback) {
    var onItemClick: ((LightMetaAccountUi) -> Unit)? = null

    init {
        delegatesManager.addDelegate(assetDelegate())
    }

    private fun assetDelegate() =
        adapterDelegateViewBinding<LightMetaAccountUi, LightMetaAccountUi, ListitemSelectAccountBinding>(
            { layoutInflater, root ->
                ListitemSelectAccountBinding.inflate(layoutInflater, root, false)
            }
        ) {
            bind {
                with(binding) {
                    item.avatar?.let {
                        if (it.isDigitsOnly()) {
                            binding.imIcon.load(it.toInt())
                        } else {
                            binding.imIcon.load(it)
                        }
                    }
                    tvTitle.text = item.name
                    imCheck.isVisible = item.isSelected
                    tvAmount.text = "$0"
                    if (item.isSelected) {
                        holderContent.backgroundTint(R.color.neutral100)
                    } else {
                        holderContent.backgroundTint(R.color.white)

                    }
                    root.setOnClickListener {
                        onItemClick?.invoke(item)
                    }
                }
            }
        }
}
