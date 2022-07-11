package io.novafoundation.nova.feature_assets.presentation.send_receive

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SendReceivePayload(val sendEnabled: Boolean) : Parcelable {
}
