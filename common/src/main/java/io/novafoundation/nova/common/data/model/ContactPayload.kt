package io.novafoundation.nova.common.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContactPayload(val address: String, val id: String? = null, var name: String? = null, var memo: String? = null) : Parcelable {
}
