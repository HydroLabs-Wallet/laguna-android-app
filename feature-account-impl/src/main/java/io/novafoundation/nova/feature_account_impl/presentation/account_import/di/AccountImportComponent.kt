package io.novafoundation.nova.feature_account_impl.presentation.account_import.di

import androidx.fragment.app.Fragment
import io.novafoundation.nova.feature_account_impl.presentation.account_import.AccountImportWFragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope

@Subcomponent(modules = [AccountImportModule::class])
@ScreenScope
interface AccountImportComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance isAuth: Boolean
        ): AccountImportComponent
    }

    fun inject(fragment: AccountImportWFragment)
}
