package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt.warning

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SeedWarningView : MvpView {
    fun toggleCheckbox()
    fun enableButton(isEnabled: Boolean)

}
