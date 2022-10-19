package io.novafoundation.nova.feature_account_impl.presentation.address_book_contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.data.model.ContactPayload
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.common.utils.compatColor
import io.novafoundation.nova.feature_account_api.di.AccountFeatureApi
import io.novafoundation.nova.feature_account_impl.R
import io.novafoundation.nova.feature_account_impl.databinding.FragmentAddressBookContactBinding
import io.novafoundation.nova.feature_account_impl.di.AccountFeatureComponent
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject


class AddressBookContactFragment : BaseFragment<AddressBookContactPresenter>(), AddressBookContactView {

    companion object {
        private const val EXTRA_PAYLOAD = "AddressBookContactFragment.extra_asset"
        fun getNewInstance(data: ContactPayload) = AddressBookContactFragment().apply {
            arguments = bundleOf(
                EXTRA_PAYLOAD to data
            )
        }
    }


    @Inject
    @InjectPresenter
    lateinit var presenter: AddressBookContactPresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentAddressBookContactBinding

    override fun inject() {
        FeatureUtils.getFeature<AccountFeatureComponent>(requireContext(), AccountFeatureApi::class.java)
            .addressBookContactComponentFactory()
            .create(this, argument(EXTRA_PAYLOAD))
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressBookContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener { presenter.onBackCommandClick() }
        binding.btnAction.setOnClickListener { presenter.onActionClick() }
        binding.btnNext.setOnClickListener { presenter.onNextClick() }
        binding.btnCancel.setOnClickListener { presenter.onCancelClick() }
        binding.tvName.onTextChanges
            .onEach { presenter.onNameChange(it) }
            .launchIn(lifecycleScope)
        binding.tvAddress.onTextChanges
            .onEach { presenter.onAddressChange(it) }
            .launchIn(lifecycleScope)
        binding.tvMemo.onTextChanges
            .onEach { presenter.onMemoChange(it) }
            .launchIn(lifecycleScope)
    }

    override fun setData(name: String, address: String, memo: String) {
        binding.tvName.setText(name)
        binding.tvAddress.setText(address)
        binding.tvMemo.setText(memo)

    }

    override fun showCreateState() {
        binding.btnAction.isVisible = false
        binding.btnNext.setText(R.string.save_to_contact)
        binding.btnNext.isVisible = true
        binding.imCircle.isVisible = true
        binding.imCircleContent.isVisible = true
        binding.tvToolbarTitle.text = getString(R.string.new_contact)

        binding.tvName.isEnabled = true
        binding.tvAddress.isEnabled = true
        binding.tvAddress.setIcEnd(null)
        binding.tvMemo.isEnabled = true
        binding.btnCancel.isVisible = false
        binding.tvMemo.isVisible = true
    }

    override fun showViewState(hasMemo: Boolean) {
        binding.btnAction.isVisible = true
        binding.btnAction.setTextColor(requireContext().compatColor(R.color.indigo500))

        binding.btnAction.text = getString(R.string.common_edit)
        binding.btnNext.isVisible = false
        binding.imCircle.isVisible = true
        binding.imCircleContent.isVisible = true
        binding.tvToolbarTitle.text = getString(R.string.contact)
        binding.tvName.isEnabled = false
        binding.tvAddress.isEnabled = false
        binding.tvAddress.setIcEnd(R.drawable.ic_copy)

        binding.tvMemo.isEnabled = false
        binding.btnCancel.isVisible = false
        binding.tvMemo.isVisible = hasMemo

    }

    override fun showEditState() {
        binding.btnAction.isVisible = true
        binding.btnAction.setText(R.string.delete)
        binding.btnAction.setTextColor(requireContext().compatColor(R.color.red500))
        binding.btnNext.isVisible = true
        binding.btnNext.setText(R.string.save)
        binding.imCircle.isVisible = true
        binding.imCircleContent.isVisible = true
        binding.tvToolbarTitle.text = getString(R.string.edit_contact)
        binding.tvAddress.setIcEnd(null)

        binding.tvName.isEnabled = true
        binding.tvAddress.isEnabled = true
        binding.tvMemo.isEnabled = true
        binding.btnCancel.isVisible = true
        binding.tvMemo.isVisible = true

    }

    override fun enableButton(enable: Boolean) {
        binding.btnNext.isEnabled = enable
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackCommandClick()
        return true
    }
}
