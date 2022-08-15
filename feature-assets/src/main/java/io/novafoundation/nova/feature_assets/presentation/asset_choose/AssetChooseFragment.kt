package io.novafoundation.nova.feature_assets.presentation.asset_choose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import com.github.terrakok.cicerone.Router
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.feature_assets.databinding.FragmentAssetChooseBinding
import io.novafoundation.nova.feature_assets.di.AssetsFeatureApi
import io.novafoundation.nova.feature_assets.di.AssetsFeatureComponent
import io.novafoundation.nova.feature_assets.presentation.asset_choose.adapter.AssetChooseAdapter
import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class AssetChooseFragment : BaseFragment<AssetChoosePresenter>(), AssetChooseView {
    companion object {
        fun getNewInstance() = AssetChooseFragment()
    }

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    @InjectPresenter
    lateinit var presenter: AssetChoosePresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentAssetChooseBinding
    lateinit var adapter: AssetChooseAdapter

    override fun inject() {
        FeatureUtils.getFeature<AssetsFeatureComponent>(requireContext(), AssetsFeatureApi::class.java)
            .assetChooseComponentFactory()
            .create(this)
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
        adapter = AssetChooseAdapter(imageLoader)
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
