package io.novafoundation.nova.feature_onboarding_impl.presentation.welcome

import io.novafoundation.nova.feature_onboarding_impl.OnboardingRouter
import moxy.InjectViewState
import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload
import javax.inject.Inject

@InjectViewState
class OnboardingPresenter @Inject constructor(
    private val router: OnboardingRouter,
    private val payload: AddAccountPayload
) :
    BasePresenter<OnboardingView>() {
    var isAuth = true

    fun onCreateClick() {
        router.toSeedPromptScreen(payload)
    }

    fun onImportClick() {
        router.toAccountImport(payload)
    }

    fun onSupportClick() {
        viewState.showSupportSnack()
    }

    fun onBackCommandClick() {
        router.back()
    }
}
