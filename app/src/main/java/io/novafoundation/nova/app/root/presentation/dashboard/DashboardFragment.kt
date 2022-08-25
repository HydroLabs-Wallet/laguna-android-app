package io.novafoundation.nova.app.root.presentation.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import io.novafoundation.nova.app.R
import io.novafoundation.nova.app.databinding.FragmentDashboardBinding
import io.novafoundation.nova.app.root.di.RootApi
import io.novafoundation.nova.app.root.di.RootComponent
import io.novafoundation.nova.app.root.presentation.view.DashboardBalanceView
import io.novafoundation.nova.common.base.BaseFragment
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.common.utils.DoubleClickListener
import io.novafoundation.nova.common.utils.showToast
import io.novafoundation.nova.feature_assets.presentation.balance.list.model.TotalBalanceModel
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.math.BigDecimal
import javax.inject.Inject

class DashboardFragment : BaseFragment<DashboardPresenter>(), DashboardView {
    companion object {
        fun getNewInstance(): DashboardFragment = DashboardFragment()
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: DashboardPresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    lateinit var binding: FragmentDashboardBinding
    override fun inject() {
        FeatureUtils.getFeature<RootComponent>(this, RootApi::class.java)
            .dashboardFragmentComponentFactory()
            .create(fragment = this)
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pagerAdapter = DashboardPageAdapter(this)
        binding.pager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabs, binding.pager) { tab, position ->
            val text = when (position) {
                0 -> getString(R.string.assets)
                1 -> getString(R.string.networks)
                else -> getString(R.string.error)
            }
            tab.text = text.uppercase()
        }.attach()
        binding.toolbar.setOnAvatarClickListener {
            presenter.onAvatarClicked()
        }
        binding.toolbar.setOnMenuClickListener { presenter.onMenuClick() }
        binding.btnOverflow.setOnClickListener { presenter.onChainSettingsCLick() }

        binding.holderBalance.setOnChangeVisibility(object : DoubleClickListener() {
            override fun onDoubleClick(v: View) {
                presenter.onValueVisibilityToggle()
            }
        })
        binding.bottomNavBarView.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_active -> {
                    requireActivity().showToast("Active state")
                }
                R.id.nav_default -> {
                    requireActivity().showToast("Default state")
                }
            }
            true
        }

        binding.bottomNavBarView.actionButton.setOnClickListener {
            presenter.onSendReceivePopupScreen()
        }
        binding.holderBalance.setOnReceiveClickListener { presenter.onReceiveClicked() }
    }

    override fun showImportSnack() {
        Snackbar.make(requireView(), "Import Wallet", Snackbar.LENGTH_SHORT).show()
    }

    override fun showSupportSnack() {
        Snackbar.make(requireView(), "Contact support", Snackbar.LENGTH_SHORT).show()
    }

    override fun showContent(text: String) {
    }


    override fun submitBalanceModel(data: TotalBalanceModel) {
        val balance = DashboardBalanceView.DashboardBalanceValue(
            isHidden = data.totalBalance.stripTrailingZeros() == BigDecimal.ZERO,
            hasAccounts = data.totalBalance.stripTrailingZeros() != BigDecimal.ZERO,
            balance = data.totalBalanceFiat,
            delta = data.balanceChange
        )
        binding.holderBalance.updateValue(balance)
    }

    override fun setAccountName(text: String) {
        binding.toolbar.setName(text)
    }

    override fun onBackPressed(): Boolean {
        presenter.onBackCommandClick()
        return true
    }
}
