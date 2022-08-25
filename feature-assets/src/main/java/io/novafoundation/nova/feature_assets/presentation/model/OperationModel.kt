package io.novafoundation.nova.feature_assets.presentation.model

import io.novafoundation.nova.common.utils.equalTo
import io.novafoundation.nova.feature_assets.presentation.transaction.history.model.OperationMarker
import io.novafoundation.nova.feature_wallet_api.domain.model.Operation

class OperationModel(
    val id: String,
    val operation:Operation,
    val formattedTime: String,
    val icon: String,
    val notNativeIcon:String?,
    val title: String,
    val subtitle:String,
    val amount: String,
    val dollarAmount: String,
    val statusAppearance: OperationStatusAppearance,

) : OperationMarker {
    override fun isItemSame(other: Any?): Boolean {
        return equalTo(other, { id })
    }

    override fun isContentSame(other: Any?): Boolean {
        return equalTo(other, { amount }, { dollarAmount }, { formattedTime },{statusAppearance})
    }
}
