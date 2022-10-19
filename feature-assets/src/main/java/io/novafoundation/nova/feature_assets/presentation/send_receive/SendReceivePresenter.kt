package io.novafoundation.nova.feature_assets.presentation.send_receive

import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import moxy.InjectViewState
import io.novafoundation.nova.common.base.BasePresenter
import javax.inject.Inject

@InjectViewState
class SendReceivePresenter @Inject constructor(
    private val router: WalletRouter,
    private val payload: SendReceivePayload
) : BasePresenter<SendReceiveView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.setSendEnabled(payload.sendEnabled)
    }
    fun onSendClick(){
        router.back()
        router.toSendAssetChooser()
    }
    fun onReceiveClick() {
        router.back()
        router.toAssetSelectionToReceive()
    }

    fun onBackCommandClick() {
        router.back()
    }
}
