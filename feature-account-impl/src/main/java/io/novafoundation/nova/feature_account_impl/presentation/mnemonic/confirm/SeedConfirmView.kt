package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.confirm

import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.create.SeedWord
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SeedConfirmView : MvpView {

    fun setList(data: List<SeedWord>)
    fun setSelection(data: List<SeedWord>)
    fun showConfirmationError()
    fun enableButton(isEnabled: Boolean)
    fun showError(text:String)
}
