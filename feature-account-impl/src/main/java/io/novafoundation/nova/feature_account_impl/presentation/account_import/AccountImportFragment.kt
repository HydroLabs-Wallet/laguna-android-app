package io.novafoundation.nova.feature_account_impl.presentation.account_import

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.common.utils.textFlow
import io.novafoundation.nova.feature_account_api.di.AccountFeatureApi
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload
import io.novafoundation.nova.feature_account_impl.databinding.FragmentAccountImportBinding
import io.novafoundation.nova.feature_account_impl.di.AccountFeatureComponent
import io.novafoundation.nova.feature_account_impl.presentation.account_import.AccountImportFragment.ImportMode.JSON
import io.novafoundation.nova.feature_account_impl.presentation.account_import.AccountImportFragment.ImportMode.SEED
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class AccountImportFragment : BaseFragment<AccountImportPresenter>(), AccountImportView {
    override val isAuthorisedContent=false

    companion object {
        private const val EXTRA_IS_AUTH = "isAuth"

        fun getNewInstance(payload: AddAccountPayload): AccountImportFragment = AccountImportFragment().apply {
            arguments = bundleOf(EXTRA_IS_AUTH to payload)
        }
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: AccountImportPresenter

    @ProvidePresenter
     fun createPresenter() = presenter

    lateinit var binding: FragmentAccountImportBinding
    override fun inject() {
        FeatureUtils.getFeature<AccountFeatureComponent>(requireContext(), AccountFeatureApi::class.java)
            .accountImportComponent()
            .create(
                fragment = this,
                isAuth= argument(EXTRA_IS_AUTH)
            ).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountImportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progress.setOnClickListener { presenter.onBackCommandClick() }
        binding.btnNext.setOnClickListener { presenter.onNextClick() }
        binding.tvJson.setOnClickListener { getContent.launch("application/json") }
        binding.btnHelp.setOnClickListener { presenter.onHelpClick() }
        binding.tvSeed.textFlow()
            .debounce(300L)
            .distinctUntilChanged()
            .onEach { presenter.onTextChanged(it) }
            .launchIn(lifecycleScope)
        binding.tvPassword.onTextChanges
            .debounce(300L)
            .distinctUntilChanged()
            .onEach { presenter.onPasswordChanged(it) }
            .launchIn(lifecycleScope)
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackCommandClick()
        return true
    }

    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { presenter.onFileUriReceived(it) }
    }

    override fun showProgress(show: Boolean) {
        binding.btnNext.setIsProgress(show)
    }


    override fun enableButton(enable: Boolean) {
        binding.btnNext.isEnabled = enable
    }

    override fun updateMode(mode: ImportMode) {
        with(binding) {
            when (mode) {
                SEED -> {
                    tvPassword.isVisible = false
                    holderJson.isVisible = false
                    holderMnemonic.isVisible = true
                }
                JSON -> {
                    tvPassword.isVisible = true
                    holderJson.isVisible = true
                    holderMnemonic.isVisible = false
                }
            }
        }
    }

    enum class ImportMode { SEED, JSON }
}
