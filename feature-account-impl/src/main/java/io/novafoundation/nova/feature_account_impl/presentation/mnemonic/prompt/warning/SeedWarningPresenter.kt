package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt.warning

import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt.SeedPromptFragment
import com.github.terrakok.cicerone.Router
import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt.warning.SeedWarningView
import moxy.InjectViewState
import io.novafoundation.nova.common.base.BasePresenter
import javax.inject.Inject

@InjectViewState
class SeedWarningPresenter @Inject constructor(
    private val router: AccountRouter
) :
    BasePresenter<SeedWarningView>() {

    fun onAgree(isAgree: Boolean) {
        viewState.enableButton(isAgree)
    }

    fun toggleCheckBox() {
        viewState.toggleCheckbox()
    }

    fun onSecureClick() {
        router.back()
        router.setResult(SeedPromptFragment.RESULT_PROMPT, SeedPromptFragment.ResultPrompt.SECURE)
    }

    fun onSkipClick() {
        router.back()
        router.setResult(SeedPromptFragment.RESULT_PROMPT, SeedPromptFragment.ResultPrompt.SKIP)
    }

    fun onBackCommandClick() {
        router.back()
    }
}
