package io.novafoundation.nova.feature_assets.presentation.send_receive

import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class SendReceivePresenter @Inject constructor(
    private val router: WalletRouter,
    private val payload: SendReceivePayload
) : MvpPresenter<SendReceiveView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.setSendEnabled(payload.sendEnabled)
    }

    fun onAssetChooseClicked() {
        router.back()
        router.toAssetSelectionToReceive()
    }

    fun onBackCommandClick() {
        router.back()
    }
}
