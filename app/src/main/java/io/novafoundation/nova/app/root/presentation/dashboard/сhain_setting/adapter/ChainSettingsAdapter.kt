package io.novafoundation.nova.app.root.presentation.dashboard.сhain_setting.adapter

import androidx.core.view.isVisible
import coil.ImageLoader
import coil.load
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import io.novafoundation.nova.app.databinding.ListitemAssetSettingsBinding
import io.novafoundation.nova.common.utils.DefaultDiffUtilCallback
import io.novafoundation.nova.common.utils.format
import io.novafoundation.nova.feature_assets.presentation.model.AssetModel

class ChainSettingsAdapter(private val imageLoader: ImageLoader) :
    AsyncListDifferDelegationAdapter<AssetModel>(DefaultDiffUtilCallback()) {
    var onCheckClick: ((AssetModel, Boolean) -> Unit)? = null

    init {
        delegatesManager.addDelegate(assetDelegate())
    }

    private fun assetDelegate() =
        adapterDelegateViewBinding<AssetModel, AssetModel, ListitemAssetSettingsBinding>(
            { layoutInflater, root ->
                ListitemAssetSettingsBinding.inflate(layoutInflater, root, false)
            }
        ) {
            bind {
                with(binding) {
                    val asset = item
                    val configuration = asset.token.configuration
                    tvTitle.text = configuration.name
                    imNotNative.isVisible = configuration.chainId != configuration.name
                    tvTokenAmount.text = asset.total.format()
                    vSwitch.isChecked = true
                    vSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
                        onCheckClick?.invoke(item, isChecked)
                    }
                    imIcon.load(configuration.iconUrl, imageLoader)
                }
            }
        }
}
