package io.novafoundation.nova.feature_assets.presentation.asset_transactions

import coil.ImageLoader
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import io.novafoundation.nova.common.utils.DefaultDiffUtilCallback
import io.novafoundation.nova.feature_assets.databinding.ListitemAssetTransactionBinding
import io.novafoundation.nova.feature_assets.databinding.ListitemAssetTransactionDateBinding
import io.novafoundation.nova.feature_assets.presentation.model.OperationModel
import io.novafoundation.nova.feature_assets.presentation.model.OperationStatusAppearance
import io.novafoundation.nova.feature_assets.presentation.transaction.history.model.DayHeader
import io.novafoundation.nova.feature_assets.presentation.transaction.history.model.OperationMarker
import io.novafoundation.nova.feature_assets.presentation.view.TransactionStatusView

class AssetTransactionsAdapter(private val imageLoader: ImageLoader) :
    AsyncListDifferDelegationAdapter<OperationMarker>(DefaultDiffUtilCallback()) {

    var onItemClickListener: ((OperationModel) -> Unit)? = null

    init {
        delegatesManager.addDelegate(transactionDelegate())
        delegatesManager.addDelegate(dateDelegate())
    }

    private fun transactionDelegate() =
        adapterDelegateViewBinding<OperationModel, OperationMarker, ListitemAssetTransactionBinding>(
            { layoutInflater, root ->
                ListitemAssetTransactionBinding.inflate(layoutInflater, root, false)
            }
        ) {
            bind {
                with(binding) {
                    when (item.statusAppearance) {
                        OperationStatusAppearance.COMPLETED -> vStatus.setStatus(TransactionStatusView.TransactionStatus.APPROVED)
                        OperationStatusAppearance.PENDING -> vStatus.setStatus(TransactionStatusView.TransactionStatus.PENDING)
                        OperationStatusAppearance.FAILED -> vStatus.setStatus(TransactionStatusView.TransactionStatus.FAILED)
                    }
//                    tvName.text = item.name
                    tvTokenAmount.text = item.amount
//                    tvCurrencyAmount.text = item.currencyAmount
//                    tvAddress.text = item.address
                    tvDate.text = item.formattedTime
//                    imIconBig.load(item.icon, imageLoader)
                    root.setOnClickListener { onItemClickListener?.invoke(item) }
                }
            }
        }


    private fun dateDelegate() =
        adapterDelegateViewBinding<DayHeader, OperationMarker, ListitemAssetTransactionDateBinding>(
            { layoutInflater, root ->
                ListitemAssetTransactionDateBinding.inflate(layoutInflater, root, false)
            }
        ) {
            bind {
                binding.tvTitle.text = item.daysSinceEpoch.toString()
            }
        }
}
