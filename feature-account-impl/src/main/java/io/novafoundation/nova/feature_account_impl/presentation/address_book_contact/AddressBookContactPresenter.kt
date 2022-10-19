package io.novafoundation.nova.feature_account_impl.presentation.address_book_contact

import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.common.data.model.Contact
import io.novafoundation.nova.common.data.model.ContactPayload
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.common.utils.DEFAULT_PREFIX
import io.novafoundation.nova.common.utils.WithCoroutineScopeExtensions
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountInteractor
import io.novafoundation.nova.feature_account_impl.R
import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import jp.co.soramitsu.fearless_utils.ss58.SS58Encoder
import jp.co.soramitsu.fearless_utils.ss58.SS58Encoder.toAddress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.presenterScope
import java.lang.Exception
import javax.inject.Inject

@InjectViewState
class AddressBookContactPresenter @Inject constructor(
    private val router: AccountRouter,
    private val accountInteractor: AccountInteractor,
    private val contactPayload: ContactPayload,
    private val resourceManager: ResourceManager
) : BasePresenter<AddressBookContactView>(), WithCoroutineScopeExtensions {

    override val coroutineScope: CoroutineScope = presenterScope

    var name = ""
    var address = ""
    var memo = ""

    var isNew = false
    var isEdit = false

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.setData(contactPayload.name.orEmpty(), contactPayload.address.orEmpty(), contactPayload.memo.orEmpty())
        isNew = contactPayload.name.isNullOrBlank()
        validate()
        setState()
    }

    fun onNameChange(data: String) {
        name = data
        validate()
    }

    fun onAddressChange(data: String) {
        address = data
        validate()
    }

    fun onMemoChange(data: String) {
        memo = data
    }

    fun setState() {
        if (isNew) {
            viewState.showCreateState()
        } else {
            if (isEdit) {
                viewState.showEditState()
            } else {
                viewState.showViewState(memo.isNotEmpty())
            }
        }
    }

    fun validate() {
        viewState.enableButton(name.isNotBlank() && address.isNotBlank())
    }

    fun onNextClick() {
        try {
            val decoded = SS58Encoder.decode(address)

            val substrateAddress = decoded.toAddress(SS58Encoder.DEFAULT_PREFIX)
            address = substrateAddress
            val contact =
                if (isNew) {
                    Contact(id = null, address = substrateAddress, name = name, memo = memo)
                } else {
                    Contact(id = contactPayload.id, address = substrateAddress, name = name, memo = memo)
                }

            presenterScope.launch {
                accountInteractor.saveContact(contact)
                if (isNew) {
                    showSuccess(resourceManager.getString(R.string.contact_saved))
                    router.back()
                } else {
                    viewState.setData(name, address, memo)
                    viewState.showViewState(memo.isNotEmpty())
                    isEdit = false
                }
            }
        }catch (e:Exception){
            showError(resourceManager.getString(R.string.invalid_blockchain_address))
        }
    }

    fun onCancelClick() {
        viewState.setData(contactPayload.name.orEmpty(), contactPayload.address.orEmpty(), contactPayload.memo.orEmpty())
        viewState.showViewState(memo.isNotEmpty())
        isEdit = false
    }

    fun onDeleteClick() {
        presenterScope.launch {
            accountInteractor.deleteContact(contactPayload.id!!)
            router.back()
        }
    }

    fun onActionClick() {
        if (isEdit) {
            onDeleteClick()
        } else {
            viewState.showEditState()
            isEdit = true
        }
    }

    fun onBackCommandClick() {
        router.back()
    }
}
