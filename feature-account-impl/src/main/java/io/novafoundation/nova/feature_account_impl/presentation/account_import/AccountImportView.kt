package io.novafoundation.nova.feature_account_impl.presentation.account_import

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AccountImportView : MvpView {
    fun showProgress(show: Boolean)
    fun showError(data: String)
    fun enableButton(enable: Boolean)
    fun updateMode(mode: AccountImportWFragment.ImportMode)
}
