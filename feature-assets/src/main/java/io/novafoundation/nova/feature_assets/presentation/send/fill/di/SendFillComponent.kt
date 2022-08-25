package io.novafoundation.nova.feature_assets.presentation.send.fill.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_assets.presentation.asset_receive.di.AssetReceiveModule
import io.novafoundation.nova.feature_assets.presentation.send.TransferDraft
import io.novafoundation.nova.feature_assets.presentation.send.fill.SendFillFragment

@Subcomponent(modules = [AssetReceiveModule::class])
@ScreenScope
interface SendFillComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance data: TransferDraft
        ): SendFillComponent
    }

    fun inject(fragment: SendFillFragment)
}
