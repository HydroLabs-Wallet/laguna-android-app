package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.create

import android.os.Parcelable

import io.novafoundation.nova.common.utils.DiffEquals
import io.novafoundation.nova.common.utils.equalTo
import kotlinx.android.parcel.Parcelize


@Parcelize
data class SeedWord(
    val number: Int,
    val word: String,
    val isChecked: Boolean = false,
    val isFocused: Boolean = false,
    val isVerify: Boolean = false
) : DiffEquals, Parcelable {


    override fun isItemSame(other: Any?): Boolean {
        return equalTo(other, { number })
    }

    override fun isContentSame(other: Any?): Boolean {
        return equalTo(other, { word }, { isFocused }, { isVerify }, { isChecked })
    }
}
