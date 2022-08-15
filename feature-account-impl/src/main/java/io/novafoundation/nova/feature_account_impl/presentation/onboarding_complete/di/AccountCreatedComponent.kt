package io.novafoundation.nova.feature_account_impl.presentation.onboarding_complete.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload
import io.novafoundation.nova.feature_account_impl.presentation.onboarding_complete.AccountCreatedFragment

@Subcomponent()
@ScreenScope
interface AccountCreatedComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance isAuth: AddAccountPayload,
        ): AccountCreatedComponent
    }
    fun inject(fragment: AccountCreatedFragment)
}
