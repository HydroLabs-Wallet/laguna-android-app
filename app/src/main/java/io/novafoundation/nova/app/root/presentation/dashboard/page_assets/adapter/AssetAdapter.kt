package io.novafoundation.nova.app.root.presentation.dashboard.page_assets.adapter

import androidx.core.view.isVisible
import coil.ImageLoader
import coil.load
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import io.novafoundation.nova.app.databinding.ListitemAssetBinding
import io.novafoundation.nova.common.utils.DefaultDiffUtilCallback
import io.novafoundation.nova.common.utils.format
import io.novafoundation.nova.common.utils.formatAsCurrency
import io.novafoundation.nova.common.utils.setTextColorRes
import io.novafoundation.nova.feature_assets.presentation.model.AssetMarker
import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import io.novafoundation.nova.feature_wallet_api.presentation.formatters.formatTokenAmount

class AssetAdapter(private val imageLoader: ImageLoader) :
    AsyncListDifferDelegationAdapter<AssetMarker>(DefaultDiffUtilCallback()) {
    var onItemClick: ((AssetModel) -> Unit)? = null
    var onAddClick: (() -> Unit)? = null

    init {
        delegatesManager.addDelegate(assetDelegate())
//        delegatesManager.addDelegate(addDelegate())
    }

    private fun assetDelegate() =
        adapterDelegateViewBinding<AssetModel, AssetMarker, ListitemAssetBinding>(
            { layoutInflater, root ->
                ListitemAssetBinding.inflate(layoutInflater, root, false)
            }
        ) {
            bind {
                with(binding) {
                    val asset = item
                    val configuration = asset.token.configuration
                    tvTitle.text = configuration.name
                    imNotNative.isVisible = !item.isNative()
                    tvTokenAmount.text = asset.total.formatTokenAmount(asset.token.configuration.symbol)
                    tvAmount.text = asset.dollarAmount?.formatAsCurrency()
                    imIcon.load(configuration.iconUrl, imageLoader)
                    if (!item.isNative()) {
                        imNotNative.load(item.chain.icon, imageLoader)
                    }
                    tvDelta.text = asset.token.recentRateChange
                    tvDelta.setTextColorRes(asset.token.rateChangeColorRes)
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
