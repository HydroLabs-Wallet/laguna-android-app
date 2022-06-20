package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt

import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class SeedPromptPresenter @Inject constructor(
    private val router: AccountRouter
) :
    MvpPresenter<SeedPromptView>() {
    var isAuth = true

    fun onCreateClick() {
        router.toSeedCreate(isAuth)
    }

    fun onInfoClick() {
        router.toSeedInfo()
    }

    fun onSkipClick() {
        router.setResultListener(SeedPromptFragment.RESULT_PROMPT) { data ->
            when (data as SeedPromptFragment.ResultPrompt) {
                SeedPromptFragment.ResultPrompt.SKIP -> {
                    if (isAuth) {
                        router.toCreatePassword()
                    } else {
                        router.toAccountComplete(isAuth)

                    }
                }
                SeedPromptFragment.ResultPrompt.SECURE -> {
                    router.toSeedCreate(isAuth)
                }
            }
        }
        router.toSeedWarning()
    }


    fun onBackCommandClick() {
        router.back()
    }
}
