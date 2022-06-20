package io.novafoundation.nova.feature_account_impl.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.feature_account_api.di.AccountFeatureApi
import io.novafoundation.nova.feature_account_impl.R
import io.novafoundation.nova.feature_account_impl.databinding.FragmentLoginBinding
import io.novafoundation.nova.feature_account_impl.di.AccountFeatureComponent
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class LoginFragment : BaseFragment(), LoginView {
    companion object {
        fun getNewInstance(): LoginFragment = LoginFragment()
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: LoginPresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentLoginBinding
    override fun inject() {
        FeatureUtils.getFeature<AccountFeatureComponent>(requireContext(), AccountFeatureApi::class.java)
            .loginComponent()
            .create(
                fragment = this,
            ).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvSecret.setOnClickListener { presenter.onImportClick() }
        binding.tvSupport.setOnClickListener { presenter.onSupportClick() }
        binding.btnNext.setOnClickListener { presenter.onNextClick() }
        binding.tvPassword.onTextChanges
            .debounce(300L)
            .distinctUntilChanged()
            .onEach { presenter.onTextChanged(it) }
            .launchIn(lifecycleScope)
    }

    override fun showImportSnack() {
        Snackbar.make(requireView(), "Import Wallet", Snackbar.LENGTH_SHORT).show()
    }

    override fun showSupportSnack() {
        Snackbar.make(requireView(), "Contact support", Snackbar.LENGTH_SHORT).show()
    }

    override fun enableButton(enable: Boolean) {
        binding.btnNext.isEnabled = enable
    }

    override fun showPasswordError() {
        Snackbar.make(
            requireView(),
            getString(R.string.password_do_not_match),
            Snackbar.LENGTH_LONG
        ).show()
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackCommandClick()
        return true
    }
}
