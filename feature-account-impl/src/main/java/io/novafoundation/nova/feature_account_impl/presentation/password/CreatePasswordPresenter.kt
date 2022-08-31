package io.novafoundation.nova.feature_account_impl.presentation.password

import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountInteractor
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload
import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import io.novafoundation.nova.feature_account_impl.presentation.menu.change_password.ValidatePasswordUseCase
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class CreatePasswordPresenter @Inject constructor(
    private val interactor: AccountInteractor,
    private val router: AccountRouter,
    private val resourceManager: ResourceManager,
    private val payload: AddAccountPayload,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) :
    BasePresenter<CreatePasswordView>() {
    private var password: String = ""
    private var passwordConfirm: String = ""
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.initView()
    }

    fun onNextClick() {
        presenterScope.launch {
            interactor.savePin(password)
            router.toAccountComplete(payload)
        }
    }

    fun onBackCommandClick() {
        router.back()
    }

    fun onPasswordChanged(text: String) {
        password = text
        validate()
    }

    fun onPasswordConfirmChanged(text: String) {
        passwordConfirm = text
        validate()
    }

    private fun validate() {
        val result = validatePasswordUseCase.validate(password, passwordConfirm)
        viewState.updatePasswordState(result.first)
        viewState.updateConfirmPasswordState(result.second)
        viewState.enableButton(result.first.isSuccess && result.second.isSuccess)
    }
}
