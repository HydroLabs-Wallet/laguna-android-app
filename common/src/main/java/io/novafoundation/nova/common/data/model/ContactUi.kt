package io.novafoundation.nova.common.data.model

import android.os.Parcelable
import io.novafoundation.nova.common.utils.DiffEquals
import io.novafoundation.nova.common.utils.equalTo
import kotlinx.android.parcel.Parcelize

interface ContactUiMarker : DiffEquals

@Parcelize
data class ContactUi(val id: String?, val name: String, val address: String, val memo: String? = null) : ContactUiMarker, Parcelable {
    override fun isItemSame(other: Any?): Boolean {
        return equalTo(other, { id })
    }

    override fun isContentSame(other: Any?): Boolean {
        return equalTo(other, { name }, { address }, { memo })
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
