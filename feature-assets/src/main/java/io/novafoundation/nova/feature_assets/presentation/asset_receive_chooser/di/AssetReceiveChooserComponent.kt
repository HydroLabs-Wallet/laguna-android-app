package io.novafoundation.nova.feature_assets.presentation.asset_receive_chooser.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import io.novafoundation.nova.feature_assets.presentation.asset_choose.di.AssetChooseComponent
import io.novafoundation.nova.feature_assets.presentation.asset_receive_chooser.AssetReceiveChooserFragment
import io.novafoundation.nova.feature_assets.presentation.asset_receive_chooser.AssetReceivePayload

@Subcomponent()
@ScreenScope
interface AssetReceiveChooserComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance payLoad: AssetReceivePayload
        ): AssetReceiveChooserComponent
    }

    fun inject(fragment: AssetReceiveChooserFragment)
}
