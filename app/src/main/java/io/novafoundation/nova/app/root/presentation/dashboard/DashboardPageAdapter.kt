package io.novafoundation.nova.app.root.presentation.dashboard

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import io.novafoundation.nova.app.root.presentation.dashboard.page_assets.PageAssetsFragment
import io.novafoundation.nova.app.root.presentation.dashboard.page_networks.PageNetworksFragment

class DashboardPageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PageAssetsFragment.getNewInstance()
            1 -> PageNetworksFragment.getNewInstance()
            else -> PageAssetsFragment.getNewInstance()
        }
    }
}
