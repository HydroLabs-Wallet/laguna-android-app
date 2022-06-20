package io.novafoundation.nova.app.root.presentation.dashboard.page_networks.adapter

import androidx.core.view.isVisible
import coil.ImageLoader
import coil.load
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import io.novafoundation.nova.app.databinding.ListitemNetworkBinding
import io.novafoundation.nova.common.utils.DefaultDiffUtilCallback
import io.novafoundation.nova.feature_assets.presentation.model.AssetMarker
import io.novafoundation.nova.feature_assets.presentation.model.AssetModel

class NetworksAdapter(private val imageLoader: ImageLoader) :
    AsyncListDifferDelegationAdapter<AssetMarker>(DefaultDiffUtilCallback()) {
    var onItemClick: ((AssetModel) -> Unit)? = null
    var onAddClick: (() -> Unit)? = null

    init {
        delegatesManager.addDelegate(assetDelegate())
//        delegatesManager.addDelegate(addDelegate())
    }

    private fun assetDelegate() =
        adapterDelegateViewBinding<AssetModel, AssetMarker, ListitemNetworkBinding>(
            { layoutInflater, root ->
                ListitemNetworkBinding.inflate(layoutInflater, root, false)
            }
        ) {
            bind {
                with(binding) {
                    val asset = item
                    val configuration = asset.token.configuration
                    tvTitle.text = configuration.name
                    imNotNative.isVisible = configuration.chainId != configuration.chainId
//                    tvAddress.text = asset.address?.ellipsis()
//                    tvAmount.text =
//                        asset.token.capitalisation.orZero().formatAsCurrency(asset.token.fiatSymbol)
                    imIcon.load(configuration.iconUrl, imageLoader)
                    tvAssetQuantity.text = "1 Asset"
                    root.setOnClickListener {
                        onItemClick?.invoke(item)
                    }
                }


            }

        }

//    private fun addDelegate() =
//        adapterDelegateViewBinding<AssetAddModel, AssetMarker, ListitemAssetAddBinding>(
//            { layoutInflater, root ->
//                ListitemAssetAddBinding.inflate(layoutInflater, root, false)
//            }
//        ) {
//            bind {
//
//                binding.root.setOnClickListener {
//                    onAddClick?.invoke()
//                }
//            }
//        }
}
