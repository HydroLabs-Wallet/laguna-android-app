package io.novafoundation.nova.feature_account_impl.presentation.mixin.impl

import io.novafoundation.nova.common.address.AddressIconGenerator
import io.novafoundation.nova.common.utils.IgnoredOnEquals
import io.novafoundation.nova.common.utils.ellipsis
import io.novafoundation.nova.common.utils.mapList
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountInteractor
import io.novafoundation.nova.feature_account_api.domain.model.LightMetaAccount
import io.novafoundation.nova.feature_account_api.domain.model.defaultSubstrateAddress
import io.novafoundation.nova.feature_account_impl.presentation.mixin.api.AccountListingMixin
import io.novafoundation.nova.feature_account_impl.presentation.model.LightMetaAccountUi

@Suppress("EXPERIMENTAL_API_USAGE")
class AccountListingProvider(
    private val accountInteractor: AccountInteractor,
    private val addressIconGenerator: AddressIconGenerator
) : AccountListingMixin {

    override fun accountsFlow() = accountInteractor.lightMetaAccountsFlow()
        .mapList { mapMetaAccountToUi(it, addressIconGenerator) }

    private suspend fun mapMetaAccountToUi(
        metaAccount: LightMetaAccount,
        iconGenerator: AddressIconGenerator,
    ) = with(metaAccount) {
        val icon = iconGenerator.createAddressIcon(metaAccount.substrateAccountId, AddressIconGenerator.SIZE_MEDIUM)
        val name = name.ifEmpty { metaAccount.defaultSubstrateAddress.ellipsis() }
        LightMetaAccountUi(
            id = id,
            name = name,
            isSelected = isSelected,
            picture = IgnoredOnEquals(icon),
            avatar = avatar
        )
    }
}
