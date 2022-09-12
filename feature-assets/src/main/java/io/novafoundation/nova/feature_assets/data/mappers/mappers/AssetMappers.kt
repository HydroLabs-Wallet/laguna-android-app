package io.novafoundation.nova.feature_assets.data.mappers.mappers

import io.novafoundation.nova.common.utils.formatAsChange
import io.novafoundation.nova.common.utils.formatAsCurrency
import io.novafoundation.nova.common.utils.isNonNegative
import io.novafoundation.nova.common.utils.orZero
import io.novafoundation.nova.feature_assets.R
import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import io.novafoundation.nova.feature_assets.presentation.model.TokenModel
import io.novafoundation.nova.feature_wallet_api.domain.model.Asset
import io.novafoundation.nova.feature_wallet_api.domain.model.Token
import io.novafoundation.nova.runtime.multiNetwork.chain.model.Chain
import java.math.BigDecimal
import java.math.RoundingMode

fun mapTokenToTokenModel(token: Token): TokenModel {
    return with(token) {
        val rateChange = token.recentRateChange

        val changeColorRes = when {
            rateChange == null -> R.color.neutral500
            rateChange.isNonNegative -> R.color.green500
            else -> R.color.neutral500
        }
        val dollarChange = if(recentRateChange.orZero().stripTrailingZeros()!= BigDecimal.ZERO){
            dollarRate.orZero().multiply(recentRateChange.orZero()).divide(BigDecimal(100),RoundingMode.HALF_UP).formatAsCurrency()
        }else{
            BigDecimal.ZERO.formatAsCurrency()
        }
        TokenModel(
            configuration = configuration,
            dollarRate = dollarRate.orZero(),
            dollarChange = dollarChange,
            recentRateChange = (recentRateChange ?: BigDecimal.ZERO).formatAsChange(),
            rateChangeColorRes = changeColorRes
        )
    }
}

fun mapAssetToAssetModel(chain: Chain, asset: Asset, valuesVisible: Boolean): AssetModel {
    return with(asset) {
        AssetModel(
            token = mapTokenToTokenModel(token),
            total = total,
            bonded = bonded,
            locked = locked,
            available = transferable,
            reserved = reserved,
            redeemable = redeemable,
            unbonding = unbonding,
            dollarAmount = dollarAmount,
            chain = chain,
            showValues = valuesVisible
        )
    }
}
