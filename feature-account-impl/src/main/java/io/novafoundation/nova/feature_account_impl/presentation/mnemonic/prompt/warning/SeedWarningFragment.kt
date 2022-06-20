package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt.warning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.feature_account_api.di.AccountFeatureApi
import io.novafoundation.nova.feature_account_impl.databinding.FragmentSeedWarningBinding
import io.novafoundation.nova.feature_account_impl.di.AccountFeatureComponent
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class SeedWarningFragment : BaseFragment(), SeedWarningView {
    companion object {
        fun getNewInstance(): SeedWarningFragment = SeedWarningFragment()
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: SeedWarningPresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentSeedWarningBinding

    override fun inject() {
        FeatureUtils.getFeature<AccountFeatureComponent>(context!!, AccountFeatureApi::class.java)
            .seedPromptWarningComponentFactory()
            .create(
                fragment = this,
            ).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSeedWarningBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setOnClickListener { presenter.onBackCommandClick() }
        binding.btnSecure.setOnClickListener { presenter.onSecureClick() }
        binding.btnSkip.setOnClickListener { presenter.onSkipClick() }
        binding.tvSubTitle.setOnClickListener { presenter.toggleCheckBox() }
        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            presenter.onAgree(isChecked)
        }
    }

    override fun toggleCheckbox() {
        binding.checkbox.toggle()
    }

    override fun enableButton(isEnabled: Boolean) {
        binding.btnSkip.isEnabled = isEnabled
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackCommandClick()
        return true
    }
}
