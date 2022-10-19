package io.novafoundation.nova.feature_assets.presentation.all_transactions.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import io.novafoundation.nova.feature_assets.presentation.all_transactions.AllTransactionsFragment
import io.novafoundation.nova.feature_assets.presentation.asset_transactions.AssetTransactionsFragment

@Subcomponent(
    modules = [
        AllTransactionsModule::class
    ]
)
@ScreenScope
interface AllTransactionsComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment
        ): AllTransactionsComponent
    }

    fun inject(fragment: AllTransactionsFragment)
}
