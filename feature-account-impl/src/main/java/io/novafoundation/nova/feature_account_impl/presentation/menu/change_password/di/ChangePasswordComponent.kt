package io.novafoundation.nova.feature_account_impl.presentation.menu.change_password.di

import androidx.fragment.app.Fragment
import io.novafoundation.nova.feature_account_impl.presentation.login.LoginFragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_account_impl.presentation.login.di.LoginComponent
import io.novafoundation.nova.feature_account_impl.presentation.menu.change_password.ChangePasswordFragment

@Subcomponent()
@ScreenScope
interface ChangePasswordComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment
        ): ChangePasswordComponent
    }
    fun inject(fragment: ChangePasswordFragment)
}
