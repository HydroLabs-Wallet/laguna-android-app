package io.novafoundation.nova.feature_assets.presentation.send

import android.os.Parcelable
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
class TransferDraft(
    var amount: BigDecimal,
    var fee: BigDecimal,
    var assetPayload: AssetPayload,
    var contact: ContactUi
) : Parcelable {
    fun getTotal(): BigDecimal = amount + fee
    fun totalAfterTransfer(currentTotal: BigDecimal) = currentTotal - getTotal()
}
