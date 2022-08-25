package io.novafoundation.nova.app.root.presentation.dashboard.page_networks

import io.novafoundation.nova.feature_wallet_api.domain.model.AssetGroup
import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface PageNetworksView: BaseView {
    fun submitList(data: List<AssetGroup>)
}
