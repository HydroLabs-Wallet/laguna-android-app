package io.novafoundation.nova.app.root.presentation.dashboard.page_assets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.ImageLoader
import io.novafoundation.nova.app.databinding.FragmentDashbordAssetsBinding
import io.novafoundation.nova.app.root.di.RootApi
import io.novafoundation.nova.app.root.di.RootComponent
import io.novafoundation.nova.app.root.presentation.dashboard.page_assets.adapter.AssetAdapter
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class PageAssetsFragment : BaseFragment<PageAssetsPresenter>(), PageAssetsView {
    companion object {
        fun getNewInstance(): PageAssetsFragment = PageAssetsFragment()
    }

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    @InjectPresenter
    lateinit var presenter: PageAssetsPresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentDashbordAssetsBinding
    lateinit var adapter: AssetAdapter
    override fun inject() {
        FeatureUtils.getFeature<RootComponent>(this, RootApi::class.java)
            .pageAssetsFragmentComponentFactory()
            .create(fragment = this)
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashbordAssetsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AssetAdapter(imageLoader)
        adapter.onItemClick = { presenter.onAssetClick(it) }
        binding.rvList.adapter = adapter

    }

    override fun submitList(data: List<AssetModel>) {
        adapter.items = data
    }

    override fun onBackPressed(): Boolean {
        return false
    }
}
