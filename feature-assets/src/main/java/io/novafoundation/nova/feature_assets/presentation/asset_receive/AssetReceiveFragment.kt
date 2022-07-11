package io.novafoundation.nova.feature_assets.presentation.asset_receive

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import coil.ImageLoader
import coil.load
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.common.utils.*
import io.novafoundation.nova.feature_assets.R
import io.novafoundation.nova.feature_assets.databinding.FragmentAssetReceiveBinding
import io.novafoundation.nova.feature_assets.di.AssetsFeatureApi
import io.novafoundation.nova.feature_assets.di.AssetsFeatureComponent
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import io.novafoundation.nova.feature_assets.presentation.receive.model.TokenReceiver
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class AssetReceiveFragment : BaseFragment(), AssetReceiveView {
    companion object {
        const val EXTRA_PAYLOAD = "AssetReceiveFragment.extra_asset"
        fun getNewInstance(assetReceive: AssetPayload) = AssetReceiveFragment().apply {
            arguments = bundleOf(
                EXTRA_PAYLOAD to assetReceive
            )
        }
    }

    @Inject
    lateinit var imageLoader: ImageLoader


    @Inject
    @InjectPresenter
    lateinit var presenter: AssetReceivePresenter

    lateinit var adapter: AssetReceiveAdapter
    override fun inject() {
        FeatureUtils.getFeature<AssetsFeatureComponent>(requireContext(), AssetsFeatureApi::class.java)
            .assetReceiveComponentFactory()
            .create(this, argument(EXTRA_PAYLOAD))
            .inject(this)
    }

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentAssetReceiveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAssetReceiveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateUI()

        binding.viewQrAddress.btnCopy.setOnClickListener {
            requireContext().copyTextToClipboard(presenter.currentAsset?.addressModel?.address ?: "")
        }

        binding.btnShare.setOnClickListener {
            shareQR(it)
        }

        binding.btnBack.setOnClickListener { presenter.onBackCommandClick() }
        binding.vAsset.setOnClickListener { presenter.onAssetClick() }
    }

    private fun generateQr(value: String?) {
        val qrgEncoder = QRGEncoder(value, null, QRGContents.Type.TEXT, 500)
        try {
            val bitmap = qrgEncoder.bitmap
            binding.qrImage.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onAssetChanged(data: TokenReceiver) {
        generateQr(data.addressModel.address)
        binding.viewQrAddress.tvTitle.text = data.addressModel.address
        binding.tvQrHint.text = getString(R.string.asset_qr_hint, data.chain.name)
        data.asset?.let {
            binding.imIconBig.load(it.iconUrl, imageLoader)
            binding.tvName.text = it.symbol
            val isNative = it.priceId == null || it.priceId == it.name.lowercase()
            binding.imNotNative.isVisible = !isNative
            binding.tvToolbarTitle.text = getString(R.string.receive_toolbar, it.priceId.orEmpty().ifEmpty { it.name.lowercase() })

            if (!isNative) {
                binding.imNotNative.load(data.chain.icon, imageLoader)
            }
        }
    }


    private fun updateUI() {

    }

    private fun shareQR(view: View) {
        with(requireContext()) {
            val uri = getUriForFile(
                newImageFile(
                    "laguna_qr_${System.currentTimeMillis()}",
                    Bitmap.CompressFormat.PNG
                )
            ) ?: return
            storeBitmap(view.capture(), uri)
            sendFileIntent(
                "image/*",
                "Laguna Qr code",
                uri,
                "Share QR",
                "QR text message here"
            )
        }
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackCommandClick()
        return true
    }
}
