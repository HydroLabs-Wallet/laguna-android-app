package io.novafoundation.nova.feature_assets.presentation.asset_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import coil.ImageLoader
import coil.load
import com.github.terrakok.cicerone.Router
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.common.utils.backgroundTint
import io.novafoundation.nova.common.utils.compatColor
import io.novafoundation.nova.feature_assets.R
import io.novafoundation.nova.feature_assets.databinding.FragmentAssetDetailsBinding
import io.novafoundation.nova.feature_assets.di.AssetsFeatureApi
import io.novafoundation.nova.feature_assets.di.AssetsFeatureComponent
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import io.novafoundation.nova.feature_assets.presentation.asset_receive.AssetReceiveFragment
import io.novafoundation.nova.feature_assets.presentation.asset_transactions.AssetTransactionsFragment
import io.novafoundation.nova.feature_assets.presentation.balance.detail.AssetDetailsModel
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.math.BigDecimal
import javax.inject.Inject

class AssetDetailsFragment : BaseFragment(), AssetDetailsView {
    companion object {
        const val EXTRA_PAYLOAD = "AssetDetailsFragment.extra_payload"
        fun getNewInstance(payload: AssetPayload): AssetDetailsFragment =
            AssetDetailsFragment().apply {
                arguments = bundleOf(EXTRA_PAYLOAD to payload)
            }
    }

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    @InjectPresenter
    lateinit var presenter: AssetDetailsPresenter

    @ProvidePresenter
    fun createPresenter() = presenter.apply {
        val payload = requireArguments().getParcelable<AssetPayload>(EXTRA_PAYLOAD)
        requireNotNull(payload)
        presenter.setData(payload)
    }

    lateinit var binding: FragmentAssetDetailsBinding

    override fun inject() {
        FeatureUtils.getFeature<AssetsFeatureComponent>(requireContext(), AssetsFeatureApi::class.java)
            .assetDetailComponentFactory()
            .create(this, argument(AssetDetailsFragment.EXTRA_PAYLOAD))
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAssetDetailsBinding.inflate(inflater, container, false)
        val fragment = AssetTransactionsFragment.getNewInstance(presenter.payload, false)
        childFragmentManager.beginTransaction().add(R.id.containerTransaction, fragment).commit();
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener { presenter.onBackCommandClick() }
        binding.btnSeeAll.setOnClickListener { presenter.onSeeAllClick() }
    }


    override fun setAssetData(data: AssetDetailsModel) {
        val configuration = data.token.configuration
        with(binding) {
            tvTitle.text = "${configuration.name} (${configuration.symbol})"
            imIcon.load(configuration.iconUrl, imageLoader)
            val fiatSymbol = data.token.configuration.symbol
            tvTokePrice.text = data.token.dollarRate
            val delta = data.token.recentRateChange
            tvPriceChange.text =data.token.recentRateChange
            tvPriceDelta.text = data.token.recentRateChange
            tvTokenBalance.text = data.total.token
            tvCurrencyBalance.text =data.total.fiat
            tvDeltaBalance.text = delta
            tvChainName.text = "${configuration.name} ${getString(R.string.chain)}"

            btnReceive.setOnClickListener { presenter.onReceiveClick(data) }

            if (delta.startsWith("-")) {
                val redTextColor = requireContext().compatColor(R.color.red500)
                val redBackgroundColor = R.color.red100
                tvPriceDelta.setTextColor(redTextColor)
                tvDeltaBalance.setTextColor(redTextColor)
                tvPriceDelta.backgroundTint(redBackgroundColor)
                tvDeltaBalance.backgroundTint(redBackgroundColor)
                tvPriceChange.setTextColor(redTextColor)

            } else {
                val greenTextColor = requireContext().compatColor(R.color.green500)
                val greenBackgroundColor = R.color.green100
                tvPriceDelta.setTextColor(greenTextColor)
                tvDeltaBalance.setTextColor(greenTextColor)
                tvPriceDelta.backgroundTint(greenBackgroundColor)
                tvDeltaBalance.backgroundTint(greenBackgroundColor)
                tvPriceChange.setTextColor(greenTextColor)
            }
        }
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackCommandClick()
        return true
    }
}
