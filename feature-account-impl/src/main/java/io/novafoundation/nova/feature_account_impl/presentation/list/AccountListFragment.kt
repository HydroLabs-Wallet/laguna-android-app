package io.novafoundation.nova.feature_account_impl.presentation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import io.novafoundation.nova.common.base.BaseFragmentOld
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.feature_account_api.di.AccountFeatureApi
import io.novafoundation.nova.feature_account_impl.R
import io.novafoundation.nova.feature_account_impl.di.AccountFeatureComponent
import io.novafoundation.nova.feature_account_impl.presentation.account.list.AccountChosenNavDirection
import io.novafoundation.nova.feature_account_impl.presentation.account.list.AccountListViewModel
import io.novafoundation.nova.feature_account_impl.presentation.model.LightMetaAccountUi
import kotlinx.android.synthetic.main.fragment_accounts.accountsList
import kotlinx.android.synthetic.main.fragment_accounts.addAccount
import kotlinx.android.synthetic.main.fragment_accounts.novaToolbar

private const val ARG_DIRECTION = "ARG_DIRECTION"

class AccountListFragment : BaseFragmentOld<AccountListViewModel>(), AccountsAdapter.AccountItemHandler {
    private lateinit var adapter: AccountsAdapter

    companion object {

        fun getBundle(accountChosenNavDirection: AccountChosenNavDirection) = Bundle().apply {
            putSerializable(ARG_DIRECTION, accountChosenNavDirection)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = layoutInflater.inflate(R.layout.fragment_accounts, container, false)

    override fun initViews() {
        adapter = AccountsAdapter(this)

        accountsList.setHasFixedSize(true)
        accountsList.adapter = adapter

        novaToolbar.setRightActionClickListener {
            viewModel.editClicked()
        }

        novaToolbar.setHomeButtonListener {
            viewModel.backClicked()
        }

        addAccount.setOnClickListener { viewModel.addAccountClicked() }
    }

    override fun inject() {
        val accountChosenNavDirection = argument<AccountChosenNavDirection>(ARG_DIRECTION)

        FeatureUtils.getFeature<AccountFeatureComponent>(
            requireContext(),
            AccountFeatureApi::class.java
        )
            .accountsComponentFactory()
            .create(this, accountChosenNavDirection)
            .inject(this)
    }

    override fun subscribe(viewModel: AccountListViewModel) {
        viewModel.accountsFlow.observe { adapter.submitList(it) }
    }

    override fun infoClicked(accountModel: LightMetaAccountUi) {
        viewModel.infoClicked(accountModel)
    }

    override fun checkClicked(accountModel: LightMetaAccountUi) {
        viewModel.selectAccountClicked(accountModel)
    }
}
