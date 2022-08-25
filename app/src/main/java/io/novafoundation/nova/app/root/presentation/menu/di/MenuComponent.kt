package io.novafoundation.nova.app.root.presentation.menu.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.app.root.presentation.menu.MenuNavDrawerFragment
import io.novafoundation.nova.common.di.scope.ScreenScope

@Subcomponent()
@ScreenScope
interface MenuComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment
        ): MenuComponent
    }

    fun inject(fragment: MenuNavDrawerFragment)
}
