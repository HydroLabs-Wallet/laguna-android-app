package io.novafoundation.nova.feature_account_impl.presentation.add_existing_account_complete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.google.android.material.snackbar.Snackbar
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.feature_account_api.di.AccountFeatureApi
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload
import io.novafoundation.nova.feature_account_impl.databinding.FragmentAccountCreatedBinding
import io.novafoundation.nova.feature_account_impl.databinding.FragmentAddToExistingCompletedBinding
import io.novafoundation.nova.feature_account_impl.di.AccountFeatureComponent
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

class AddToExistingAccountCompleteFragment : BaseFragment<AddToExistingAccountCompletePresenter>(), AddToExistingAccountCompleteView {
    override val isAuthorisedContent = false

    companion object {
        private const val EXTRA_IS_AUTH = "isAuth"

        fun getNewInstance(payload: AddAccountPayload): AddToExistingAccountCompleteFragment = AddToExistingAccountCompleteFragment().apply {
            arguments = bundleOf(EXTRA_IS_AUTH to payload)
        }
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: AddToExistingAccountCompletePresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentAddToExistingCompletedBinding
    override fun inject() {
        FeatureUtils.getFeature<AccountFeatureComponent>(requireContext(), AccountFeatureApi::class.java)
            .addToExistingAccountCompleteComponentFactory()
            .create(
                fragment = this,
                isAuth = argument(EXTRA_IS_AUTH),
            ).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddToExistingCompletedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNext.setOnClickListener { presenter.onOnNextClick() }
    }



    override fun onBackPressed(): Boolean {
        presenter.onBackCommandClick()
        return true
    }
}
