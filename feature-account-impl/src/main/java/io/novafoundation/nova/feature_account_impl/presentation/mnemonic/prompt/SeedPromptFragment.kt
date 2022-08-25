package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.github.terrakok.cicerone.Router
import com.google.android.material.snackbar.Snackbar
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.feature_account_api.di.AccountFeatureApi
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload
import io.novafoundation.nova.feature_account_impl.databinding.FragmentSeedPromptBinding
import io.novafoundation.nova.feature_account_impl.di.AccountFeatureComponent
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class SeedPromptFragment : BaseFragment<SeedPromptPresenter>(), SeedPromptView {
    companion object {
        const val RESULT_PROMPT = "result_prompt"
        private const val EXTRA_IS_AUTH = "isAuth"

        fun getNewInstance(payload: AddAccountPayload): SeedPromptFragment = SeedPromptFragment().apply {
            arguments = bundleOf(EXTRA_IS_AUTH to payload)
        }
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: SeedPromptPresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentSeedPromptBinding

    override fun inject() {
        FeatureUtils.getFeature<AccountFeatureComponent>(requireContext(), AccountFeatureApi::class.java)
            .seedPromptComponentFactory()
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
        binding = FragmentSeedPromptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvSubTitle.setOnClickListener { presenter.onInfoClick() }
        binding.btnSkip.setOnClickListener { presenter.onSkipClick() }
        binding.progress.setOnClickListener { presenter.onBackCommandClick() }
        binding.btnCreate.setOnClickListener { presenter.onCreateClick() }
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

    enum class ResultPrompt {
        SKIP, SECURE
    }
}
