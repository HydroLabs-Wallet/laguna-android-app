package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt.info

import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class SeedInfoPresenter @Inject constructor(
    private val router: AccountRouter
) :
    BasePresenter<SeedInfoView>() {

    fun onNextClick() {
        router.back()
    }

    fun onBackCommandClick() {
        router.back()
    }
}
