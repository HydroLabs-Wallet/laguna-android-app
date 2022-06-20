package io.novafoundation.nova.feature_assets.presentation.asset_choose.adapter

import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import coil.ImageLoader
import coil.load
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import io.novafoundation.nova.common.utils.format
import io.novafoundation.nova.common.utils.formatAsCurrency
import io.novafoundation.nova.feature_assets.databinding.ListitemAssetChooseBinding
import io.novafoundation.nova.feature_assets.presentation.model.AssetModel

class AssetChooseAdapter(private val imageLoader: ImageLoader) :
    AsyncListDifferDelegationAdapter<AssetModel>(AssetChooseDiffCallback()) {
    var onItemClick: ((AssetModel) -> Unit)? = null

    init {
        delegatesManager.addDelegate(assetReceiveDelegate())
    }

    private fun assetReceiveDelegate() =
        adapterDelegateViewBinding<AssetModel, AssetModel, ListitemAssetChooseBinding>(
            { layoutInflater, root ->
                ListitemAssetChooseBinding.inflate(layoutInflater, root, false)
            }
        ) {
            bind {
                with(binding) {
                    val asset = item
                    val configuration = asset.token.configuration

                    imNotNative.isVisible = configuration.chainId != configuration.name
                    tvName.text = configuration.name
                    tvTokenAmount.text = asset.total.format()
                    tvAmount.text = asset.dollarAmount?.formatAsCurrency()
                    imIconBig.load(configuration.iconUrl, imageLoader)
                    if (configuration.chainId != configuration.name) {
                        imNotNative.isVisible = true
                        imNotNative.load(asset.token.configuration.iconUrl, imageLoader)
                    } else {
                        imNotNative.isVisible = false
                    }
                    root.setOnClickListener {
                        onItemClick?.invoke(item)
                    }
                }
            }
        }

    class AssetChooseDiffCallback : DiffUtil.ItemCallback<AssetModel>() {
        override fun areItemsTheSame(oldItem: AssetModel, newItem: AssetModel): Boolean {
            val configurationOld = oldItem.token.configuration
            val configurationNew = newItem.token.configuration
            return configurationOld.id == configurationNew.id && configurationOld.chainId == configurationNew.chainId
        }

        override fun areContentsTheSame(
            oldItem: AssetModel,
            newItem: AssetModel
        ): Boolean {
            return oldItem.total == newItem.total
                && oldItem.available == newItem.available
                && oldItem.dollarAmount == newItem.dollarAmount
        }
    }
}
