package io.novafoundation.nova.feature_assets.presentation.send.asset_choose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.feature_assets.databinding.FragmentAssetChooseBinding
import io.novafoundation.nova.feature_assets.databinding.FragmentSendAssetChooseBinding
import io.novafoundation.nova.feature_assets.di.AssetsFeatureApi
import io.novafoundation.nova.feature_assets.di.AssetsFeatureComponent
import io.novafoundation.nova.feature_assets.presentation.asset_choose.AssetChooseFragment
import io.novafoundation.nova.feature_assets.presentation.asset_choose.AssetChoosePresenter
import io.novafoundation.nova.feature_assets.presentation.asset_choose.adapter.AssetChooseAdapter
import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import io.novafoundation.nova.feature_assets.presentation.send.asset_choose.adapter.SendAssetChooseAdapter
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class SendAssetChooseFragment : BaseFragment<SendAssetChoosePresenter>(), SendAssetChooseView {
    companion object {
        fun getNewInstance() = SendAssetChooseFragment()
    }

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    @InjectPresenter
    lateinit var presenter: SendAssetChoosePresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentSendAssetChooseBinding
    lateinit var adapter: SendAssetChooseAdapter

    override fun inject() {
        FeatureUtils.getFeature<AssetsFeatureComponent>(requireContext(), AssetsFeatureApi::class.java)
            .sendAssetChooseComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSendAssetChooseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = SendAssetChooseAdapter(imageLoader)
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
