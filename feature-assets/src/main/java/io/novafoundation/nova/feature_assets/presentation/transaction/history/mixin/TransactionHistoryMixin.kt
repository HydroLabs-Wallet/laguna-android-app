package io.novafoundation.nova.feature_assets.presentation.transaction.history.mixin

import io.novafoundation.nova.feature_assets.presentation.model.OperationModel
import io.novafoundation.nova.feature_assets.presentation.transaction.history.model.OperationMarker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface TransactionHistoryUi {

    class State(
        val filtersButtonVisible: Boolean,
        val listState: ListState,
    ) {

        sealed class ListState {

            object Empty : State.ListState()

            object EmptyProgress : State.ListState()

            class Data(val items: List<OperationMarker>) : State.ListState()
        }
    }

    val state: Flow<State>

    fun transactionClicked(transactionModel: OperationModel)
}

interface TransactionHistoryMixin : TransactionHistoryUi, CoroutineScope {

    suspend fun syncFirstOperationsPage(): Result<*>

    fun scrolled(currentIndex: Int)
}
