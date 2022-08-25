package io.novafoundation.nova.app.root.presentation.dashboard.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.app.root.presentation.dashboard.DashboardFragment
import io.novafoundation.nova.common.di.scope.ScreenScope

@Subcomponent(
    modules = [
        MainFragmentModule::class
    ]
)
@ScreenScope
interface DashboardFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment
        ): DashboardFragmentComponent
    }

    fun inject(fragment: DashboardFragment)
}
