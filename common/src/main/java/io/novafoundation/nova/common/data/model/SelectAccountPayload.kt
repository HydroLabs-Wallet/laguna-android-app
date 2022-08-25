package io.novafoundation.nova.common.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SelectAccountPayload(val tag: String, val showAdd: Boolean) : Parcelable {
}
