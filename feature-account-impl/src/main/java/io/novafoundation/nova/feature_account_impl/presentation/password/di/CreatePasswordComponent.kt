package io.novafoundation.nova.feature_account_impl.presentation.password.di

import androidx.fragment.app.Fragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.confirm.SeedConfirmFragment
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.create.SeedWord
import io.novafoundation.nova.feature_account_impl.presentation.password.CreatePasswordFragment

@Subcomponent()
@ScreenScope
interface CreatePasswordComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment
        ): CreatePasswordComponent
    }
    fun inject(fragment: CreatePasswordFragment)
}
