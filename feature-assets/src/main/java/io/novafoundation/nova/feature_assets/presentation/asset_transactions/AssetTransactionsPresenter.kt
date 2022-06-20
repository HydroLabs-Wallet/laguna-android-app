package io.novafoundation.nova.feature_assets.presentation.asset_transactions

import io.novafoundation.nova.common.utils.WithCoroutineScopeExtensions
import io.novafoundation.nova.feature_assets.domain.WalletInteractor
import io.novafoundation.nova.feature_assets.domain.send.SendInteractor
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import io.novafoundation.nova.feature_assets.presentation.balance.assetActions.buy.BuyMixinFactory
import io.novafoundation.nova.feature_assets.presentation.model.OperationModel
import io.novafoundation.nova.feature_assets.presentation.transaction.history.mixin.TransactionHistoryMixin
import io.novafoundation.nova.feature_assets.presentation.transaction.history.mixin.TransactionHistoryUi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class AssetTransactionsPresenter @Inject constructor(
    private val router: WalletRouter,
    private val transactionHistoryMixin: TransactionHistoryMixin,
) :
    MvpPresenter<AssetTransactionsView>(),
    TransactionHistoryUi by transactionHistoryMixin, WithCoroutineScopeExtensions {

    override val coroutineScope = presenterScope

    lateinit var payload: AssetPayload
    fun setData(payload: AssetPayload) {
        this.payload = payload
    }


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        presenterScope.launch { transactionHistoryMixin.syncFirstOperationsPage() }
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
