package io.novafoundation.nova.feature_assets.presentation.transaction.history.model

import io.novafoundation.nova.common.utils.equalTo

data class DayHeader(val daysSinceEpoch: Long) : OperationMarker {
    override fun isItemSame(other: Any?): Boolean {
        return equalTo(other, { daysSinceEpoch })
    }

    override fun isContentSame(other: Any?): Boolean {
        return true
    }
}
