package io.novafoundation.nova.feature_account_impl.presentation.menu.change_autolock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.common.utils.hideKeyboard
import io.novafoundation.nova.feature_account_api.di.AccountFeatureApi
import io.novafoundation.nova.feature_account_impl.databinding.FragmentChangeAutolockBinding
import io.novafoundation.nova.feature_account_impl.di.AccountFeatureComponent
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class ChangeAutoLockFragment : BaseFragment<ChangeAutoLockPresenter>(), ChangeAutoLockView {
    companion object {
        fun getNewInstance(): ChangeAutoLockFragment = ChangeAutoLockFragment()
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: ChangeAutoLockPresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentChangeAutolockBinding
    override fun inject() {
        FeatureUtils.getFeature<AccountFeatureComponent>(requireContext(), AccountFeatureApi::class.java)
            .changeAutoLockComponentFactory()
            .create(
                fragment = this,
            ).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeAutolockBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvText.onTextChanges
            .debounce(300L)
            .distinctUntilChanged()
            .onEach { presenter.onCurrentPasswordTextChange(it) }
            .launchIn(lifecycleScope)

        binding.btnBack.setOnClickListener { presenter.onBackCommandClick() }
        binding.btnCancel.setOnClickListener { presenter.onBackCommandClick() }
        binding.btnNext.setOnClickListener {
            hideKeyboard()
            presenter.onNextClick()
        }
    }

    override fun setCurrentTime(data: String) {
        binding.tvText.setText(data)
    }

    override fun enableButton(enable: Boolean) {
//        binding.btnNext.isEnabled = enable
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackCommandClick()
        return true
    }
}
