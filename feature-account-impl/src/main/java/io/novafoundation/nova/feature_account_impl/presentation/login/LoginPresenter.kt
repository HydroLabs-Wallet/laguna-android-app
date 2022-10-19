package io.novafoundation.nova.feature_account_impl.presentation.login

import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountInteractor
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload
import io.novafoundation.nova.feature_account_impl.R
import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class LoginPresenter @Inject constructor(
    private val interactor: AccountInteractor,
    private val router: AccountRouter,
    private val resourceManager: ResourceManager
) :
    BasePresenter<LoginView>() {
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
                val text = resourceManager.getString(R.string.password_do_not_match)
                showError(text)
            }
        }
    }

    fun onImportClick() {
        router.toAccountImport(AddAccountPayload.MetaAccount(true))
    }

    fun onSupportClick() {
        viewState.showSupportSnack()
    }


}
