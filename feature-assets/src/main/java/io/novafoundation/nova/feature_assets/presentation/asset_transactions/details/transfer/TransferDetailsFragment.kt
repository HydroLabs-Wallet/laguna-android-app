package io.novafoundation.nova.feature_assets.presentation.asset_transactions.details.transfer

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.feature_assets.R
import io.novafoundation.nova.feature_assets.databinding.FragmentTransferDetailsBinding
import io.novafoundation.nova.feature_assets.di.AssetsFeatureApi
import io.novafoundation.nova.feature_assets.di.AssetsFeatureComponent
import io.novafoundation.nova.feature_assets.presentation.model.OperationParcelizeModel
import io.novafoundation.nova.feature_assets.presentation.model.OperationStatusAppearance
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject


class TransferDetailsFragment : BaseFragment<TransactionDetailsPresenter>(), TransferDetailsView {
    companion object {
        private const val EXTRA_PAYLOAD = "TransactionDetailsFragment.extra_payload"
        fun getNewInstance(payload: OperationParcelizeModel.Transfer): TransferDetailsFragment =
            TransferDetailsFragment().apply {
                arguments = bundleOf(EXTRA_PAYLOAD to payload)
            }
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: TransactionDetailsPresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentTransferDetailsBinding

    override fun inject() {
        FeatureUtils.getFeature<AssetsFeatureComponent>(requireContext(), AssetsFeatureApi::class.java)
            .transferDetailsComponentFactory()
            .create(this, argument(EXTRA_PAYLOAD))
            .inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransferDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setOnClickListener { presenter.onBackCommandClick() }
        binding.imCopy.setOnClickListener { presenter.onCopyClick() }
        binding.imOpenBrowser.setOnClickListener { presenter.onWebClick(this) }
        binding.imDownload.setOnClickListener { presenter.onDownloadClick() }
        binding.imCopy.setOnClickListener { presenter.onCopyClick() }

    }

    override fun setTransaction(data: OperationParcelizeModel.Transfer) {

        with(binding) {
            holderDialog.isVisible = true
            if (data.isIncome) {
                labelStatus.text = getString(R.string.received)
                holderDetails.isVisible = false
            } else {
                when (data.statusAppearance) {
                    OperationStatusAppearance.PENDING -> {
                        labelStatus.text = getString(R.string.sending)
                    }
                    OperationStatusAppearance.FAILED -> {
                        labelStatus.text = getString(R.string.failed)
                    }
                    else -> {
                        labelStatus.text = getString(R.string.sent)
                    }
                }
                holderDetails.isVisible = true

            }
            tvAmountCurrency.text = data.dollarAmount
            tvAmountToken.text = data.amount
            vFrom.setData(true, data.sender)
            vTo.setData(false, data.receiver)
            tvFeeToken.text = data.fee
            tvFeeCurrency.text = data.dollarFee
            tvTotalToken.text = data.totalAmount
            tvTotalCurrency.text = data.totalDollarAmount
            tvStatus.setStatus(data.statusAppearance)
        }
    }

    override fun copyToClipboard(text: String) {
        val clip = ClipData.newPlainText(getString(R.string.app_name), text)
        val clipboard =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(clip)
        showSuccess(getString(R.string.transaction_copied_to_clip))
    }


    override fun onBackPressed(): Boolean {
        presenter.onBackCommandClick()
        return true
    }
}
