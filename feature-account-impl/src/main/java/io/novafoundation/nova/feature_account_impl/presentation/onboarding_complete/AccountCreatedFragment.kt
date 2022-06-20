package io.novafoundation.nova.feature_account_impl.presentation.onboarding_complete

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
import io.novafoundation.nova.feature_account_impl.databinding.FragmentAccountCreatedBinding
import io.novafoundation.nova.feature_account_impl.di.AccountFeatureComponent
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.confirm.SeedConfirmFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class AccountCreatedFragment : BaseFragment(), AccountCreatedView {
    companion object {
        const val EXTRA_IS_AUTH = "isAuth"

        fun getNewInstance(isAuth: Boolean): AccountCreatedFragment = AccountCreatedFragment().apply {
            arguments = bundleOf(EXTRA_IS_AUTH to isAuth)
        }
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: AccountCreatedPresenter

    @ProvidePresenter
    fun createPresenter() = presenter.apply {
        presenter.isAuth = requireArguments().getBoolean(EXTRA_IS_AUTH)
    }

    lateinit var binding: FragmentAccountCreatedBinding
    override fun inject() {
        FeatureUtils.getFeature<AccountFeatureComponent>(context!!, AccountFeatureApi::class.java)
            .accountCreatedComponentFactory()
            .create(
                fragment = this,
                isAuth = argument(SeedConfirmFragment.EXTRA_IS_AUTH),
            ).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountCreatedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDiscord.setOnClickListener { presenter.onDiscordClick() }
        binding.btnTwitter.setOnClickListener { presenter.onOnTwitterClick() }
        binding.btnNext.setOnClickListener { presenter.onOnNextClick() }
    }

    override fun showDiscordSnack() {
        Snackbar.make(requireView(), "Discord", Snackbar.LENGTH_SHORT).show()
    }

    override fun showTwitterSnack() {
        Snackbar.make(requireView(), "Twitter", Snackbar.LENGTH_SHORT).show()
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackCommandClick()
        return true
    }
}
