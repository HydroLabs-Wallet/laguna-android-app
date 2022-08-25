package io.novafoundation.nova.feature_account_impl.presentation.password_confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.data.model.ConfirmPayload
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.common.utils.hideSoftKeyboard
import io.novafoundation.nova.common.utils.showSoftKeyboard
import io.novafoundation.nova.feature_account_api.di.AccountFeatureApi
import io.novafoundation.nova.feature_account_impl.R
import io.novafoundation.nova.feature_account_impl.databinding.FragmentPasswordConfirmBinding
import io.novafoundation.nova.feature_account_impl.di.AccountFeatureComponent
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class PasswordConfirmFragment : BaseFragment<PasswordConfirmPresenter>(), PasswordConfirmView {
    companion object {
        private const val EXTRA_RESULT = "PasswordConfirmFragment.extra_result"
        fun getNewInstance(data: ConfirmPayload): PasswordConfirmFragment = PasswordConfirmFragment().apply {
            arguments = bundleOf(
                EXTRA_RESULT to data
            )
        }
    }


    @Inject
    @InjectPresenter
    lateinit var presenter: PasswordConfirmPresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentPasswordConfirmBinding
    override fun inject() {
        FeatureUtils.getFeature<AccountFeatureComponent>(requireContext(), AccountFeatureApi::class.java)
            .passwordConfirmComponentFactory()
            .create(
                fragment = this, argument(EXTRA_RESULT)
            ).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPasswordConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView() {
        binding.root.setOnClickListener { presenter.onBackCommandClick() }
        binding.btnBack.setOnClickListener { presenter.onBackCommandClick() }
        binding.btnNext.setOnClickListener { presenter.onNextClick() }
        binding.tvPassword.onTextChanges
            .debounce(300L)
            .distinctUntilChanged()
            .onEach { presenter.onPasswordChanged(it) }
            .launchIn(lifecycleScope)

    }

    override fun onResume() {
        super.onResume()
        binding.tvPassword.requestFocus()
        binding.tvPassword.showSoftKeyboard()
    }

    override fun onPause() {
        super.onPause()
        binding.tvPassword.hideSoftKeyboard()
    }



    override fun enableButton(isEnabled: Boolean) {
        binding.btnNext.isEnabled = isEnabled
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackCommandClick()
        return true
    }
}
