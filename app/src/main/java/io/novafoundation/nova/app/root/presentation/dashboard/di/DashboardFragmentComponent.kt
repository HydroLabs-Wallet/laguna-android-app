package io.novafoundation.nova.app.root.presentation.dashboard.di

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.app.root.presentation.dashboard.DashboardFragment
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.confirm.SeedConfirmFragment
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.confirm.di.SeedConfirmComponent
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.create.SeedWord

@Subcomponent(
    modules = [
        MainFragmentModule::class
    ]
)
@ScreenScope
interface DashboardFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment
        ): DashboardFragmentComponent
    }

    fun inject(fragment: DashboardFragment)
}
