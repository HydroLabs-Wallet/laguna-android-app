package io.novafoundation.nova.common.base

import io.novafoundation.nova.common.utils.WithCoroutineScopeExtensions
import io.novafoundation.nova.common.validation.ProgressConsumer
import io.novafoundation.nova.common.validation.ValidationExecutor
import io.novafoundation.nova.common.validation.ValidationSystem
import kotlinx.coroutines.CoroutineScope
import moxy.MvpPresenter
import moxy.presenterScope

abstract class BasePresenter<V : BaseView> : MvpPresenter<V>(), WithCoroutineScopeExtensions {

    override val coroutineScope: CoroutineScope
        get() = presenterScope


    fun showError(data: String) {
        viewState.showError(data)
    }

    fun showError(data: Throwable) {
        viewState.showError(data.message ?: "unknown")
    }

    fun showSuccess(data: String) {
        viewState.showSuccess(data)
    }

    suspend fun <P, S> ValidationExecutor.requireValid(
        validationSystem: ValidationSystem<P, S>,
        payload: P,
        validationFailureTransformer: (S) -> TitleAndMessage,
        progressConsumer: ProgressConsumer? = null,
        autoFixPayload: (original: P, failureStatus: S) -> P = { original, _ -> original },
        block: (P) -> Unit,
    ) = requireValid(
        validationSystem = validationSystem,
        payload = payload,
        errorDisplayer = ::showError,
        validationFailureTransformerDefault = validationFailureTransformer,
        progressConsumer = progressConsumer,
        autoFixPayload = autoFixPayload,
        block = block
    )
}
