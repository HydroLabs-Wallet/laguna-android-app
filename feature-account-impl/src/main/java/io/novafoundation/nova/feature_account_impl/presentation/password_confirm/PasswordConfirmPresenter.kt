package io.novafoundation.nova.feature_account_impl.presentation.password_confirm

import io.novafoundation.nova.common.data.model.ConfirmPayload
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountInteractor
import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import kotlinx.coroutines.launch
import moxy.InjectViewState
import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.feature_account_impl.R
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class PasswordConfirmPresenter @Inject constructor(
    private val interactor: AccountInteractor,
    private val router: AccountRouter,
    private val payload: ConfirmPayload,
    private val resourceManager: ResourceManager
) :
    BasePresenter<PasswordConfirmView>() {
    private var password: String = ""
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.initView()
    }

    fun onNextClick() {
        presenterScope.launch {
            val valid = interactor.isPinCorrect(password)
            if (!valid) {
                viewState.showError(resourceManager.getString(R.string.password_do_not_match))
            } else {
                router.back()
                router.setResult(payload.tag, true)
            }
        }
    }

    fun onBackCommandClick() {
        router.back()
    }

    fun onPasswordChanged(text: String) {
        password = text
        viewState.enableButton(password.isNotBlank())
    }

}
