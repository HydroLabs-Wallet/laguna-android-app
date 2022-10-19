package io.novafoundation.nova.feature_account_impl.presentation.menu.change_autolock

import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountInteractor
import io.novafoundation.nova.feature_account_impl.R
import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.presenterScope
import javax.inject.Inject
import kotlin.math.absoluteValue

@InjectViewState
class ChangeAutoLockPresenter @Inject constructor(
    private val interactor: AccountInteractor,
    private val router: AccountRouter,
    private val resourceManager: ResourceManager,

    ) :
    BasePresenter<ChangeAutoLockView>() {

    private var newTime = ""
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        interactor.getAutoLockTimer()
            .onEach { viewState.setCurrentTime(it) }
            .launchIn(presenterScope)
    }

    fun onCurrentPasswordTextChange(data: String) {
        newTime = data
        viewState.enableButton(newTime.isNotEmpty())
    }


    fun onNextClick() {
        presenterScope.launch {
            val time = newTime.filter { it.isDigit() }.toDouble().absoluteValue
            if (time / 60 > 24) {
                showError(resourceManager.getString(R.string.error_auto_lock))
            } else {
                interactor.saveAutoLockTimer(newTime)
                showSuccess(resourceManager.getString(R.string.autolock_changed))
                router.back()
            }
        }
    }

    fun onBackCommandClick() {
        router.back()
    }
}
