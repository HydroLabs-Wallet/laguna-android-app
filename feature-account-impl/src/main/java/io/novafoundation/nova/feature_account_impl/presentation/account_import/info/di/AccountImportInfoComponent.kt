package io.novafoundation.nova.feature_account_impl.presentation.account_import.info.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_account_impl.presentation.account_import.info.AccountImportInfoFragment

@Subcomponent()
@ScreenScope
interface AccountImportInfoComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment,
            ): AccountImportInfoComponent
    }
    fun inject(fragment: AccountImportInfoFragment)
}
