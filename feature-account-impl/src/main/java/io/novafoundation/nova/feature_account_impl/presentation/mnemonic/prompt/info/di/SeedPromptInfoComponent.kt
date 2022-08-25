package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt.info.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt.info.SeedInfoFragment

@Subcomponent()
@ScreenScope
interface SeedPromptInfoComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment,
        ): SeedPromptInfoComponent
    }

    fun inject(fragment: SeedInfoFragment)
}
