package io.novafoundation.nova.feature_assets.presentation.send

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ContactPayload(val address: String) : Parcelable {
}
