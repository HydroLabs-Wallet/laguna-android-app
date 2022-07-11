package io.novafoundation.nova.app.root.presentation.dashboard.page_networks

import io.novafoundation.nova.feature_wallet_api.domain.model.AssetGroup
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface PageNetworksView : MvpView {
    fun submitList(data: List<AssetGroup>)
}
