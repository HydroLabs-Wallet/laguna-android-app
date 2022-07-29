package io.novafoundation.nova.feature_assets.presentation.send

import android.os.Parcelable
import io.novafoundation.nova.common.utils.DiffEquals
import io.novafoundation.nova.common.utils.equalTo
import kotlinx.android.parcel.Parcelize

interface ContactUiMarker : DiffEquals

@Parcelize
data class ContactUi(val name: String, val address: String) : ContactUiMarker, Parcelable {
    override fun isItemSame(other: Any?): Boolean {
        return equalTo(other, { name }, { address })
    }

    override fun isContentSame(other: Any?): Boolean {
        return true
    }
}

data class ContactUiHeader(val title: String) : ContactUiMarker {
    override fun isItemSame(other: Any?): Boolean {
        return equalTo(other, { title })
    }

    override fun isContentSame(other: Any?): Boolean {
        return true
    }
}
