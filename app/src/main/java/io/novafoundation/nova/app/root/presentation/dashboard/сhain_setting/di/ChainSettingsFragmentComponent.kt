package io.novafoundation.nova.app.root.presentation.dashboard.сhain_setting.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.app.root.presentation.dashboard.di.MainFragmentModule
import io.novafoundation.nova.app.root.presentation.dashboard.page_networks.PageNetworksFragment
import io.novafoundation.nova.app.root.presentation.dashboard.сhain_setting.ChainSettingsFragment
import io.novafoundation.nova.common.di.scope.ScreenScope

@Subcomponent()
@ScreenScope
interface ChainSettingsFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment
        ): ChainSettingsFragmentComponent
    }

    fun inject(fragment: ChainSettingsFragment)
}
