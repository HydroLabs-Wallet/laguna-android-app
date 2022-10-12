package io.novafoundation.nova.feature_account_impl.presentation.address_book_contact

import io.novafoundation.nova.common.base.BaseView
import io.novafoundation.nova.common.data.model.ContactUiMarker
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AddressBookContactView : BaseView {
    fun setData(name: String, address: String, memo: String)
    fun showCreateState()
    fun showViewState(hasMemo:Boolean)
    fun showEditState()

    fun enableButton(enable: Boolean)
}
