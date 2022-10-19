package io.novafoundation.nova.feature_assets.di

import dagger.BindsInstance
import dagger.Component
import io.novafoundation.nova.common.di.CommonApi
import io.novafoundation.nova.common.di.scope.FeatureScope
import io.novafoundation.nova.core_db.di.DbApi
import io.novafoundation.nova.feature_account_api.di.AccountFeatureApi
import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import io.novafoundation.nova.feature_assets.presentation.all_transactions.di.AllTransactionsComponent
import io.novafoundation.nova.feature_assets.presentation.asset_choose.di.AssetChooseComponent
import io.novafoundation.nova.feature_assets.presentation.asset_details.di.AssetDetailComponent
import io.novafoundation.nova.feature_assets.presentation.asset_receive.di.AssetReceiveComponent
import io.novafoundation.nova.feature_assets.presentation.asset_receive_chooser.di.AssetReceiveChooserComponent
import io.novafoundation.nova.feature_assets.presentation.asset_transactions.details.transfer.di.TransferDetailsComponent
import io.novafoundation.nova.feature_assets.presentation.asset_transactions.di.AssetTransactionsComponent
import io.novafoundation.nova.feature_assets.presentation.balance.filters.di.AssetFiltersComponent
import io.novafoundation.nova.feature_assets.presentation.receive.di.ReceiveComponent
import io.novafoundation.nova.feature_assets.presentation.send.address_choose.di.SendAddressChooseComponent
import io.novafoundation.nova.feature_assets.presentation.send.asset_choose.di.SendAssetChooseComponent
import io.novafoundation.nova.feature_assets.presentation.send.confirm.di.SendConfirmComponent
import io.novafoundation.nova.feature_assets.presentation.send.create_contact.di.CreateContactComponent
import io.novafoundation.nova.feature_assets.presentation.send.fill.di.SendFillComponent
import io.novafoundation.nova.feature_assets.presentation.send.qr.di.SendQRComponent
import io.novafoundation.nova.feature_assets.presentation.send_receive.di.SendReceiveComponent
import io.novafoundation.nova.feature_assets.presentation.transaction.filter.di.TransactionHistoryFilterComponent
import io.novafoundation.nova.feature_nft_api.NftFeatureApi
import io.novafoundation.nova.feature_wallet_api.di.WalletFeatureApi
import io.novafoundation.nova.runtime.di.RuntimeApi

@Component(
    dependencies = [
        AssetsFeatureDependencies::class
    ],
    modules = [
        AssetsFeatureModule::class,
    ]
)
@FeatureScope
interface AssetsFeatureComponent : AssetsFeatureApi {


    fun transactionHistoryComponentFactory(): TransactionHistoryFilterComponent.Factory
    fun assetReceiveChooserComponentFactory(): AssetReceiveChooserComponent.Factory
    fun transferDetailsComponentFactory(): TransferDetailsComponent.Factory


    fun receiveComponentFactory(): ReceiveComponent.Factory

    fun assetFiltersComponentFactory(): AssetFiltersComponent.Factory
    fun assetChooseComponentFactory(): AssetChooseComponent.Factory
    fun assetReceiveComponentFactory(): AssetReceiveComponent.Factory
    fun assetDetailComponentFactory(): AssetDetailComponent.Factory
    fun sendReceiveComponentFactory(): SendReceiveComponent.Factory
    fun assetTransactionsComponentFactory(): AssetTransactionsComponent.Factory
    fun allTransactionsComponentFactory(): AllTransactionsComponent.Factory

    //send
    fun sendAssetChooseComponentFactory(): SendAssetChooseComponent.Factory
    fun sendAddressChooseComponentFactory(): SendAddressChooseComponent.Factory
    fun sendQRComponentFactory(): SendQRComponent.Factory

    fun createContactComponentFactory(): CreateContactComponent.Factory
    fun sendFillComponentFactory(): SendFillComponent.Factory
    fun sendConfirmComponentFactory(): SendConfirmComponent.Factory

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance accountRouter: WalletRouter,
            deps: AssetsFeatureDependencies
        ): AssetsFeatureComponent
    }

    @Component(
        dependencies = [
            CommonApi::class,
            DbApi::class,
            RuntimeApi::class,
            NftFeatureApi::class,
            WalletFeatureApi::class,
            AccountFeatureApi::class
        ]
    )
    interface AssetsFeatureDependenciesComponent : AssetsFeatureDependencies
}
