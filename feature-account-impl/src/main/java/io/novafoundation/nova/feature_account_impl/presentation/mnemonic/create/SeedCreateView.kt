package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.create

import io.novafoundation.nova.common.base.BaseView
import io.novafoundation.nova.feature_account_api.presenatation.account.add.SeedWord
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SeedCreateView: BaseView {

    fun setSeeds(data: List<SeedWord>)
    fun copyToClipboard(data: String)
    fun enableNext()
    fun disableNexT()
}
