package io.novafoundation.nova.feature_account_impl.presentation.select_account

import io.novafoundation.nova.common.data.model.SelectAccountPayload
import io.novafoundation.nova.common.utils.WithCoroutineScopeExtensions
import io.novafoundation.nova.common.utils.inBackground
import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import io.novafoundation.nova.feature_account_impl.presentation.mixin.api.AccountListingMixin
import io.novafoundation.nova.feature_account_impl.presentation.model.LightMetaAccountUi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class SelectAccountPresenter @Inject constructor(
    private val router: AccountRouter,
    private val payload: SelectAccountPayload,
    accountListingMixin: AccountListingMixin,
) :
    MvpPresenter<SelectAccountView>(), WithCoroutineScopeExtensions {
    override val coroutineScope = presenterScope

    val accountsFlow = accountListingMixin.accountsFlow()
        .inBackground()
        .share()


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.showAddButton(payload.showAdd)
        accountsFlow.onEach { viewState.submitList(it) }.launchIn(presenterScope)
    }

    fun onAddClick() {
        router.back()
        router.toOnboardingScreen()
    }

    fun onItemClicked(account: LightMetaAccountUi) {
        router.back()
        router.setResult(payload.tag, account.id)

    }

    fun onBackCommandClick() {
        router.back()
    }
}
