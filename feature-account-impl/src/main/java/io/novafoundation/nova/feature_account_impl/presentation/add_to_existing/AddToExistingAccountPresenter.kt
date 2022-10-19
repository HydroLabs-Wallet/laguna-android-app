package io.novafoundation.nova.feature_account_impl.presentation.add_to_existing

import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload
import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import moxy.InjectViewState
import javax.inject.Inject

@InjectViewState
class AddToExistingAccountPresenter @Inject constructor(
    private val router: AccountRouter,
    private val payload: AddAccountPayload
) :
    BasePresenter<AddToExistingAccountView>() {
    var isAuth = true

    fun onCreateClick() {
        router.toSeedPromptScreen(payload)
    }

    fun onImportClick() {
        router.toAccountImport(payload)
    }

    fun onSupportClick() {
        viewState.showSupportSnack()
    }

    fun onBackCommandClick() {
        router.back()
    }
}
