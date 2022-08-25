package io.novafoundation.nova.feature_assets.presentation.asset_transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.feature_assets.R
import io.novafoundation.nova.feature_assets.databinding.FragmentAssetTransactionsBinding
import io.novafoundation.nova.feature_assets.di.AssetsFeatureApi
import io.novafoundation.nova.feature_assets.di.AssetsFeatureComponent
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import io.novafoundation.nova.feature_assets.presentation.asset_details.AssetDetailsFragment
import io.novafoundation.nova.feature_assets.presentation.transaction.history.model.OperationMarker
import kotlinx.android.synthetic.main.fragment_asset_choose.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class AssetTransactionsFragment : BaseFragment<AssetTransactionsPresenter>(), AssetTransactionsView {
    companion object {
        private const val EXTRA_PAYLOAD = "AssetTransactionsFragment.extra_payload"
        const val EXTRA_SHOW_CONTAINER = "AssetTransactionsFragment.extra_show_container"

        fun getNewInstance(
            payload: AssetPayload,
            showContainer: Boolean
        ): AssetTransactionsFragment =
            AssetTransactionsFragment().apply {
                arguments = bundleOf(
                    EXTRA_PAYLOAD to payload,
                    EXTRA_SHOW_CONTAINER to showContainer
                )
            }
    }

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    @InjectPresenter
    lateinit var presenter: AssetTransactionsPresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentAssetTransactionsBinding
    lateinit var adapter: AssetTransactionsAdapter

    override fun inject() {
        FeatureUtils.getFeature<AssetsFeatureComponent>(requireContext(), AssetsFeatureApi::class.java)
            .assetTransactionsComponentFactory()
            .create(this, argument(EXTRA_PAYLOAD))
            .inject(this)    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAssetTransactionsBinding.inflate(inflater, container, false)
        val showContainer = requireArguments().getBoolean(EXTRA_SHOW_CONTAINER, true)
        binding.holderHeader.isVisible = showContainer
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AssetTransactionsAdapter(imageLoader)
        adapter.onItemClickListener = { presenter.onTransactionClick(it) }
        binding.rvList.adapter = adapter
        addScrollListener()
        binding.btnBack.setOnClickListener { presenter.onBackCommandClick() }

    }

    private fun addScrollListener() {
        val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val lastVisiblePosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                presenter.onScroll(lastVisiblePosition)
            }
        }

        rvList.addOnScrollListener(scrollListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun submitList(data: List<OperationMarker>) {
        adapter.items = data
    }

    override fun setTitle(name: String) {
        binding.tvTitle.text = "$name ${getString(R.string.activity)}"
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackCommandClick()
        return true
    }
}
