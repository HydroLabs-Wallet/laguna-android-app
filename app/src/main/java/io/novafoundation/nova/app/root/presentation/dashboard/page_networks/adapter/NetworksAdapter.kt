package io.novafoundation.nova.app.root.presentation.dashboard.page_networks.adapter

import androidx.recyclerview.widget.DiffUtil
import coil.ImageLoader
import coil.load
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import io.novafoundation.nova.app.R
import io.novafoundation.nova.app.databinding.ListitemNetworkBinding
import io.novafoundation.nova.common.utils.ellipsis
import io.novafoundation.nova.common.utils.formatAsCurrency
import io.novafoundation.nova.feature_wallet_api.domain.model.AssetGroup

private val diffUtilCallback = object : DiffUtil.ItemCallback<AssetGroup>() {
    override fun areItemsTheSame(oldItem: AssetGroup, newItem: AssetGroup): Boolean {
        return oldItem.chain.id == newItem.chain.id
    }

    override fun areContentsTheSame(oldItem: AssetGroup, newItem: AssetGroup): Boolean {
        return oldItem.groupBalanceFiat == newItem.groupBalanceFiat
    }
}

class NetworksAdapter(private val imageLoader: ImageLoader) :
    AsyncListDifferDelegationAdapter<AssetGroup>(diffUtilCallback) {
    var onItemClick: ((AssetGroup) -> Unit)? = null

    init {
        delegatesManager.addDelegate(assetDelegate())
    }

    private fun assetDelegate() =
        adapterDelegateViewBinding<AssetGroup, AssetGroup, ListitemNetworkBinding>(
            { layoutInflater, root ->
                ListitemNetworkBinding.inflate(layoutInflater, root, false)
            }
        ) {
            bind {
                with(binding) {
                    tvTitle.text = item.chain.name
                    tvAddress.text = item.address?.ellipsis()
                    tvAmount.text = item.groupBalanceFiat.formatAsCurrency()
                    imIcon.load(item.chain.icon, imageLoader)
                    tvAssetQuantity.text = context.resources.getQuantityString(R.plurals.n_assets, item.chain.assets.size, item.chain.assets.size)
                    root.setOnClickListener {
                        onItemClick?.invoke(item)
                    }
                }


            }

        }


}
