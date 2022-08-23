package io.novafoundation.nova.feature_account_impl.presentation.edit_field

import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.common.data.model.EditFieldPayload
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountInteractor
import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class EditFieldPresenter @Inject constructor(
    private val interactor: AccountInteractor,
    private val router: AccountRouter,
    private val payload: EditFieldPayload,
    private val resourceManager: ResourceManager
) :
    BasePresenter<EditFieldView>() {
    private var text: String? = ""
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.initView(payload)
        text=payload.text
        viewState.enableButton(text.orEmpty().isNotBlank())

    }

    fun onNextClick() {
        router.back()
        router.setResult(payload.tag, text.orEmpty())
    }

    fun onBackCommandClick() {
        router.back()
    }

    fun onPasswordChanged(text: String) {
        this.text = text
        viewState.enableButton(text.isNotBlank())
    }

}
