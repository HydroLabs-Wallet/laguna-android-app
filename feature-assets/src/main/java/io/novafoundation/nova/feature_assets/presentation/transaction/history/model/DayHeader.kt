package io.novafoundation.nova.feature_assets.presentation.transaction.history.model

import io.novafoundation.nova.common.utils.equalTo

data class DayHeader(val date: String) : OperationMarker {
    override fun isItemSame(other: Any?): Boolean {
        return equalTo(other, { date })
    }

    override fun isContentSame(other: Any?): Boolean {
        return true
    }
}
