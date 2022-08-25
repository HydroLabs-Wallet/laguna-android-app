package io.novafoundation.nova.feature_account_impl.presentation.password_confirm.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.data.model.ConfirmPayload
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_account_impl.presentation.password_confirm.PasswordConfirmFragment

@Subcomponent()
@ScreenScope
interface PasswordConfirmComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance data: ConfirmPayload
        ): PasswordConfirmComponent
    }

    fun inject(fragment: PasswordConfirmFragment)
}
