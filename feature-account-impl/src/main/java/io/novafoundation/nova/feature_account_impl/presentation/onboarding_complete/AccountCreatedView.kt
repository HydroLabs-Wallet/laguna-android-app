package io.novafoundation.nova.feature_account_impl.presentation.onboarding_complete

import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AccountCreatedView: BaseView {
    fun showDiscordSnack()
    fun showTwitterSnack()

}
