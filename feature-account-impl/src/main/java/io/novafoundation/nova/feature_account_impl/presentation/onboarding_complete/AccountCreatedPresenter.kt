package io.novafoundation.nova.feature_account_impl.presentation.onboarding_complete

import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload
import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class AccountCreatedPresenter @Inject constructor(
    private val router: AccountRouter,
    private val payload: AddAccountPayload
) :
    BasePresenter<AccountCreatedView>() {

    var isAuth = true
    fun onDiscordClick() {
        viewState.showDiscordSnack()

    }

    fun onOnTwitterClick() {
        viewState.showTwitterSnack()
    }

    fun onOnNextClick() {
        if (isAuth) {
            router.toDashboard()
        } else {
            router.toLoginScreen()
        }
    }

    fun onBackCommandClick() {
        router.back()
    }
}
