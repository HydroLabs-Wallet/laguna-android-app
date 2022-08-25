package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.confirm

import io.novafoundation.nova.feature_account_api.presenatation.account.add.SeedWord
import io.novafoundation.nova.common.base.BaseView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SeedConfirmView: BaseView {

    fun setList(data: List<SeedWord>)
    fun setSelection(data: List<SeedWord>)
    fun enableButton(isEnabled: Boolean)
}
