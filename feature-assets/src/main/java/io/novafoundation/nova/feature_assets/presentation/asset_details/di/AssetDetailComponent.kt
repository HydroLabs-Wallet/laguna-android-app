package io.novafoundation.nova.feature_assets.presentation.asset_details.di

import androidx.fragment.app.Fragment
import io.novafoundation.nova.feature_assets.presentation.asset_details.AssetDetailsFragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_assets.presentation.AssetPayload

@Subcomponent()
@ScreenScope
interface AssetDetailComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance assetPayload: AssetPayload,
        ): AssetDetailComponent
    }

    fun inject(fragment: AssetDetailsFragment)
}
