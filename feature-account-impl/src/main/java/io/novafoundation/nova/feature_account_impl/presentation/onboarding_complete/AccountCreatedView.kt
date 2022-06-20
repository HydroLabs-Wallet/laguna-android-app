package io.novafoundation.nova.feature_account_impl.presentation.onboarding_complete

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AccountCreatedView : MvpView {
    fun showDiscordSnack()
    fun showTwitterSnack()

}
