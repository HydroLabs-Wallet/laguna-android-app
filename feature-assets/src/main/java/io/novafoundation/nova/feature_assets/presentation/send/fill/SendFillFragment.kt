package io.novafoundation.nova.feature_assets.presentation.send.fill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.load
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.common.utils.textFlow
import io.novafoundation.nova.feature_assets.databinding.FragmentSendFillBinding
import io.novafoundation.nova.feature_assets.di.AssetsFeatureApi
import io.novafoundation.nova.feature_assets.di.AssetsFeatureComponent
import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import io.novafoundation.nova.feature_assets.presentation.send.ContactUi
import io.novafoundation.nova.feature_assets.presentation.send.TransferDraft
import io.novafoundation.nova.feature_wallet_api.presentation.formatters.formatTokenAmount
import kotlinx.android.synthetic.main.fragment_send_fill.*
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class SendFillFragment : BaseFragment(), SendFillView {
    companion object {
        const val EXTRA_PAYLOAD = "SendFillFragment.extra_asset"
        fun getNewInstance(data: TransferDraft) = SendFillFragment().apply {
            arguments = bundleOf(
                EXTRA_PAYLOAD to data
            )
        }
    }

    @Inject
    lateinit var imageLoader: ImageLoader


    @Inject
    @InjectPresenter
    lateinit var presenter: SendFillPresenter

    override fun inject() {
        FeatureUtils.getFeature<AssetsFeatureComponent>(requireContext(), AssetsFeatureApi::class.java)
            .sendFillComponentFactory()
            .create(this, argument(EXTRA_PAYLOAD))
            .inject(this)
    }

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentSendFillBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSendFillBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener { presenter.onBackCommandClick() }
        binding.btnSwap.setOnClickListener { presenter.onSwapClicked() }
        binding.tvMax.setOnClickListener { presenter.onMaxClicked() }
        binding.tvAmount.textFlow()
            .debounce(300L)
            .distinctUntilChanged()
            .onEach { presenter.onTextChanged(it) }
            .launchIn(lifecycleScope)
        binding.btnNext.setOnClickListener { presenter.onNextClicked() }
    }

    override fun setReceiver(data: ContactUi) {
        binding.tvReceiver.setText(data.name)
    }

    override fun setSource(data: AssetModel) {
        with(binding) {
            imAsset.load(data.token.configuration.iconUrl, imageLoader)
            imNotNative.isVisible = !data.isNative()
            if (!data.isNative()) {
                imNotNative.load(data.chain.icon, imageLoader)
            }
            tvAsset.text = data.token.configuration.assetName
            tvAssetAvailable.text = data.available.formatTokenAmount(data.token.configuration)
        }
    }

    override fun setFee(currency: String, token: String) {
        binding.tvCurrencyFee.text = currency
        binding.tvDotFee.text = token
    }

    override fun setValueTopTransfer(data: String?) {
        binding.tvAmount.setText(data)
    }

    override fun setValueBottomTransfer(data: String?) {
        binding.tvCurrentAmount.text = data
    }

    override fun showError(data: String) {
        Toast.makeText(requireContext(), data, Toast.LENGTH_LONG).show()
    }

    override fun showLoader(show: Boolean) {
        tvAmount.isEnabled = !show
        binding.btnNext.setIsProgress(show)
    }

    override fun setAmountName(data: String) {
        binding.amountType.text = data
    }

    override fun enableButton(enabled: Boolean) {
        binding.btnNext.isEnabled = enabled
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackCommandClick()
        return true
    }
}
