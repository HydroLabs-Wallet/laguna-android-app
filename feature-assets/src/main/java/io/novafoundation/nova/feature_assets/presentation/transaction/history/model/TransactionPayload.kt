package io.novafoundation.nova.feature_assets.presentation.transaction.history.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class TransactionPayload(
    val transactionId: String,
    val chainId: String,
    val chainAssetId: String
) : Parcelable
