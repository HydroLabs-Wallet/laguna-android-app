package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.create

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SeedCreateView : MvpView {

    fun setSeeds(data: List<SeedWord>)
    fun copyToClipboard(data: String)
    fun enableNext()
    fun disableNexT()
}
