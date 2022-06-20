package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.confirm.di

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.confirm.SeedConfirmFragment
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.create.SeedWord

@Subcomponent(
    modules = [
        SeedConfirmModule::class
    ])
@ScreenScope
interface SeedConfirmComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance isAuth: Boolean,
            @BindsInstance list: List<SeedWord>
        ): SeedConfirmComponent
    }
    fun inject(fragment: SeedConfirmFragment)
}
