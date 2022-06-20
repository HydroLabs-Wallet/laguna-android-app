package io.novafoundation.nova.feature_onboarding_impl.presentation.welcome

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface OnboardingView : MvpView {
    fun showImportSnack()
    fun showSupportSnack()

}
