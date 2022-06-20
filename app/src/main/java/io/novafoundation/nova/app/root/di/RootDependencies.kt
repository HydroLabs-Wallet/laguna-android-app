package io.novafoundation.nova.app.root.di

import coil.ImageLoader
import com.github.terrakok.cicerone.NavigatorHolder
import io.novafoundation.nova.common.address.AddressIconGenerator
import io.novafoundation.nova.common.data.network.AppLinksProvider
import io.novafoundation.nova.common.data.storage.Preferences
import io.novafoundation.nova.common.mixin.api.NetworkStateMixin
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.common.utils.systemCall.SystemCallExecutor
import io.novafoundation.nova.core.updater.UpdateSystem
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountRepository
import io.novafoundation.nova.feature_account_api.domain.interfaces.SelectedAccountUseCase
import io.novafoundation.nova.feature_assets.data.buyToken.BuyTokenRegistry
import io.novafoundation.nova.feature_crowdloan_api.data.repository.CrowdloanRepository
import io.novafoundation.nova.feature_nft_api.data.repository.NftRepository
import io.novafoundation.nova.feature_staking_api.domain.api.StakingRepository
import io.novafoundation.nova.feature_wallet_api.di.Wallet
import io.novafoundation.nova.feature_wallet_api.domain.interfaces.WalletRepository
import io.novafoundation.nova.runtime.multiNetwork.ChainRegistry
import io.novafoundation.nova.runtime.multiNetwork.connection.ChainConnection
import kotlinx.coroutines.flow.MutableStateFlow

interface RootDependencies {
    fun preferences(): Preferences

    fun crowdloanRepository(): CrowdloanRepository

    fun networkStateMixin(): NetworkStateMixin

    fun externalRequirementsFlow(): MutableStateFlow<ChainConnection.ExternalRequirement>

    fun accountRepository(): AccountRepository

    fun walletRepository(): WalletRepository

    fun appLinksProvider(): AppLinksProvider

    fun buyTokenRegistry(): BuyTokenRegistry
    fun addressIconGenerator(): AddressIconGenerator
    fun selectedAccountUseCase(): SelectedAccountUseCase
    fun imageLoader(): ImageLoader

    fun resourceManager(): ResourceManager
    fun nftRepository(): NftRepository

    @Wallet
    fun walletUpdateSystem(): UpdateSystem

    fun stakingRepository(): StakingRepository

    fun chainRegistry(): ChainRegistry

    val systemCallExecutor: SystemCallExecutor
}
