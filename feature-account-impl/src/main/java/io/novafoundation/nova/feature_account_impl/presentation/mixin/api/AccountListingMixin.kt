package io.novafoundation.nova.feature_account_impl.presentation.mixin.api

import io.novafoundation.nova.feature_account_impl.presentation.model.LightMetaAccountUi
import kotlinx.coroutines.flow.Flow

interface AccountListingMixin {

    fun accountsFlow(): Flow<List<LightMetaAccountUi>>
}
