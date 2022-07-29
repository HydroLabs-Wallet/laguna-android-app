package io.novafoundation.nova.feature_assets.presentation.send.address_choose

import io.novafoundation.nova.feature_assets.presentation.send.ContactUiMarker
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SendAddressChooseView : MvpView {

    fun submitList(data: List<ContactUiMarker>, query:String)
    fun paste()
}
