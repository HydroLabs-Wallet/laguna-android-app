package io.novafoundation.nova.feature_assets.presentation.asset_transactions.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import io.novafoundation.nova.feature_assets.presentation.asset_transactions.AssetTransactionsFragment

@Subcomponent(
    modules = [
        AssetTransactionsModule::class
    ]
)
@ScreenScope
interface AssetTransactionsComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance assetPayload: AssetPayload,
        ): AssetTransactionsComponent
    }

    fun inject(fragment: AssetTransactionsFragment)
}
