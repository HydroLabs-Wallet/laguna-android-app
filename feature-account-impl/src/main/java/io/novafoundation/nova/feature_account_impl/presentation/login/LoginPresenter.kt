package io.novafoundation.nova.feature_account_impl.presentation.login

import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountInteractor
import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class LoginPresenter @Inject constructor(
    private val interactor: AccountInteractor,
    private val router: AccountRouter,
) :
    MvpPresenter<LoginView>() {
    private var password = ""

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        validate()
    }

    fun onTextChanged(text: String) {
        password = text
        validate()
    }

    private fun validate() {
        viewState.enableButton(password.isNotEmpty())
    }

    fun onNextClick() {
        presenterScope.launch {
            val isValid = interactor.isPinCorrect(password)
            if (isValid) {
                router.toDashboard()
            } else {
                viewState.showPasswordError()
            }
        }
    }

    fun onImportClick() {
        router.toAccountImport(true)
    }

    fun onSupportClick() {
        viewState.showSupportSnack()
    }

    fun onBackCommandClick() {
        router.back()
    }
}
