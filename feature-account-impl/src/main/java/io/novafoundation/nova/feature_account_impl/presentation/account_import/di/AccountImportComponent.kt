package io.novafoundation.nova.feature_account_impl.presentation.account_import.di

import androidx.fragment.app.Fragment
import io.novafoundation.nova.feature_account_impl.presentation.account_import.AccountImportFragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload

@Subcomponent(modules = [AccountImportModule::class])
@ScreenScope
interface AccountImportComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance isAuth: AddAccountPayload,
        ): AccountImportComponent
    }

    fun inject(fragment: AccountImportFragment)
}
