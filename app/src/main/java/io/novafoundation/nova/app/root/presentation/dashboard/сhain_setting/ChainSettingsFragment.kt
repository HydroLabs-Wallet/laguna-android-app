package io.novafoundation.nova.app.root.presentation.dashboard.сhain_setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import com.github.terrakok.cicerone.Router
import io.novafoundation.nova.app.databinding.FragmentChainSettingsBinding
import io.novafoundation.nova.app.root.di.RootApi
import io.novafoundation.nova.app.root.di.RootComponent
import io.novafoundation.nova.app.root.presentation.dashboard.сhain_setting.adapter.ChainSettingsAdapter
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class ChainSettingsFragment : BaseFragment<ChainSettingsPresenter>(), ChainSettingsView {
    companion object {
        fun getNewInstance(): ChainSettingsFragment = ChainSettingsFragment()
    }

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    @InjectPresenter
    lateinit var presenter: ChainSettingsPresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentChainSettingsBinding
    lateinit var adapter: ChainSettingsAdapter

    override fun inject() {
        FeatureUtils.getFeature<RootComponent>(this, RootApi::class.java)
            .chainSettingsFragmentComponentFactory()
            .create(fragment = this)
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChainSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ChainSettingsAdapter(imageLoader)
        adapter.onCheckClick = { asset, enabled -> presenter.onCheckChanged(asset, enabled) }
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
