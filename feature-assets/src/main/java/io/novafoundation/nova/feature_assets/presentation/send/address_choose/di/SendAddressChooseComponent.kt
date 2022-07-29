package io.novafoundation.nova.feature_assets.presentation.send.address_choose.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import io.novafoundation.nova.feature_assets.presentation.send.address_choose.SendAddressChooseFragment
import io.novafoundation.nova.feature_assets.presentation.send_receive.SendReceivePayload

@Subcomponent()
@ScreenScope
interface SendAddressChooseComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance data: AssetPayload

        ): SendAddressChooseComponent
    }

    fun inject(fragment: SendAddressChooseFragment)
}
