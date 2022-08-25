package io.novafoundation.nova.app.di.app.navigation

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import io.novafoundation.nova.app.root.navigation.NavigationHolderOld
import io.novafoundation.nova.app.root.navigation.Navigator
import io.novafoundation.nova.app.root.presentation.LocalCiceroneHolder
import io.novafoundation.nova.app.root.presentation.RootRouter
import io.novafoundation.nova.common.di.scope.ApplicationScope
import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import io.novafoundation.nova.feature_crowdloan_impl.presentation.CrowdloanRouter
import io.novafoundation.nova.feature_onboarding_impl.OnboardingRouter
import io.novafoundation.nova.feature_staking_impl.presentation.StakingRouter
import io.novafoundation.nova.splash.SplashRouter

@Module(
    includes = [
        AccountNavigationModule::class,
        DAppNavigationModule::class,
        NftNavigationModule::class,
    ]
)
class NavigationModule {

    @ApplicationScope
    @Provides
    fun provideNavigatorHolderOld(): NavigationHolderOld = NavigationHolderOld()

    @ApplicationScope
    @Provides
    fun provideNavigator(
        navigatorHolder: NavigationHolderOld,
        router: Router
    ): Navigator = Navigator(navigatorHolder, router)

    @Provides
    @ApplicationScope
    fun provideRootRouter(navigator: Navigator): RootRouter = navigator

    @ApplicationScope
    @Provides
    fun provideSplashRouter(navigator: Navigator): SplashRouter = navigator

    @ApplicationScope
    @Provides
    fun provideOnboardingRouter(navigator: Navigator): OnboardingRouter = navigator

    @ApplicationScope
    @Provides
    fun provideWalletRouter(navigator: Navigator): WalletRouter = navigator

    @ApplicationScope
    @Provides
    fun provideStakingRouter(navigator: Navigator): StakingRouter = navigator

    @ApplicationScope
    @Provides
    fun provideCrowdloanRouter(navigator: Navigator): CrowdloanRouter = navigator

    private val cicerone: Cicerone<Router> = Cicerone.create()

    @ApplicationScope
    @Provides
    fun provideRouter(): Router {
        return cicerone.router
    }

    @ApplicationScope
    @Provides
    fun provideNavigatorHolder(): NavigatorHolder {
        return cicerone.getNavigatorHolder()
    }

    @ApplicationScope
    @Provides
    fun provideLocalNavigationHolder(): LocalCiceroneHolder = LocalCiceroneHolder()
}
