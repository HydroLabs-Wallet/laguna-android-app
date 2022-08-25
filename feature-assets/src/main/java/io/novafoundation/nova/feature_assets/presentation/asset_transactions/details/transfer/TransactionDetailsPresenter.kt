package io.novafoundation.nova.feature_assets.presentation.asset_transactions.details.transfer

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.common.utils.inBackground
import io.novafoundation.nova.feature_assets.R
import io.novafoundation.nova.feature_assets.domain.WalletInteractor
import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import io.novafoundation.nova.feature_assets.presentation.model.OperationParcelizeModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.InjectViewState
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class TransactionDetailsPresenter @Inject constructor(
    private val router: WalletRouter,
    private val interactor: WalletInteractor,
    private val copyUseCase: GetTransactionCopyUseCase,
    private val saveUseCase: SaveTransactionTransferToDownloadsUseCase,
    private val resourceManager: ResourceManager,

    val operation: OperationParcelizeModel.Transfer,


    ) :
    BasePresenter<TransferDetailsView>() {

    var transaction: TransactionTransferModel? = null
    private val showValuesFlow = interactor.assetValueVisibleFlow()
        .inBackground()
        .share()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        showValuesFlow.onEach {
            viewState.setTransaction(operation, it)

        }.launchIn(presenterScope)
    }

    fun onCopyClick() {
        transaction?.let {
            val content = copyUseCase.invoke(it)
            viewState.copyToClipboard(content)
        }
    }


    fun onDownloadClick() {
        transaction?.let {
            val content = copyUseCase.invoke(it)
            saveUseCase.invoke(content, it.hash!!)
            val message = resourceManager.getString(R.string.saved_to_csv, it.hash)
            viewState.showSuccess(message)
        }
    }

    fun onWebClick(fragment: Fragment) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(operation.address))
        fragment.startActivity(browserIntent)
    }

    fun onBackCommandClick() {
        router.back()
    }
}
