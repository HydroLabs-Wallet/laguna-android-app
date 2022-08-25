package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt

import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SeedPromptView: BaseView {
    fun showImportSnack()
    fun showSupportSnack()

}
