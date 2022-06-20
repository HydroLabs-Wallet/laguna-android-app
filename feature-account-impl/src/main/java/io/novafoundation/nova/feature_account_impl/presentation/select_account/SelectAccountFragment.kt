package io.novafoundation.nova.feature_account_impl.presentation.select_account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.ImageLoader
import com.github.terrakok.cicerone.Router
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.feature_account_api.di.AccountFeatureApi
import io.novafoundation.nova.feature_account_impl.databinding.FragmentSelectAccountBinding
import io.novafoundation.nova.feature_account_impl.di.AccountFeatureComponent
import io.novafoundation.nova.feature_account_impl.presentation.model.LightMetaAccountUi
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class SelectAccountFragment : BaseFragment(), SelectAccountView {
    companion object {
        fun getNewInstance(): SelectAccountFragment = SelectAccountFragment()
    }

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    @InjectPresenter
    lateinit var presenter: SelectAccountPresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentSelectAccountBinding
    lateinit var adapter: AccountsAdapter


    override fun inject() {
        FeatureUtils.getFeature<AccountFeatureComponent>(
            requireContext(),
            AccountFeatureApi::class.java
        )
            .selectAccountComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AccountsAdapter()
        binding.rvList.adapter = adapter
        binding.root.setOnClickListener { presenter.onBackCommandClick() }
        binding.imBack.setOnClickListener { presenter.onBackCommandClick() }
        adapter.onItemClick = { presenter.onItemClicked(it) }
        binding.imAdd.setOnClickListener { presenter.onAddClick() }
    }

    override fun submitList(data: List<LightMetaAccountUi>) {
        adapter.items = data
    }

    override fun onBackPressed(): Boolean {
        return false
    }
}
