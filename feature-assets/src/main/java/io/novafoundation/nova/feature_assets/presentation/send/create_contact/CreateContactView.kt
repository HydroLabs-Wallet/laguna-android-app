package io.novafoundation.nova.feature_assets.presentation.send.create_contact

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface CreateContactView : MvpView {
    fun onAssetChanged(data: String)
    fun enableButton(enabled:Boolean)
}
