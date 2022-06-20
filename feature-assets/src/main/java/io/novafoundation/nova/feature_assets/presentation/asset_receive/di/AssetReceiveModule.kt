package io.novafoundation.nova.feature_assets.presentation.asset_receive.di

import dagger.Module
import dagger.Provides
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.common.di.viewmodel.ViewModelModule
import io.novafoundation.nova.common.interfaces.FileProvider
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountRepository
import io.novafoundation.nova.feature_assets.domain.receive.ReceiveInteractor
import io.novafoundation.nova.runtime.multiNetwork.ChainRegistry

@Module(includes = [ViewModelModule::class])
class AssetReceiveModule {

    @Provides
    @ScreenScope
    fun provideInteractor(
        fileProvider: FileProvider,
        chainRegistry: ChainRegistry,
        accountRepository: AccountRepository,
    ) = ReceiveInteractor(fileProvider, chainRegistry, accountRepository)


}
