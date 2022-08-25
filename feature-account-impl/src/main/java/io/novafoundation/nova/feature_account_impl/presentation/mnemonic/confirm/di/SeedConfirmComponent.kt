package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.confirm.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.confirm.SeedConfirmFragment
import io.novafoundation.nova.feature_account_api.presenatation.account.add.SeedWord

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
            @BindsInstance isAuth: AddAccountPayload,
        ): SeedConfirmComponent
    }
    fun inject(fragment: SeedConfirmFragment)
}
