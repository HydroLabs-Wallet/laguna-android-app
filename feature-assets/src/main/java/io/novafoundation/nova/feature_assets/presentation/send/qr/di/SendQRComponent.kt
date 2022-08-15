package io.novafoundation.nova.feature_assets.presentation.send.qr.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import io.novafoundation.nova.feature_assets.presentation.asset_receive.di.AssetReceiveModule
import io.novafoundation.nova.feature_assets.presentation.send.qr.SendQRFragment

@Subcomponent(modules = [AssetReceiveModule::class])
@ScreenScope
interface SendQRComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance data: AssetPayload
        ): SendQRComponent
    }

    fun inject(fragment: SendQRFragment)
}
