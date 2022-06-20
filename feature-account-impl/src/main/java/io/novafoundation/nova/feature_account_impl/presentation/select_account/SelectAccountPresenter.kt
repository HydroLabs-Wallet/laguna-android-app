package io.novafoundation.nova.feature_account_impl.presentation.select_account

import io.novafoundation.nova.common.utils.WithCoroutineScopeExtensions
import io.novafoundation.nova.common.utils.inBackground
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountInteractor
import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import io.novafoundation.nova.feature_account_impl.presentation.account.list.AccountChosenNavDirection
import io.novafoundation.nova.feature_account_impl.presentation.mixin.api.AccountListingMixin
import io.novafoundation.nova.feature_account_impl.presentation.model.LightMetaAccountUi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class SelectAccountPresenter @Inject constructor(
    private val accountInteractor: AccountInteractor,
    private val router: AccountRouter,
    accountListingMixin: AccountListingMixin,
) :
    MvpPresenter<SelectAccountView>(), WithCoroutineScopeExtensions {
    override val coroutineScope = presenterScope

    val accountsFlow = accountListingMixin.accountsFlow()
        .inBackground()
        .share()


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        accountsFlow.onEach { viewState.submitList(it) }.launchIn(presenterScope)
    }

    fun onAddClick() {
        router.back()
        router.toOnboardingScreen()
    }

    fun onItemClicked(account: LightMetaAccountUi) {
        presenterScope.launch {
            accountInteractor.selectMetaAccount(account.id)
        }
        router.back()

    }

    fun onBackCommandClick() {
        router.back()
    }
}
