package io.novafoundation.nova.feature_assets.presentation.send_receive.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_assets.presentation.send_receive.SendReceiveFragment
import io.novafoundation.nova.feature_assets.presentation.send_receive.SendReceivePayload

@Subcomponent()
@ScreenScope
interface SendReceiveComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance data: SendReceivePayload
        ): SendReceiveComponent
    }

    fun inject(fragment: SendReceiveFragment)
}
