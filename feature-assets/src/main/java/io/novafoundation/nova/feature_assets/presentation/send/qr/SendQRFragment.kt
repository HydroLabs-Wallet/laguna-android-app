package io.novafoundation.nova.feature_assets.presentation.send.qr

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
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import io.novafoundation.nova.feature_assets.presentation.send.ContactUi
import io.novafoundation.nova.feature_wallet_api.presentation.formatters.formatTokenAmount
import kotlinx.android.synthetic.main.fragment_send_fill.*
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class SendQRFragment : BaseFragment<SendQRPresenter>(), SendQRView {
    companion object {
        private const val EXTRA_PAYLOAD = "SendQRFragment.extra_asset"
        fun getNewInstance(data: AssetPayload) = SendQRFragment().apply {
            arguments = bundleOf(
                EXTRA_PAYLOAD to data
            )
        }
    }

    @Inject
    lateinit var imageLoader: ImageLoader


    @Inject
    @InjectPresenter
    lateinit var presenter: SendQRPresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    override fun inject() {
        FeatureUtils.getFeature<AssetsFeatureComponent>(requireContext(), AssetsFeatureApi::class.java)
            .sendQRComponentFactory()
            .create(this, argument(EXTRA_PAYLOAD))
            .inject(this)
    }

    //
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
//        binding.btnBack.setOnClickListener { presenter.onBackCommandClick() }
//        binding.btnSwap.setOnClickListener { presenter.onSwapClicked() }
//        binding.tvMax.setOnClickListener { presenter.onMaxClicked() }
//        binding.tvAmount.textFlow()
//            .debounce(300L)
//            .distinctUntilChanged()
//            .onEach { presenter.onTextChanged(it) }
//            .launchIn(lifecycleScope)
//        binding.btnNext.setOnClickListener { presenter.onNextClicked() }
    }


    override fun onBackPressed(): Boolean {
        presenter.onBackCommandClick()
        return true
    }
}
