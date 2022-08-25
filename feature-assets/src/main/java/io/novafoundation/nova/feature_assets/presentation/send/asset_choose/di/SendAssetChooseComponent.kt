package io.novafoundation.nova.feature_assets.presentation.send.asset_choose.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_assets.presentation.send.asset_choose.SendAssetChooseFragment

@Subcomponent()
@ScreenScope
interface SendAssetChooseComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): SendAssetChooseComponent
    }

    fun inject(fragment: SendAssetChooseFragment)
}
