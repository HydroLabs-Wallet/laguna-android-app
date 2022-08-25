package io.novafoundation.nova.feature_assets.presentation.send.confirm.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_assets.presentation.asset_receive.di.AssetReceiveModule
import io.novafoundation.nova.feature_assets.presentation.send.TransferDraft
import io.novafoundation.nova.feature_assets.presentation.send.confirm.SendConfirmFragment

@Subcomponent(modules = [AssetReceiveModule::class])
@ScreenScope
interface SendConfirmComponent {

    @Subcomponent.Factory
    interface Factory {

        fun create(
            @BindsInstance fragment: Fragment,
            @BindsInstance data: TransferDraft
        ): SendConfirmComponent
    }

    fun inject(fragment: SendConfirmFragment)
}
