package io.novafoundation.nova.app.root.di

import dagger.Module
import dagger.Provides
import io.novafoundation.nova.app.root.domain.RootInteractor
import io.novafoundation.nova.common.data.storage.Preferences
import io.novafoundation.nova.common.di.scope.FeatureScope
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.core.updater.UpdateSystem
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountRepository
import io.novafoundation.nova.feature_assets.data.repository.assetFilters.AssetFiltersRepository
import io.novafoundation.nova.feature_assets.data.repository.assetFilters.PreferencesAssetFiltersRepository
import io.novafoundation.nova.feature_assets.domain.WalletInteractor
import io.novafoundation.nova.feature_assets.domain.WalletInteractorImpl
import io.novafoundation.nova.feature_assets.domain.assets.list.AssetsListInteractor
import io.novafoundation.nova.feature_nft_api.data.repository.NftRepository
import io.novafoundation.nova.feature_wallet_api.di.Wallet
import io.novafoundation.nova.feature_wallet_api.domain.interfaces.WalletRepository
import io.novafoundation.nova.runtime.multiNetwork.ChainRegistry

@Module
class RootFeatureModule {

    @Provides
    @FeatureScope
    fun provideRootInteractor(
        walletRepository: WalletRepository,
        @Wallet walletUpdateSystem: UpdateSystem,
    ): RootInteractor {
        return RootInteractor(
            walletUpdateSystem,
            walletRepository
        )
    }
    @Provides
    @FeatureScope
    fun provideInteractor(
        accountRepository: AccountRepository,
        nftRepository: NftRepository
    ) = AssetsListInteractor(accountRepository, nftRepository)


    @Provides
    @FeatureScope
    fun provideWalletInteractor(
        walletRepository: WalletRepository,
        accountRepository: AccountRepository,
        assetFiltersRepository: AssetFiltersRepository,
        chainRegistry: ChainRegistry,
        nftRepository: NftRepository,
    ): WalletInteractor = WalletInteractorImpl(
        walletRepository,
        accountRepository,
        assetFiltersRepository,
        chainRegistry,
        nftRepository
    )

    @Provides
    @FeatureScope
    fun provideAssetFiltersRepository(preferences: Preferences): AssetFiltersRepository {
        return PreferencesAssetFiltersRepository(preferences)
    }
}
