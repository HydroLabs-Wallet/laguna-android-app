package io.novafoundation.nova.feature_assets.presentation.asset_transactions

import androidx.core.view.isVisible
import coil.ImageLoader
import coil.load
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import io.novafoundation.nova.common.utils.DefaultDiffUtilCallback
import io.novafoundation.nova.feature_assets.databinding.ListitemAssetTransactionBinding
import io.novafoundation.nova.feature_assets.databinding.ListitemAssetTransactionDateBinding
import io.novafoundation.nova.feature_assets.presentation.model.OperationModel
import io.novafoundation.nova.feature_assets.presentation.transaction.history.model.DayHeader
import io.novafoundation.nova.feature_assets.presentation.transaction.history.model.OperationMarker

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
                    vStatus.setStatus(item.statusAppearance, item.operation)
                    tvName.text = item.title
                    tvTokenAmount.text = item.amount
                    tvCurrencyAmount.text = item.dollarAmount
                    tvAddress.text = item.subtitle
                    tvDate.text = item.formattedTime
                    imIconBig.load(item.icon, imageLoader)
                    imNotNative.isVisible = item.notNativeIcon != null
                    if (item.notNativeIcon != null) {
                        imNotNative.load(item.notNativeIcon, imageLoader)
                    }
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
                binding.tvTitle.text = item.date
            }
        }
}
