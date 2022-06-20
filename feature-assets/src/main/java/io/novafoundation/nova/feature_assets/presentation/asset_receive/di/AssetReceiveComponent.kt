package io.novafoundation.nova.feature_assets.presentation.asset_receive.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import io.novafoundation.nova.feature_assets.presentation.asset_receive.AssetReceiveFragment

@Subcomponent(modules = [AssetReceiveModule::class])
@ScreenScope
interface AssetReceiveComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance data:AssetPayload
        ): AssetReceiveComponent
    }

    fun inject(fragment: AssetReceiveFragment)
}
