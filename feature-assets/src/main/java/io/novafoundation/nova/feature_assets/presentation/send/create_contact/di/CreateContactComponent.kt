package io.novafoundation.nova.feature_assets.presentation.send.create_contact.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_assets.presentation.send.create_contact.CreateContactFragment
import io.novafoundation.nova.feature_assets.presentation.asset_receive.di.AssetReceiveModule
import io.novafoundation.nova.common.data.model.ContactPayload

@Subcomponent(modules = [AssetReceiveModule::class])
@ScreenScope
interface CreateContactComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance data: ContactPayload
        ): CreateContactComponent
    }

    fun inject(fragment: CreateContactFragment)
}
