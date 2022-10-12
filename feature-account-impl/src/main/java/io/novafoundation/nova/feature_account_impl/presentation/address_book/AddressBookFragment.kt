package io.novafoundation.nova.feature_account_impl.presentation.address_book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.data.model.ContactUiMarker
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.feature_account_api.di.AccountFeatureApi
import io.novafoundation.nova.feature_account_impl.databinding.FragmentAddressBookBinding
import io.novafoundation.nova.feature_account_impl.di.AccountFeatureComponent
import io.novafoundation.nova.feature_account_impl.presentation.address_book.adapter.AddressBookAdapter
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject


class AddressBookFragment : BaseFragment<AddressBookPresenter>(), AddressBookView {

    companion object {
        fun getNewInstance() = AddressBookFragment()
    }


    @Inject
    @InjectPresenter
    lateinit var presenter: AddressBookPresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentAddressBookBinding
    lateinit var adapter: AddressBookAdapter

    override fun inject() {
        FeatureUtils.getFeature<AccountFeatureComponent>(requireContext(), AccountFeatureApi::class.java)
            .addressBookComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AddressBookAdapter()
        binding.rvList.adapter = adapter
        adapter.onItemClick = { asset -> presenter.onItemClicked(asset) }
        binding.btnAddBottom.setOnClickListener { presenter.onAddClick() }
        binding.btnAdd.setOnClickListener { presenter.onAddClick() }
        binding.btnBack.setOnClickListener { presenter.onBackCommandClick() }
    }

    override fun submitList(data: List<ContactUiMarker>) {
        adapter.items = data
        with(binding) {
            btnAdd.isVisible = data.isNotEmpty()
            rvList.isVisible = data.isNotEmpty()
            lbFilter.isVisible = data.isNotEmpty()
            tvFilter.isVisible = data.isNotEmpty()
            imFilter.isVisible = data.isNotEmpty()
            imCircle.isVisible = data.isEmpty()
            imCircleContent.isVisible = data.isEmpty()
            tvEmpty.isVisible = data.isEmpty()
            btnAddBottom.isVisible = data.isEmpty()
        }
    }


    override fun onBackPressed(): Boolean {
        presenter.onBackCommandClick()
        return true
    }
}
