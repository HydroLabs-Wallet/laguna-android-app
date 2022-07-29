package io.novafoundation.nova.feature_assets.presentation.asset_transactions.details.transfer.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_assets.presentation.asset_transactions.details.transfer.TransferDetailsFragment
import io.novafoundation.nova.feature_assets.presentation.model.OperationParcelizeModel

@Subcomponent()
@ScreenScope
interface TransferDetailsComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance data: OperationParcelizeModel.Transfer,
        ): TransferDetailsComponent
    }

    fun inject(fragment: TransferDetailsFragment)
}
