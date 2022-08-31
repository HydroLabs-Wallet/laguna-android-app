package io.novafoundation.nova.feature_account_impl.presentation.menu.change_password

import android.text.SpannableString
import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.common.view.InputFieldView
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountInteractor
import io.novafoundation.nova.feature_account_impl.R
import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class ChangePasswordPresenter @Inject constructor(
    private val interactor: AccountInteractor,
    private val router: AccountRouter,
    private val resourceManager: ResourceManager,
    private val validatePasswordUseCase: ValidatePasswordUseCase

) :
    BasePresenter<ChangePasswordView>() {
    private var currentPassword = ""
    private var currentWasEdited = false
    private var newPassword = ""
    private var newWasEdited = false
    private var confirmPassword = ""
    private var confirmWasEdited = false

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        validate()
    }

    fun onCurrentPasswordTextChange(data: String) {
        currentPassword = data
        if (!currentWasEdited && data.isNotEmpty()) {
            currentWasEdited = true
        }
        validate()
    }

    fun onNewPasswordTextChange(data: String) {
        newPassword = data
        if (!newWasEdited && data.isNotEmpty()) {
            newWasEdited = true
        }
        validate()
    }

    fun onConfirmPasswordTextChange(data: String) {
        confirmPassword = data
        if (!confirmWasEdited && data.isNotEmpty()) {
            confirmWasEdited = true
        }
        validate()
    }


    fun onNextClick() {
        presenterScope.launch {
            val isValid = interactor.isPinCorrect(currentPassword)
            if (isValid) {
                interactor.savePin(newPassword)
                showSuccess(resourceManager.getString(R.string.password_changed_successfully))
                router.back()
            } else {
                val text = resourceManager.getString(R.string.password_do_not_match)
                viewState.updateCurrentPasswordState(InputFieldView.InputFieldData(isError = true, hint = SpannableString(text)))
                viewState.enableButton(false)
            }
        }
    }

    private fun validate() {
        val result = validatePasswordUseCase.validate(newPassword, confirmPassword)
        if (newWasEdited) {
            viewState.updateNewPasswordState(result.first)
        }
        if (confirmWasEdited) {
            viewState.updateConfirmPasswordState(result.second)
        }
        viewState.enableButton(currentPassword.isNotEmpty() && result.first.isSuccess && result.second.isSuccess)
    }

    fun onBackCommandClick() {
        router.back()
    }
}
