package io.novafoundation.nova.feature_assets.presentation.model

import androidx.annotation.ColorRes
import io.novafoundation.nova.runtime.multiNetwork.chain.model.Chain
import java.math.BigDecimal

data class TokenModel(
    val configuration: Chain.Asset,
    val dollarRate: BigDecimal,
    val recentRateChange: String,
    @ColorRes val rateChangeColorRes: Int
)
