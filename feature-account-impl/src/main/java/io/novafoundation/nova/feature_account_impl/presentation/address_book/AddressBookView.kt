package io.novafoundation.nova.feature_account_impl.presentation.address_book

import io.novafoundation.nova.common.base.BaseView
import io.novafoundation.nova.common.data.model.ContactUiMarker
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AddressBookView : BaseView {

    fun submitList(data: List<ContactUiMarker>)
}
