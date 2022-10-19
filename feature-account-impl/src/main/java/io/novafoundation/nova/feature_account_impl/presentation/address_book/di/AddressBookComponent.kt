package io.novafoundation.nova.feature_account_impl.presentation.address_book.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_account_impl.presentation.address_book.AddressBookFragment

@Subcomponent()
@ScreenScope
interface AddressBookComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): AddressBookComponent
    }

    fun inject(fragment: AddressBookFragment)
}
