package io.novafoundation.nova.app.root.di

import com.github.terrakok.cicerone.NavigatorHolder
import dagger.BindsInstance
import dagger.Component
import io.novafoundation.nova.app.root.navigation.NavigationHolderOld
import io.novafoundation.nova.app.root.presentation.RootRouter
import io.novafoundation.nova.app.root.presentation.dashboard.di.DashboardFragmentComponent
import io.novafoundation.nova.app.root.presentation.dashboard.page_assets.di.PageAssetsFragmentComponent
import io.novafoundation.nova.app.root.presentation.dashboard.page_networks.di.PageNetworksFragmentComponent
import io.novafoundation.nova.app.root.presentation.dashboard.сhain_setting.di.ChainSettingsFragmentComponent
import io.novafoundation.nova.app.root.presentation.di.RootActivityComponent
import io.novafoundation.nova.common.di.CommonApi
import io.novafoundation.nova.common.di.scope.FeatureScope
import io.novafoundation.nova.core_db.di.DbApi
import io.novafoundation.nova.feature_account_api.di.AccountFeatureApi
import io.novafoundation.nova.feature_assets.di.AssetsFeatureApi
import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import io.novafoundation.nova.feature_crowdloan_api.di.CrowdloanFeatureApi
import io.novafoundation.nova.feature_nft_api.NftFeatureApi
import io.novafoundation.nova.feature_staking_api.di.StakingFeatureApi
import io.novafoundation.nova.feature_wallet_api.di.WalletFeatureApi
import io.novafoundation.nova.runtime.di.RuntimeApi
import io.novafoundation.nova.splash.SplashRouter

@Component(
    dependencies = [
        RootDependencies::class
    ],
    modules = [
        RootFeatureModule::class
    ]
)
@FeatureScope
interface RootComponent {

    fun mainActivityComponentFactory(): RootActivityComponent.Factory

    fun dashboardFragmentComponentFactory(): DashboardFragmentComponent.Factory
    fun pageAssetsFragmentComponentFactory(): PageAssetsFragmentComponent.Factory
    fun pageNetworksFragmentComponentFactory(): PageNetworksFragmentComponent.Factory
    fun chainSettingsFragmentComponentFactory(): ChainSettingsFragmentComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance navigationHolderOld: NavigationHolderOld,
            @BindsInstance rootRouter: RootRouter,
            @BindsInstance walletRouter: WalletRouter,
            @BindsInstance splashRouter: SplashRouter,
            @BindsInstance navigationHolder: NavigatorHolder,

            deps: RootDependencies
        ): RootComponent
    }

    @Component(
        dependencies = [
            AccountFeatureApi::class,
            WalletFeatureApi::class,
            StakingFeatureApi::class,
            CrowdloanFeatureApi::class,
            AssetsFeatureApi::class,
            DbApi::class,
            CommonApi::class,
            RuntimeApi::class,
            NftFeatureApi::class,
            WalletFeatureApi::class

        ]
    )
    interface RootFeatureDependenciesComponent : RootDependencies
}
