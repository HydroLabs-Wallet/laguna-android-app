package io.novafoundation.nova.app.root.presentation.dashboard.page_networks.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.app.root.presentation.dashboard.page_networks.PageNetworksFragment
import io.novafoundation.nova.common.di.scope.ScreenScope

@Subcomponent()
@ScreenScope
interface PageNetworksFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment
        ): PageNetworksFragmentComponent
    }

    fun inject(fragment: PageNetworksFragment)
}
