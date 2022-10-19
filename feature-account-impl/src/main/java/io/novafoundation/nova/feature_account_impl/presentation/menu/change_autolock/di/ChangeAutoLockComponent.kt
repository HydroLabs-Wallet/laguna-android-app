package io.novafoundation.nova.feature_account_impl.presentation.menu.change_autolock.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_account_impl.presentation.menu.change_autolock.ChangeAutoLockFragment

@Subcomponent()
@ScreenScope
interface ChangeAutoLockComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment
        ): ChangeAutoLockComponent
    }

    fun inject(fragment: ChangeAutoLockFragment)
}
