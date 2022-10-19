package io.novafoundation.nova.feature_account_impl.presentation.add_existing_account_complete.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload
import io.novafoundation.nova.feature_account_impl.presentation.add_existing_account_complete.AddToExistingAccountCompleteFragment

@Subcomponent()
@ScreenScope
interface AddToExistingAccountCompleteComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance isAuth: AddAccountPayload,
        ): AddToExistingAccountCompleteComponent
    }

    fun inject(fragment: AddToExistingAccountCompleteFragment)
}
