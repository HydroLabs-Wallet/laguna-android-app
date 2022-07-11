package io.novafoundation.nova.feature_assets.presentation.asset_receive_chooser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.feature_assets.databinding.FragmentAssetChooseBinding
import io.novafoundation.nova.feature_assets.di.AssetsFeatureApi
import io.novafoundation.nova.feature_assets.di.AssetsFeatureComponent
import io.novafoundation.nova.feature_assets.presentation.asset_choose.AssetChooseFragment
import io.novafoundation.nova.feature_assets.presentation.asset_receive.AssetReceiveFragment
import io.novafoundation.nova.feature_assets.presentation.asset_receive_chooser.adapter.AssetReceiveChooserAdapter
import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class AssetReceiveChooserFragment : BaseFragment(), AssetReceiveChooserView {
    companion object {
        const val EXTRA_PAYLOAD = "AssetReceiveChooserFragment.extra_asset"
        const val RESULT = "AssetReceiveChooserFragment.Result"

        fun getNewInstance(data: AssetReceivePayload) = AssetReceiveChooserFragment().apply {
            arguments = bundleOf(EXTRA_PAYLOAD to data)
        }
    }

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    @InjectPresenter
    lateinit var presenter: AssetReceiveChooserPresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentAssetChooseBinding
    lateinit var adapter: AssetReceiveChooserAdapter

    override fun inject() {
        FeatureUtils.getFeature<AssetsFeatureComponent>(requireContext(), AssetsFeatureApi::class.java)
            .assetReceiveChooserComponentFactory()
            .create(this, argument(EXTRA_PAYLOAD))
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAssetChooseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AssetReceiveChooserAdapter(imageLoader)
        adapter.onItemClick = { asset -> presenter.onAssetClicked(asset) }
        binding.tvSearch.onTextChanges
            .debounce(300L)
            .distinctUntilChanged()
            .onEach { presenter.onSearchChanged(it) }
            .launchIn(lifecycleScope)
        binding.rvList.adapter = adapter
        binding.btnBack.setOnClickListener { presenter.onBackCommandClick() }

    }

    override fun submitList(data: List<AssetModel>) {
        adapter.items = data
    }

    override fun onBackPressed(): Boolean {
        return false
    }
}
