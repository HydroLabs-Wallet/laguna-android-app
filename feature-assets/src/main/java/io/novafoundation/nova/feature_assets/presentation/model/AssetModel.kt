package io.novafoundation.nova.feature_assets.presentation.model

import io.novafoundation.nova.common.utils.equalTo
import java.math.BigDecimal

data class AssetModel(
    val token: TokenModel,
    val total: BigDecimal,
    val dollarAmount: BigDecimal?,
    val locked: BigDecimal,
    val bonded: BigDecimal,
    val reserved: BigDecimal,
    val redeemable: BigDecimal,
    val unbonding: BigDecimal,
    val available: BigDecimal
) : AssetMarker {
    override fun isItemSame(other: Any?): Boolean {
        return equalTo(other, { token.configuration.id }, { token.configuration.chainId })
    }

    override fun isContentSame(other: Any?): Boolean {
        return equalTo(other, { total }, { dollarAmount })

    }
}
