package io.novafoundation.nova.feature_account_impl.presentation.address_book

import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.common.data.model.ContactPayload
import io.novafoundation.nova.common.data.model.ContactUi
import io.novafoundation.nova.common.utils.WithCoroutineScopeExtensions
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountInteractor
import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.InjectViewState
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class AddressBookPresenter @Inject constructor(
    private val router: AccountRouter,
    private val accountInteractor: AccountInteractor
) : BasePresenter<AddressBookView>(), WithCoroutineScopeExtensions {

    override val coroutineScope: CoroutineScope = presenterScope

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        accountInteractor.getContacts()
            .onEach { viewState.submitList(it) }
            .launchIn(presenterScope)
    }


    fun onItemClicked(data: ContactUi) {
        val payload = ContactPayload(id = data.id, address = data.address, name = data.name, memo = data.memo)
        router.toAddressBookContact(payload)
    }

    fun onAddClick() {
        router.toAddressBookContact(ContactPayload(""))
    }

    fun onBackCommandClick() {
        router.back()
    }
}
