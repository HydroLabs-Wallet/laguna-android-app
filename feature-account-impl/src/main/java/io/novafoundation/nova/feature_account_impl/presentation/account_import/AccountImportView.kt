package io.novafoundation.nova.feature_account_impl.presentation.account_import

import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface AccountImportView: BaseView {
    fun showProgress(show: Boolean)
    fun enableButton(enable: Boolean)
    fun updateMode(mode: AccountImportFragment.ImportMode)
}
