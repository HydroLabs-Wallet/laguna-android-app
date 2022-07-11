package io.novafoundation.nova.feature_assets.presentation.balance.list.model

import io.novafoundation.nova.common.utils.formatAsChange
import io.novafoundation.nova.common.utils.orZero
import io.novafoundation.nova.feature_wallet_api.domain.model.Balances
import java.math.BigDecimal

class TotalBalanceModel(
    val shouldShowPlaceholder: Boolean,
    val totalBalanceFiat: String,
    val lockedBalanceFiat: String,
    val balances: Balances
) {
    var totalBalance:BigDecimal= BigDecimal.ZERO
    var balanceChange = calculateBalanceChange()
    fun calculateBalanceChange(): String {
        var previousBalance: BigDecimal = BigDecimal.ZERO
        balances.assets.forEach {
            it.value.forEach {
                val amount = it.dollarAmount
                totalBalance += amount
                val recentRateChange = it.token.recentRateChange.orZero()
                previousBalance += amount - amount * recentRateChange / BigDecimal(100)
            }
        }
        val change = if (previousBalance.stripTrailingZeros() == BigDecimal.ZERO) BigDecimal.ZERO else totalBalance.orZero() / previousBalance
        return change.formatAsChange()
    }
}
