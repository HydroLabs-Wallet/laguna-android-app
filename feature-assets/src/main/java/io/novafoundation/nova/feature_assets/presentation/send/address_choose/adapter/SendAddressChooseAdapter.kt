package io.novafoundation.nova.feature_assets.presentation.send.address_choose.adapter

import coil.load
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import io.novafoundation.nova.common.utils.DefaultDiffUtilCallback
import io.novafoundation.nova.feature_assets.R
import io.novafoundation.nova.feature_assets.databinding.ListitemContactBinding
import io.novafoundation.nova.feature_assets.databinding.ListitemContactHeaderBinding
import io.novafoundation.nova.feature_assets.presentation.send.ContactUi
import io.novafoundation.nova.feature_assets.presentation.send.ContactUiHeader
import io.novafoundation.nova.feature_assets.presentation.send.ContactUiMarker

class SendAddressChooseAdapter() :
    AsyncListDifferDelegationAdapter<ContactUiMarker>(DefaultDiffUtilCallback()) {
    var onItemClick: ((ContactUi) -> Unit)? = null

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
                    imAvatar.load(R.drawable.bg_asset_primary)
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
