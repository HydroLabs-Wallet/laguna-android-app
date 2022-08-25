package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt.info

import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SeedInfoView: BaseView {
}
