package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt.info

import com.github.terrakok.cicerone.Router
import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject

@InjectViewState
class SeedInfoPresenter @Inject constructor(
    private val router: AccountRouter
) :
    MvpPresenter<SeedInfoView>() {

    fun onNextClick() {
        router.back()
    }

    fun onBackCommandClick() {
        router.back()
    }
}
