package io.novafoundation.nova.feature_onboarding_impl.presentation.welcome

import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface OnboardingView: BaseView {
    fun showImportSnack()
    fun showSupportSnack()

}
