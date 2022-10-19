package io.novafoundation.nova.feature_assets.presentation.send.address_choose.adapter

import androidx.core.view.isVisible
import coil.load
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import io.novafoundation.nova.common.data.model.ContactUi
import io.novafoundation.nova.common.data.model.ContactUiHeader
import io.novafoundation.nova.common.data.model.ContactUiMarker
import io.novafoundation.nova.common.databinding.ListitemContactBinding
import io.novafoundation.nova.common.databinding.ListitemContactHeaderBinding
import io.novafoundation.nova.common.utils.AvatarUtils
import io.novafoundation.nova.common.utils.DefaultDiffUtilCallback

class SendAddressChooseAdapter() :
    AsyncListDifferDelegationAdapter<ContactUiMarker>(DefaultDiffUtilCallback()) {
    var onItemClick: ((ContactUi) -> Unit)? = null
    private val avatarUtils = AvatarUtils()

    init {
        delegatesManager.addDelegate(contactDelegate())
        delegatesManager.addDelegate(headerDelegate())

    }

    private fun contactDelegate() =
        adapterDelegateViewBinding<ContactUi, ContactUiMarker, ListitemContactBinding>(
            { layoutInflater, root ->
                ListitemContactBinding.inflate(layoutInflater, root, false)
            }
        ) {
            bind {
                with(binding) {
                    imAvatar.setImageResource(avatarUtils.randomAvatar().toInt())
                    tvMemo.isVisible=false
                    tvName.text = item.name
                    root.setOnClickListener {
                        onItemClick?.invoke(item)
                    }
                }
            }
        }

    private fun headerDelegate() =
        adapterDelegateViewBinding<ContactUiHeader, ContactUiMarker, ListitemContactHeaderBinding>(
            { layoutInflater, root ->
                ListitemContactHeaderBinding.inflate(layoutInflater, root, false)
            }
        ) {
            bind {
                with(binding) {
                    tvTitle.text = item.title

                }
            }
        }
}
