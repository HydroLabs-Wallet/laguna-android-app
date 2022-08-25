package io.novafoundation.nova.feature_assets.presentation.asset_choose.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_assets.presentation.asset_choose.AssetChooseFragment

@Subcomponent()
@ScreenScope
interface AssetChooseComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): AssetChooseComponent
    }

    fun inject(fragment: AssetChooseFragment)
}
