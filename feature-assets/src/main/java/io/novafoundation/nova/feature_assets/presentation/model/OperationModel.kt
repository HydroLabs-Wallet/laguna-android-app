package io.novafoundation.nova.feature_assets.presentation.model

import androidx.annotation.ColorRes
import io.novafoundation.nova.common.utils.equalTo
import io.novafoundation.nova.common.utils.images.Icon
import io.novafoundation.nova.feature_assets.presentation.transaction.history.model.OperationMarker

class OperationModel(
    val id: String,
    val formattedTime: String,
    val amount: String,
    @ColorRes val amountColorRes: Int,
    val header: String,
    val statusAppearance: OperationStatusAppearance,
    val operationIcon: Icon,
    val subHeader: String
) : OperationMarker {
    override fun isItemSame(other: Any?): Boolean {
        return equalTo(other, { id })
    }

    override fun isContentSame(other: Any?): Boolean {
        return equalTo(other, { amount }, { formattedTime })
    }
}
