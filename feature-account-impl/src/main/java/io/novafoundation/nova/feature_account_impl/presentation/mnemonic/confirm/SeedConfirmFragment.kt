package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.google.android.material.snackbar.Snackbar
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.feature_account_api.di.AccountFeatureApi
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload
import io.novafoundation.nova.feature_account_api.presenatation.account.add.SeedWord
import io.novafoundation.nova.feature_account_impl.databinding.FragmentSeedConfirmBinding
import io.novafoundation.nova.feature_account_impl.di.AccountFeatureComponent
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class SeedConfirmFragment : BaseFragment<SeedConfirmPresenter>(), SeedConfirmView {
    companion object {
        private const val EXTRA_IS_AUTH = "isAuth"

        fun getNewInstance(payload: AddAccountPayload): SeedConfirmFragment =
            SeedConfirmFragment().apply {
                arguments = bundleOf(
                    EXTRA_IS_AUTH to payload
                )
            }
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: SeedConfirmPresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentSeedConfirmBinding
    override fun inject() {
        FeatureUtils.getFeature<AccountFeatureComponent>(requireContext(), AccountFeatureApi::class.java)
            .seedConfirmComponentFactory()
            .create(
                fragment = this,
                isAuth = argument(EXTRA_IS_AUTH)
            ).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSeedConfirmBinding.inflate(inflater, container, false)
        binding.seedView.showNumber = false

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.seedView.onItemClick = { presenter.onSeedListItemClick(it) }
        binding.seedConfirm.setOnSelectionChangedListener { presenter.onSelectionChanged(it) }
        binding.btnNext.setOnClickListener { presenter.onNextClick() }
        binding.progress.setOnClickListener { presenter.onBackCommandClick() }
    }

    override fun setList(data: List<SeedWord>) {
        binding.seedView.submitList(data)
    }

    override fun setSelection(data: List<SeedWord>) {
        binding.seedConfirm.setData(data)
    }

    override fun enableButton(isEnabled: Boolean) {
        binding.btnNext.isEnabled = isEnabled
    }


    override fun onBackPressed(): Boolean {
        presenter.onBackCommandClick()
        return true
    }
}
