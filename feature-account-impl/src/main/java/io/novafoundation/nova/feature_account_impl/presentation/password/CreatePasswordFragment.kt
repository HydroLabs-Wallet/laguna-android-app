package io.novafoundation.nova.feature_account_impl.presentation.password

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.common.view.InputFieldView
import io.novafoundation.nova.feature_account_api.di.AccountFeatureApi
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload
import io.novafoundation.nova.feature_account_impl.databinding.FragmentCreatePasswordBinding
import io.novafoundation.nova.feature_account_impl.di.AccountFeatureComponent
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class CreatePasswordFragment : BaseFragment<CreatePasswordPresenter>(), CreatePasswordView {
    companion object {
        private const val EXTRA_IS_AUTH = "isAuth"
        fun getNewInstance(data: AddAccountPayload): CreatePasswordFragment = CreatePasswordFragment().apply {
            arguments = bundleOf(
                EXTRA_IS_AUTH to data
            )
        }
    }


    @Inject
    @InjectPresenter
    lateinit var presenter: CreatePasswordPresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentCreatePasswordBinding
    override fun inject() {
        FeatureUtils.getFeature<AccountFeatureComponent>(requireContext(), AccountFeatureApi::class.java)
            .createPasswordComponentFactory()
            .create(
                fragment = this,
                argument(EXTRA_IS_AUTH)
            ).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView() {
        binding.btnNext.setOnClickListener { presenter.onNextClick() }
        try {
            binding.tvPassword.onTextChanges
                .debounce(300L)
                .distinctUntilChanged()
                .onEach { presenter.onPasswordChanged(it) }
                .launchIn(lifecycleScope)
        } catch (e: Exception) {
            Log.e("mcheck", "${e.toString()}")
        }
        binding.tvConfirm.onTextChanges
            .debounce(300L)
            .distinctUntilChanged()
            .onEach { presenter.onPasswordConfirmChanged(it) }
            .launchIn(lifecycleScope)
        binding.progress.setOnClickListener { presenter.onBackCommandClick() }
    }

    override fun updateConfirmPasswordState(data: InputFieldView.InputFieldData) {
        binding.tvConfirm.updateState(data)
    }

    override fun updatePasswordState(data: InputFieldView.InputFieldData) {
        binding.tvPassword.updateState(data)
    }

    override fun enableButton(isEnabled: Boolean) {
        binding.btnNext.isEnabled = isEnabled
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackCommandClick()
        return true
    }
}
