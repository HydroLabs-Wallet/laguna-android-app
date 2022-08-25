package io.novafoundation.nova.feature_assets.presentation.send.create_contact

import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface CreateContactView: BaseView {
    fun onAssetChanged(data: String)
    fun enableButton(enabled:Boolean)
}
