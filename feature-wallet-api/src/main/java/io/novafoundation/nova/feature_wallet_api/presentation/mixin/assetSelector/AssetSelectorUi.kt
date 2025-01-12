package io.novafoundation.nova.feature_wallet_api.presentation.mixin.assetSelector

import coil.ImageLoader
import io.novafoundation.nova.common.base.BaseFragmentOld
import io.novafoundation.nova.common.base.BaseViewModel
import io.novafoundation.nova.feature_wallet_api.presentation.view.AssetSelectorBottomSheet
import io.novafoundation.nova.feature_wallet_api.presentation.view.AssetSelectorView

interface WithAssetSelector {

    val assetSelectorMixin: AssetSelectorMixin
}

fun <V> BaseFragmentOld<V>.setupAssetSelector(
    view: AssetSelectorView,
    viewModel: V,
    imageLoader: ImageLoader
) where V : BaseViewModel, V : WithAssetSelector {
    view.onClick {
        viewModel.assetSelectorMixin.assetSelectorClicked()
    }

    viewModel.assetSelectorMixin.selectedAssetModelFlow.observe {
        view.setState(imageLoader, it)
    }

    viewModel.assetSelectorMixin.showAssetChooser.observeEvent {
        AssetSelectorBottomSheet(
            imageLoader = imageLoader,
            context = requireContext(),
            payload = it,
            onClicked = viewModel.assetSelectorMixin::assetChosen
        ).show()
    }
}
