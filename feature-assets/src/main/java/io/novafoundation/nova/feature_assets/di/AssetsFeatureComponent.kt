package io.novafoundation.nova.feature_assets.di

import dagger.BindsInstance
import dagger.Component
import io.novafoundation.nova.common.di.CommonApi
import io.novafoundation.nova.common.di.scope.FeatureScope
import io.novafoundation.nova.core_db.di.DbApi
import io.novafoundation.nova.feature_account_api.di.AccountFeatureApi
import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import io.novafoundation.nova.feature_assets.presentation.asset_choose.di.AssetChooseComponent
import io.novafoundation.nova.feature_assets.presentation.asset_receive.di.AssetReceiveComponent
import io.novafoundation.nova.feature_assets.presentation.asset_details.di.AssetDetailComponent
import io.novafoundation.nova.feature_assets.presentation.asset_transactions.di.AssetTransactionsComponent
import io.novafoundation.nova.feature_assets.presentation.balance.filters.di.AssetFiltersComponent
import io.novafoundation.nova.feature_assets.presentation.balance.list.di.BalanceListComponent
import io.novafoundation.nova.feature_assets.presentation.balance.list.view.GoToNftsView
import io.novafoundation.nova.feature_assets.presentation.receive.di.ReceiveComponent
import io.novafoundation.nova.feature_assets.presentation.send.amount.di.SelectSendComponent
import io.novafoundation.nova.feature_assets.presentation.send.confirm.di.ConfirmSendComponent
import io.novafoundation.nova.feature_assets.presentation.send_receive.di.SendReceiveComponent
import io.novafoundation.nova.feature_assets.presentation.transaction.detail.di.ExtrinsicDetailComponent
import io.novafoundation.nova.feature_assets.presentation.transaction.detail.di.RewardDetailComponent
import io.novafoundation.nova.feature_assets.presentation.transaction.detail.di.TransactionDetailComponent
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

    fun balanceListComponentFactory(): BalanceListComponent.Factory


    fun chooseAmountComponentFactory(): SelectSendComponent.Factory

    fun confirmTransferComponentFactory(): ConfirmSendComponent.Factory

    fun transactionDetailComponentFactory(): TransactionDetailComponent.Factory

    fun transactionHistoryComponentFactory(): TransactionHistoryFilterComponent.Factory

    fun rewardDetailComponentFactory(): RewardDetailComponent.Factory

    fun extrinsicDetailComponentFactory(): ExtrinsicDetailComponent.Factory

    fun receiveComponentFactory(): ReceiveComponent.Factory

    fun assetFiltersComponentFactory(): AssetFiltersComponent.Factory
    fun assetChooseComponentFactory(): AssetChooseComponent.Factory
    fun assetReceiveComponentFactory(): AssetReceiveComponent.Factory
    fun assetDetailComponentFactory(): AssetDetailComponent.Factory
    fun sendReceiveComponentFactory(): SendReceiveComponent.Factory
    fun assetTransactionsComponentFactory(): AssetTransactionsComponent.Factory

    fun inject(view: GoToNftsView)

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
