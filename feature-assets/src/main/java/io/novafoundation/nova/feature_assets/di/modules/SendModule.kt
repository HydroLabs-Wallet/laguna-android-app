package io.novafoundation.nova.feature_assets.di.modules

import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import io.novafoundation.nova.common.address.AddressIconGenerator
import io.novafoundation.nova.common.data.network.AppLinksProvider
import io.novafoundation.nova.common.di.scope.FeatureScope
import io.novafoundation.nova.common.di.viewmodel.ViewModelKey
import io.novafoundation.nova.common.resources.AppVersionProvider
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountInteractor
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountRepository
import io.novafoundation.nova.feature_assets.domain.send.SendInteractor
import io.novafoundation.nova.feature_wallet_api.data.network.blockhain.assets.AssetSourceRegistry
import io.novafoundation.nova.feature_wallet_api.domain.interfaces.WalletRepository
import io.novafoundation.nova.runtime.multiNetwork.ChainRegistry
import kotlin.math.acos

@Module
class SendModule {

    @Provides
    @FeatureScope
    fun provideSendInteractor(
        chainRegistry: ChainRegistry,
        walletRepository: WalletRepository,
        assetSourceRegistry: AssetSourceRegistry,
        accountRepository: AccountRepository,
        resourceManager: ResourceManager
    ) = SendInteractor(
        chainRegistry = chainRegistry,
        walletRepository = walletRepository,
        assetSourceRegistry = assetSourceRegistry,
        accountRepository = accountRepository,
        resourceManager=resourceManager
    )


}
