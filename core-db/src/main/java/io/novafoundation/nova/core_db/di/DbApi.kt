package io.novafoundation.nova.core_db.di

import io.novafoundation.nova.core_db.AppDatabase
import io.novafoundation.nova.core_db.dao.*

interface DbApi {

    fun provideDatabase(): AppDatabase

    fun provideAccountDao(): AccountDao

    fun provideNodeDao(): NodeDao

    fun provideAssetDao(): AssetDao

    fun provideOperationDao(): OperationDao

    fun providePhishingAddressDao(): PhishingAddressDao

    fun storageDao(): StorageDao

    fun tokenDao(): TokenDao

    fun accountStakingDao(): AccountStakingDao

    fun stakingTotalRewardDao(): StakingTotalRewardDao

    fun chainDao(): ChainDao

    fun metaAccountDao(): MetaAccountDao

    fun dappAuthorizationDao(): DappAuthorizationDao

    fun nftDao(): NftDao
    fun contactDao():ContactsDao
    val phishingSitesDao: PhishingSitesDao

    val favouritesDAppsDao: FavouriteDAppsDao
}
