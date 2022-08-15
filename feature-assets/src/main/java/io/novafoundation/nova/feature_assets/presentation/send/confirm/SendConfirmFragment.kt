package io.novafoundation.nova.feature_assets.presentation.send.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.common.utils.formatAsCurrency
import io.novafoundation.nova.common.utils.hideSoftKeyboard
import io.novafoundation.nova.common.utils.showSoftKeyboard
import io.novafoundation.nova.common.utils.textFlow
import io.novafoundation.nova.feature_assets.R
import io.novafoundation.nova.feature_assets.databinding.FragmentSendConfirmBinding
import io.novafoundation.nova.feature_assets.di.AssetsFeatureApi
import io.novafoundation.nova.feature_assets.di.AssetsFeatureComponent
import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import io.novafoundation.nova.feature_assets.presentation.send.TransferDraft
import io.novafoundation.nova.feature_wallet_api.presentation.formatters.formatTokenAmount
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class SendConfirmFragment : BaseFragment<SendConfirmPresenter>(), SendConfirmView {
    companion object {
        private const val EXTRA_PAYLOAD = "SendConfirmFragment.extra_asset"
        fun getNewInstance(data: TransferDraft) = SendConfirmFragment().apply {
            arguments = bundleOf(
                EXTRA_PAYLOAD to data
            )
        }
    }

    @Inject
    lateinit var imageLoader: ImageLoader


    @Inject
    @InjectPresenter
    lateinit var presenter: SendConfirmPresenter
    @ProvidePresenter
    fun createPresenter() = presenter

    override fun inject() {
        FeatureUtils.getFeature<AssetsFeatureComponent>(requireContext(), AssetsFeatureApi::class.java)
            .sendConfirmComponentFactory()
            .create(this, argument(EXTRA_PAYLOAD))
            .inject(this)
    }


    lateinit var binding: FragmentSendConfirmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSendConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener { presenter.onBackCommandClick() }
        binding.labelNote.setOnClickListener { presenter.onAddNoteClick() }
        binding.tvAddNote.textFlow()
            .debounce(300L)
            .distinctUntilChanged()
            .onEach { presenter.onTextChanged(it) }
            .launchIn(lifecycleScope)
        binding.btnNext.setOnClickListener { presenter.onNextClicked() }
    }

    override fun setReceiver(data: TransferDraft, assetModel: AssetModel, account: String) {
        with(binding) {
            tvTokenAmount.text = data.amount.formatTokenAmount(assetModel.token.configuration)
            val rate = assetModel.token.dollarRate
            tvCurrencyAmount.text = data.amount.multiply(rate).formatAsCurrency()
            tvChainName.text = getString(R.string.s_chain, assetModel.chain.name)
            tvTo.text = data.contact.name
            tvFrom.text = account
            tvDotFee.text = data.fee.formatTokenAmount(assetModel.token.configuration)
            tvCurrencyFee.text = data.fee.multiply(rate).formatAsCurrency()

            tvDotTotal.text = data.getTotal().formatTokenAmount(assetModel.token.configuration)
            tvCurrencyTotal.text = data.getTotal().multiply(rate).formatAsCurrency()
            val rateCurrency = rate.formatAsCurrency()
            tvRate.text = "1 ${assetModel.token.configuration.symbol} ≈ $rateCurrency"
        }
    }

    override fun startEditMode(edit: Boolean) {
        with(binding) {
            holderDetails.isVisible = !edit
            holderExtra.isVisible = !edit
            tvAddNote.isVisible = edit

            if (edit) {
                btnNext.setText(R.string.add_note)
                tvAddNote.requestFocus()
                tvAddNote.showSoftKeyboard()
            } else {
                tvAddNote.hideSoftKeyboard()
                btnNext.setText(R.string.send)

            }
        }
    }

    override fun setNote(data: String) {
        binding.labelNote.text = data
    }

    override fun showLoader(show: Boolean) {
        binding.labelNote.isClickable = !show
        binding.btnNext.setIsProgress(show)
    }

    override fun enableButton(enabled: Boolean) {

        binding.btnNext.isEnabled = enabled
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackCommandClick()
        return true
    }
}
