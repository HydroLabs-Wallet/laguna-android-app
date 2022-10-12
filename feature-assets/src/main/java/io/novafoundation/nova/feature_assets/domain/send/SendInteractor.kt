package io.novafoundation.nova.feature_assets.domain.send

import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountRepository
import io.novafoundation.nova.feature_assets.R
import io.novafoundation.nova.common.data.model.ContactUi
import io.novafoundation.nova.common.data.model.ContactUiHeader
import io.novafoundation.nova.common.data.model.ContactUiMarker
import io.novafoundation.nova.feature_wallet_api.data.network.blockhain.assets.AssetSourceRegistry
import io.novafoundation.nova.feature_wallet_api.data.network.blockhain.assets.tranfers.AssetTransfer
import io.novafoundation.nova.feature_wallet_api.domain.interfaces.WalletRepository
import io.novafoundation.nova.common.data.model.Contact
import io.novafoundation.nova.runtime.multiNetwork.ChainRegistry
import io.novafoundation.nova.runtime.multiNetwork.chain.model.Chain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.BigInteger

class SendInteractor(
    private val chainRegistry: ChainRegistry,
    private val walletRepository: WalletRepository,
    private val assetSourceRegistry: AssetSourceRegistry,
    private val accountRepository: AccountRepository,
    private val resourceManager: ResourceManager
) {

    suspend fun saveContact(data: Contact) {
        walletRepository.createContact(data)
    }

    fun getContacts(): Flow<List<ContactUiMarker>> {

        val contacts = walletRepository.getContacts()
            .map { list ->
                val newList = mutableListOf<ContactUiMarker>()
                if (list.isNotEmpty()) {
                    val header = ContactUiHeader(resourceManager.getString(R.string.contacts))
                    newList.add(header)
                }
                newList.addAll(list.map { ContactUi(name = it.name, address = it.address, memo = it.memo,id=it.id!!) })
                newList
            }
        return contacts
    }

    // TODO wallet phishing
    suspend fun isAddressFromPhishingList(address: String): Boolean {
        return /*walletRepository.isAccountIdFromPhishingList(address)*/ false
    }

    suspend fun getTransferFee(transfer: AssetTransfer): BigInteger = withContext(Dispatchers.Default) {
        getAssetTransfers(transfer).calculateFee(transfer)
    }

    suspend fun performTransfer(
        transfer: AssetTransfer,
        fee: BigDecimal
    ): Result<String> = withContext(Dispatchers.Default) {
        getAssetTransfers(transfer).performTransfer(transfer)
            .onSuccess { hash ->
                walletRepository.insertPendingTransfer(hash, transfer, fee)
            }
    }

    fun validationSystemFor(asset: Chain.Asset) = assetSourceRegistry.sourceFor(asset).transfers.validationSystem

    suspend fun areTransfersEnabled(asset: Chain.Asset) = assetSourceRegistry.sourceFor(asset).transfers.areTransfersEnabled(asset)

    private fun getAssetTransfers(transfer: AssetTransfer) =
        assetSourceRegistry.sourceFor(transfer.chainAsset).transfers
}
