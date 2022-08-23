package io.novafoundation.nova.feature_account_impl.presentation.edit_field.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.feature_account_impl.presentation.edit_field.EditFieldFragment
import io.novafoundation.nova.common.data.model.EditFieldPayload
import io.novafoundation.nova.common.di.scope.ScreenScope

@Subcomponent()
@ScreenScope
interface EditFieldComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance data: EditFieldPayload
        ): EditFieldComponent
    }

    fun inject(fragment: EditFieldFragment)
}
