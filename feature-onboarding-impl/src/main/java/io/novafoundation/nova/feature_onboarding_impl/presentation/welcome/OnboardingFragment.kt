package io.novafoundation.nova.feature_onboarding_impl.presentation.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf

import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload
import io.novafoundation.nova.feature_onboarding_api.di.OnboardingFeatureApi
import io.novafoundation.nova.feature_onboarding_impl.databinding.FragmentOnboardingStartBinding
import io.novafoundation.nova.feature_onboarding_impl.di.OnboardingFeatureComponent
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class OnboardingFragment : BaseFragment<OnboardingPresenter>(), OnboardingView {

    companion object {
        private const val EXTRA_IS_AUTH = "isAuth"
        fun getNewInstance(payload: AddAccountPayload): OnboardingFragment = OnboardingFragment().apply {
            arguments = bundleOf(EXTRA_IS_AUTH to payload)
        }

    }

    @Inject
    @InjectPresenter
    lateinit var presenter: OnboardingPresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentOnboardingStartBinding

    override fun inject() {
        FeatureUtils.getFeature<OnboardingFeatureComponent>(requireContext(), OnboardingFeatureApi::class.java)
            .welcomeComponentFactory()
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
        binding = FragmentOnboardingStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCreate.setOnClickListener { presenter.onCreateClick() }
        binding.btnImport.setOnClickListener { presenter.onImportClick() }
    }

    override fun showImportSnack() {
        Snackbar.make(requireView(), "Import Wallet", Snackbar.LENGTH_SHORT).show()
    }

    override fun showSupportSnack() {
        Snackbar.make(requireView(), "Contact support", Snackbar.LENGTH_SHORT).show()
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackCommandClick()
        return true
    }
}
