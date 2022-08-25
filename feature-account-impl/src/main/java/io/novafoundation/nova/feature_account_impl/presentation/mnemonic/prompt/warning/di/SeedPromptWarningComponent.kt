package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt.warning.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt.warning.SeedWarningFragment

@Subcomponent()
@ScreenScope
interface SeedPromptWarningComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment,
        ): SeedPromptWarningComponent
    }

    fun inject(fragment: SeedWarningFragment)
}
