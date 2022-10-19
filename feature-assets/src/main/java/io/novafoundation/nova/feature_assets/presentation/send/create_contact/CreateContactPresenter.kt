package io.novafoundation.nova.feature_assets.presentation.send.create_contact

import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.common.data.model.Contact
import io.novafoundation.nova.common.data.model.ContactPayload
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.common.utils.DEFAULT_PREFIX
import io.novafoundation.nova.common.utils.WithCoroutineScopeExtensions
import io.novafoundation.nova.feature_assets.R
import io.novafoundation.nova.feature_assets.domain.send.SendInteractor
import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import jp.co.soramitsu.fearless_utils.ss58.SS58Encoder
import jp.co.soramitsu.fearless_utils.ss58.SS58Encoder.toAddress
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.presenterScope
import java.lang.Exception
import javax.inject.Inject

@InjectViewState
class CreateContactPresenter @Inject constructor(
    private val sendInteractor: SendInteractor,
    private val router: WalletRouter,
    private var payload: ContactPayload,
    private val resourceManager: ResourceManager

) : BasePresenter<CreateContactView>(), WithCoroutineScopeExtensions {
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
        try {
            val decoded = SS58Encoder.decode(payload.address)

            val substrateAddress = decoded.toAddress(SS58Encoder.DEFAULT_PREFIX)

            presenterScope.launch {
                sendInteractor.saveContact(Contact(name = name, address = substrateAddress, memo = null, id = null))
            }
            router.back()
        }catch (e:Exception){
            showError(resourceManager.getString(R.string.invalid_blockchain_address))

        }
    }

}
