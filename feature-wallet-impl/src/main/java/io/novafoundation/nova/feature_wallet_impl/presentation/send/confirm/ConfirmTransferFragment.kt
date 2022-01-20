package io.novafoundation.nova.feature_wallet_impl.presentation.send.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import coil.ImageLoader
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.common.mixin.impl.observeValidations
import io.novafoundation.nova.common.utils.applyStatusBarInsets
import io.novafoundation.nova.common.view.setAddressModel
import io.novafoundation.nova.feature_account_api.presenatation.actions.setupExternalActions
import io.novafoundation.nova.feature_wallet_api.di.WalletFeatureApi
import io.novafoundation.nova.feature_wallet_api.presentation.mixin.amountChooser.setupAmountChooser
import io.novafoundation.nova.feature_wallet_api.presentation.mixin.fee.setupFeeLoading
import io.novafoundation.nova.feature_wallet_impl.R
import io.novafoundation.nova.feature_wallet_impl.di.WalletFeatureComponent
import io.novafoundation.nova.feature_wallet_impl.presentation.send.TransferDraft
import kotlinx.android.synthetic.main.fragment_confirm_transfer.*
import javax.inject.Inject

private const val KEY_DRAFT = "KEY_DRAFT"

class ConfirmTransferFragment : BaseFragment<ConfirmTransferViewModel>() {

    companion object {

        fun getBundle(transferDraft: TransferDraft) = Bundle().apply {
            putParcelable(KEY_DRAFT, transferDraft)
        }
    }

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = layoutInflater.inflate(R.layout.fragment_confirm_transfer, container, false)

    override fun initViews() {
        confirmTransferContainer.applyStatusBarInsets()

        confirmTransferFrom.setWholeClickListener { viewModel.senderAddressClicked() }
        confirmTransferTo.setWholeClickListener { viewModel.recipientAddressClicked() }

        confirmTransferToolbar.setHomeButtonListener { viewModel.backClicked() }

        confirmTransferConfirm.submit.setOnClickListener { viewModel.submitClicked() }
        confirmTransferConfirm.submit.prepareForProgress(viewLifecycleOwner)
    }

    override fun inject() {
        val transferDraft = argument<TransferDraft>(KEY_DRAFT)

        FeatureUtils.getFeature<WalletFeatureComponent>(
            requireContext(),
            WalletFeatureApi::class.java
        )
            .confirmTransferComponentFactory()
            .create(this, transferDraft)
            .inject(this)
    }

    override fun subscribe(viewModel: ConfirmTransferViewModel) {
        setupExternalActions(viewModel)
        observeValidations(viewModel)
        setupAmountChooser(viewModel, confirmTransferAmount)
        setupFeeLoading(viewModel, confirmTransferConfirm.fee)

        viewModel.recipientModel.observe(confirmTransferTo::setAddressModel)
        viewModel.senderModel.observe(confirmTransferFrom::setAddressModel)

        viewModel.sendButtonStateLiveData.observe(confirmTransferConfirm.submit::setState)
    }
}
