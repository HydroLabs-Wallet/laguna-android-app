package io.novafoundation.nova.feature_onboarding_impl.presentation.welcome

import io.novafoundation.nova.feature_onboarding_impl.OnboardingRouter
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class OnboardingPresenter @Inject constructor(
    private val router: OnboardingRouter,

    ) :
    MvpPresenter<OnboardingView>() {
    var isAuth = true

    fun onCreateClick() {
        router.toSeedPromptScreen(isAuth)
    }

    fun onImportClick() {
        router.toAccountImport(isAuth)
    }

    fun onSupportClick() {
        viewState.showSupportSnack()
    }

    fun onBackCommandClick() {
        router.back()
    }
}
