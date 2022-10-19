package io.novafoundation.nova.feature_assets.presentation.all_transactions

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
import io.novafoundation.nova.feature_assets.databinding.FragmentAllTransactionsBinding
import io.novafoundation.nova.feature_assets.databinding.FragmentAssetTransactionsBinding
import io.novafoundation.nova.feature_assets.di.AssetsFeatureApi
import io.novafoundation.nova.feature_assets.di.AssetsFeatureComponent
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import io.novafoundation.nova.feature_assets.presentation.asset_transactions.AssetTransactionsAdapter
import io.novafoundation.nova.feature_assets.presentation.transaction.history.model.OperationMarker
import kotlinx.android.synthetic.main.fragment_asset_choose.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class AllTransactionsFragment : BaseFragment<AllTransactionsPresenter>(), AllTransactionsView {
    companion object {

        fun getNewInstance(): AllTransactionsFragment =     AllTransactionsFragment()
    }

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    @InjectPresenter
    lateinit var presenter: AllTransactionsPresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentAllTransactionsBinding
    lateinit var adapter: AssetTransactionsAdapter

    override fun inject() {
        FeatureUtils.getFeature<AssetsFeatureComponent>(requireContext(), AssetsFeatureApi::class.java)
            .allTransactionsComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllTransactionsBinding.inflate(inflater, container, false)
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

    override fun onBackPressed(): Boolean {
        presenter.onBackCommandClick()
        return true
    }
}
