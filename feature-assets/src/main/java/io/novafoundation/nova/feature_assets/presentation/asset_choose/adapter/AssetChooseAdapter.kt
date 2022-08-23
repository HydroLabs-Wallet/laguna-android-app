package io.novafoundation.nova.feature_assets.presentation.asset_choose.adapter

import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import coil.ImageLoader
import coil.load
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import io.novafoundation.nova.common.utils.format
import io.novafoundation.nova.common.utils.formatAsCurrency
import io.novafoundation.nova.feature_assets.R
import io.novafoundation.nova.feature_assets.databinding.ListitemAssetChooseBinding
import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import io.novafoundation.nova.feature_wallet_api.presentation.formatters.formatTokenAmount

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
                    val configuration = item.token.configuration

                    tvName.text = configuration.name
                    val amount = if (item.showValues) item.total.formatTokenAmount(configuration.symbol) else getString(R.string.value_hidden)
                    tvTokenAmount.text = amount
                    tvAmount.text = item.dollarAmount?.formatAsCurrency()
                    imIconBig.load(configuration.iconUrl, imageLoader)
                    imNotNative.isVisible = !item.isNative()
                    if (!item.isNative()) {
                        imNotNative.load(item.chain.icon, imageLoader)
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
