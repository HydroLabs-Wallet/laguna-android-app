package io.novafoundation.nova.feature_account_impl.presentation.account_import.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.feature_account_api.di.AccountFeatureApi
import io.novafoundation.nova.feature_account_impl.databinding.FragmentWalletImportInfoBinding
import io.novafoundation.nova.feature_account_impl.di.AccountFeatureComponent
import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import io.novafoundation.nova.feature_account_impl.presentation.account_import.AccountImportPresenter
import javax.inject.Inject

class AccountImportInfoFragment : BaseFragment<AccountImportInfoPresenter>(), AccountImportInfoView {
    companion object {
        fun getNewInstance(): AccountImportInfoFragment = AccountImportInfoFragment()
    }

    @Inject
    lateinit var router: AccountRouter

    lateinit var binding: FragmentWalletImportInfoBinding

    override fun inject() {
        FeatureUtils.getFeature<AccountFeatureComponent>(requireContext(), AccountFeatureApi::class.java)
            .accountImportInfoComponent()
            .create(fragment = this).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalletImportInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNext.setOnClickListener { router.back() }
        binding.root.setOnClickListener { router.back() }
    }

    override fun onBackPressed(): Boolean {
        router.back()
        return true
    }
}
