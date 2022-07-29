package io.novafoundation.nova.feature_account_impl.presentation.select_account.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_account_impl.presentation.account.list.di.AccountListModule
import io.novafoundation.nova.feature_account_impl.presentation.select_account.SelectAccountFragment
import io.novafoundation.nova.common.data.model.SelectAccountPayload

@Subcomponent(
    modules = [
        AccountListModule::class
    ]
)
@ScreenScope
interface SelectAccountComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance data: SelectAccountPayload
        ): SelectAccountComponent
    }

    fun inject(fragment: SelectAccountFragment)
}
