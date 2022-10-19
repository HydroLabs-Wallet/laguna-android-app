package io.novafoundation.nova.feature_account_impl.presentation.edit_field

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.data.model.EditFieldPayload
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.common.utils.hideSoftKeyboard
import io.novafoundation.nova.common.utils.showSoftKeyboard
import io.novafoundation.nova.feature_account_api.di.AccountFeatureApi
import io.novafoundation.nova.feature_account_impl.databinding.FragmentEditFieldBinding
import io.novafoundation.nova.feature_account_impl.databinding.FragmentPasswordConfirmBinding
import io.novafoundation.nova.feature_account_impl.di.AccountFeatureComponent
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class EditFieldFragment : BaseFragment<EditFieldPresenter>(), EditFieldView {

    companion object {

        private const val EXTRA_RESULT = "EditFieldFragment.extra_result"
        fun getNewInstance(data: EditFieldPayload): EditFieldFragment = EditFieldFragment().apply {
            arguments = bundleOf(
                EXTRA_RESULT to data
            )
        }
    }


    @Inject
    @InjectPresenter
    lateinit var presenter: EditFieldPresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentEditFieldBinding

    override fun inject() {
        FeatureUtils.getFeature<AccountFeatureComponent>(this, AccountFeatureApi::class.java)
            .editFieldComponentFactory()
            .create(fragment = this, argument(EXTRA_RESULT))
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditFieldBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView(data: EditFieldPayload) {
        binding.tvTitle.text = data.title
        binding.tvPassword.setText(data.text)
        binding.tvPassword.setHint(data.hint)
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
