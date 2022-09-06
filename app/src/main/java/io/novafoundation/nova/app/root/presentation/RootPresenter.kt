package io.novafoundation.nova.app.root.presentation

import android.util.Log
import androidx.fragment.app.Fragment
import io.novafoundation.nova.app.R
import io.novafoundation.nova.app.root.domain.RootInteractor
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.common.mixin.api.NetworkStateMixin
import io.novafoundation.nova.common.mixin.api.NetworkStateUi
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.core.updater.Updater
import io.novafoundation.nova.runtime.multiNetwork.connection.ChainConnection
import io.novafoundation.nova.splash.SplashRouter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.presenterScope
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@InjectViewState
class RootPresenter @Inject constructor(
    private val interactor: RootInteractor,
    private val router: RootRouter,
    private val splashRouter: SplashRouter,
    private val externalConnectionRequirementFlow: MutableStateFlow<ChainConnection.ExternalRequirement>,
    private val resourceManager: ResourceManager,
    private val networkStateMixin: NetworkStateMixin,
    private val autoLockUseCase: AutoLockUseCase
) : BasePresenter<RootView>(), NetworkStateUi by networkStateMixin {

    private var willBeClearedForLanguageChange = false
    private var lastActive = 0L
    private var autoLockTimer = 0
    override fun onFirstViewAttach() {
        interactor.runBalancesUpdate()
            .onEach { handleUpdatesSideEffect(it) }
            .launchIn(presenterScope)
        autoLockUseCase.getTimerFlow()
            .onEach { autoLockTimer = it.filter { it.isDigit() }.toInt() }
            .launchIn(presenterScope)
        updatePhishingAddresses()
        splashRouter.toSplashScreen()
    }

    private fun handleUpdatesSideEffect(sideEffect: Updater.SideEffect) {
        // pass
    }

    private fun updatePhishingAddresses() {
        presenterScope.launch {
            interactor.updatePhishingAddresses()
        }
    }

    fun jsonFileOpened(content: String?) {}

    override fun onDestroy() {
        externalConnectionRequirementFlow.value = ChainConnection.ExternalRequirement.FORBIDDEN
    }

    fun noticeInBackground() {
        lastActive = System.currentTimeMillis()

        if (!willBeClearedForLanguageChange) {
            externalConnectionRequirementFlow.value = ChainConnection.ExternalRequirement.STOPPED
        }
    }

    fun noticeInForeground(fragment: Fragment?) {
        val timeDiff = System.currentTimeMillis() - lastActive
        val timeMinutes = TimeUnit.MILLISECONDS.toMinutes(timeDiff)
        val isAuthorisedContent = if (fragment is BaseFragment<*>) fragment.isAuthorisedContent else false
        Log.e("mcheck","timeMinutes $timeMinutes isAuthorised $isAuthorisedContent  fragment ${fragment?.javaClass?.simpleName}")
        if (lastActive != 0L && timeMinutes >= autoLockTimer && isAuthorisedContent) {
            router.toLoginScreen()
        }
        if (externalConnectionRequirementFlow.value == ChainConnection.ExternalRequirement.STOPPED) {
            externalConnectionRequirementFlow.value = ChainConnection.ExternalRequirement.ALLOWED
        }
    }

    fun noticeLanguageLanguage() {
        willBeClearedForLanguageChange = true
    }

    fun restoredAfterConfigChange() {
        if (willBeClearedForLanguageChange) {
            router.returnToWallet()
            willBeClearedForLanguageChange = false
        }
    }

    fun externalUrlOpened(uri: String) {
        if (interactor.isBuyProviderRedirectLink(uri)) {
            viewState.showMessage(resourceManager.getString(R.string.buy_completed))
        }
    }
}
