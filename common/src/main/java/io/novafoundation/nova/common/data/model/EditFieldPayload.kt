package io.novafoundation.nova.common.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EditFieldPayload(

    val tag: String,
    val title: String,
    val text: String?,
    val hint: String?

) : Parcelable {
}
