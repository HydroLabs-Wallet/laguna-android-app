package io.novafoundation.nova.app.root.presentation.dashboard.page_assets.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.app.root.presentation.dashboard.page_assets.PageAssetsFragment
import io.novafoundation.nova.common.di.scope.ScreenScope

@Subcomponent()
@ScreenScope
interface PageAssetsFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment
        ): PageAssetsFragmentComponent
    }

    fun inject(fragment: PageAssetsFragment)
}
