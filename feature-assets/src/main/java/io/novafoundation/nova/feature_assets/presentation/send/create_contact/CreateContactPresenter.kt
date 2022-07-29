package io.novafoundation.nova.feature_assets.presentation.send.create_contact

import io.novafoundation.nova.common.utils.WithCoroutineScopeExtensions
import io.novafoundation.nova.feature_assets.domain.send.SendInteractor
import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import io.novafoundation.nova.feature_assets.presentation.send.ContactPayload
import io.novafoundation.nova.feature_wallet_api.domain.model.Contact
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class CreateContactPresenter @Inject constructor(
    private val sendInteractor: SendInteractor,
    private val router: WalletRouter,
    private var payload: ContactPayload
) : MvpPresenter<CreateContactView>(), WithCoroutineScopeExtensions {
    override val coroutineScope = presenterScope


    var name = ""

    fun onBackCommandClick() {
        router.back()
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.enableButton(name.isNotBlank())
        viewState.onAssetChanged(payload.address)

    }

    fun onTextChanged(data: String) {
        name = data
        viewState.enableButton(name.isNotBlank())
    }

    fun onNextClicked() {
        presenterScope.launch {
            sendInteractor.saveContact(Contact(name = name, address = payload.address))
        }
        router.back()
    }

}
