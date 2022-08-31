package io.novafoundation.nova.feature_account_impl.presentation.menu.change_avatar.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_account_impl.presentation.menu.change_avatar.ChangeAvatarFragment

@Subcomponent()
@ScreenScope
interface ChangeAvatarComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment
        ): ChangeAvatarComponent
    }

    fun inject(fragment: ChangeAvatarFragment)
}
