package io.novafoundation.nova.feature_account_impl.presentation.address_book_contact.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.data.model.ContactPayload
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_account_impl.presentation.address_book_contact.AddressBookContactFragment

@Subcomponent()
@ScreenScope
interface AddressBookContactComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance data: ContactPayload
        ): AddressBookContactComponent
    }

    fun inject(fragment: AddressBookContactFragment)
}
