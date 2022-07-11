package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.create.di

import androidx.fragment.app.Fragment
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.create.SeedCreateFragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope

@Subcomponent()
@ScreenScope
interface SeedCreateComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance isAuth: Boolean,
        ): SeedCreateComponent
    }

    fun inject(fragment: SeedCreateFragment)
}