package io.novafoundation.nova.feature_assets.presentation.asset_transactions

import io.novafoundation.nova.common.utils.WithCoroutineScopeExtensions
import io.novafoundation.nova.common.utils.inBackground
import io.novafoundation.nova.feature_assets.domain.WalletInteractor
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import io.novafoundation.nova.feature_assets.presentation.model.OperationModel
import io.novafoundation.nova.feature_assets.presentation.transaction.history.mixin.TransactionHistoryMixin
import io.novafoundation.nova.feature_assets.presentation.transaction.history.mixin.TransactionHistoryUi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import java.util.*
import javax.inject.Inject

@InjectViewState
class AssetTransactionsPresenter @Inject constructor(
    private val router: WalletRouter,
    private val transactionHistoryMixin: TransactionHistoryMixin,
    private val payload: AssetPayload,
    private val interactor: WalletInteractor,
) :
    MvpPresenter<AssetTransactionsView>(),
    TransactionHistoryUi by transactionHistoryMixin, WithCoroutineScopeExtensions {

    override val coroutineScope = presenterScope


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        presenterScope.launch { transactionHistoryMixin.syncFirstOperationsPage() }
        val assetFlow = interactor.assetFlow(payload.chainId, payload.chainAssetId).inBackground()
        assetFlow
            .onEach {
                var name = it.token.configuration.priceId ?: it.token.configuration.name
                name = name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                viewState.setTitle(name)
            }
            .launchIn(presenterScope)
        state.onEach {
            when (val data = it.listState) {
                is TransactionHistoryUi.State.ListState.Data -> viewState.submitList(data.items)
                TransactionHistoryUi.State.ListState.Empty -> {}
                TransactionHistoryUi.State.ListState.EmptyProgress -> {}
            }
        }.launchIn(presenterScope)
    }


    fun onScroll(pos: Int) {
        transactionHistoryMixin.scrolled(pos)
    }

    fun onTransactionClick(data: OperationModel) {
        transactionHistoryMixin.transactionClicked(data)
    }


    fun onBackCommandClick() {
        router.back()
    }
}
