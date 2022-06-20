package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SeedPromptView : MvpView {
    fun showImportSnack()
    fun showSupportSnack()

}
