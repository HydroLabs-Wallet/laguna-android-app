package io.novafoundation.nova.feature_wallet_api.domain.interfaces

import io.novafoundation.nova.common.data.model.CursorPage
import io.novafoundation.nova.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransfer
import io.novafoundation.nova.feature_wallet_api.domain.model.Asset
import io.novafoundation.nova.common.data.model.Contact
import io.novafoundation.nova.feature_wallet_api.domain.model.Operation
import io.novafoundation.nova.runtime.multiNetwork.chain.model.Chain
import io.novafoundation.nova.runtime.multiNetwork.chain.model.ChainId
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.math.BigInteger

interface WalletRepository {

    fun assetsFlow(metaId: Long): Flow<List<Asset>>

    suspend fun getAssets(metaId: Long): List<Asset>

    suspend fun syncAssetsRates()
    fun assetValueVisibleFlow(): Flow<Boolean>
    suspend fun toggleValueVisible()

    fun assetFlow(
        accountId: AccountId,
        chainAsset: Chain.Asset
    ): Flow<Asset>

    fun assetFlow(
        metaId: Long,
        chainAsset: Chain.Asset
    ): Flow<Asset>

    suspend fun getAsset(
        accountId: AccountId,
        chainAsset: Chain.Asset
    ): Asset?

    suspend fun getAsset(
        metaId: Long,
        chainAsset: Chain.Asset
    ): Asset?

    suspend fun syncOperationsFirstPage(
        pageSize: Int,
        filters: Set<TransactionFilter>,
        accountId: AccountId,
        chain: Chain,
        chainAsset: Chain.Asset
    )

    suspend fun getOperations(
        pageSize: Int,
        cursor: String?,
        filters: Set<TransactionFilter>,
        accountId: AccountId,
        chain: Chain,
        chainAsset: Chain.Asset
    ): CursorPage<Operation>

    fun operationsFirstPageFlow(
        accountId: AccountId,
        chain: Chain,
        chainAsset: Chain.Asset
    ): Flow<CursorPage<Operation>>

    fun getContacts(): Flow<List<Contact>>
    suspend fun createContact(data: Contact)

    suspend fun insertPendingTransfer(
        hash: String,
        assetTransfer: AssetTransfer,
        fee: BigDecimal
    )

    suspend fun updatePhishingAddresses()

    suspend fun isAccountIdFromPhishingList(accountId: AccountId): Boolean

    suspend fun getAccountFreeBalance(chainId: ChainId, accountId: AccountId): BigInteger
}
