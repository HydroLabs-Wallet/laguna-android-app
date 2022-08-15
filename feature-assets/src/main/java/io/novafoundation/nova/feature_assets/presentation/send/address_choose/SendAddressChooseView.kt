package io.novafoundation.nova.feature_assets.presentation.send.address_choose

import io.novafoundation.nova.feature_assets.presentation.send.ContactUiMarker
import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SendAddressChooseView: BaseView {

    fun submitList(data: List<ContactUiMarker>, query: String)
    fun paste()
}
