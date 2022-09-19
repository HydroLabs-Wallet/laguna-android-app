package io.novafoundation.nova.feature_account_impl.presentation.add_existing_account_complete

import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload
import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class AddToExistingAccountCompletePresenter @Inject constructor(
    private val router: AccountRouter,
    private val payload: AddAccountPayload
) :
    BasePresenter<AddToExistingAccountCompleteView>() {

    var isAuth = true


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
