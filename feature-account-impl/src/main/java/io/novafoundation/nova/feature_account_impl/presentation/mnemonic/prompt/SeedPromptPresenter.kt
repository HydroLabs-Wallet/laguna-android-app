package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt

import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import moxy.InjectViewState
import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload
import javax.inject.Inject

@InjectViewState
class SeedPromptPresenter @Inject constructor(
    private val router: AccountRouter,
    private val payload: AddAccountPayload
) :
    BasePresenter<SeedPromptView>() {

    fun onCreateClick() {
        router.toSeedCreate(payload)
    }

    fun onInfoClick() {
        router.toSeedInfo()
    }

    fun onSkipClick() {
        router.setResultListener(SeedPromptFragment.RESULT_PROMPT) { data ->
            val isAuth= when(payload){
                is AddAccountPayload.ChainAccount -> false
                is AddAccountPayload.MetaAccount -> payload.isAuth
            }
            when (data as SeedPromptFragment.ResultPrompt) {
                SeedPromptFragment.ResultPrompt.SKIP -> {
                    if (isAuth) {
                        router.toCreatePassword(payload)
                    } else {
                        router.toAccountComplete(payload)

                    }
                }
                SeedPromptFragment.ResultPrompt.SECURE -> {
                    router.toSeedCreate(payload)
                }
            }
        }
        router.toSeedWarning()
    }


    fun onBackCommandClick() {
        router.back()
    }
}
