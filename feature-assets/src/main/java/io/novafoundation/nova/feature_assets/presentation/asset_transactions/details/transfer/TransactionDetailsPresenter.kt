package io.novafoundation.nova.feature_assets.presentation.asset_transactions.details.transfer

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Router
import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import io.novafoundation.nova.feature_assets.presentation.model.OperationParcelizeModel
import moxy.InjectViewState
import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.common.utils.ellipsis
import io.novafoundation.nova.feature_assets.R
import javax.inject.Inject

@InjectViewState
class TransactionDetailsPresenter @Inject constructor(
    private val router: WalletRouter,
    private val copyUseCase: GetTransactionCopyUseCase,
    private val saveUseCase: SaveTransactionTransferToDownloadsUseCase,
    private val resourceManager: ResourceManager,

    val operation: OperationParcelizeModel.Transfer,


    ) :
    BasePresenter<TransferDetailsView>() {

    var transaction: TransactionTransferModel? = null


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.setTransaction(operation)
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
