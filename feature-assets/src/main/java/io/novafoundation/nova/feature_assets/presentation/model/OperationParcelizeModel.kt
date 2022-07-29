package io.novafoundation.nova.feature_assets.presentation.model

import android.os.Parcelable
import io.novafoundation.nova.runtime.multiNetwork.chain.model.ChainId
import kotlinx.android.parcel.Parcelize

sealed class OperationParcelizeModel : Parcelable {

    @Parcelize
    class Reward(
        val chainId: ChainId,
        val eventId: String,
        val address: String,
        val time: Long,
        val amount: String,
        val type: String,
        val era: String,
        val validator: String?,
        val statusAppearance: OperationStatusAppearance,
    ) : OperationParcelizeModel()

    @Parcelize
    class Extrinsic(
        val chainId: ChainId,
        val time: Long,
        val originAddress: String,
        val hash: String,
        val module: String,
        val call: String,
        val fee: String,
        val statusAppearance: OperationStatusAppearance,
    ) : Parcelable, OperationParcelizeModel()

    @Parcelize
    class Transfer(
        val hash: String?,
        val chainId: ChainId,
        val assetId: Int,
        val icon: String,
        val notNativeIcon: String?,
        val name: String,
        val time: String,
        val statusAppearance: OperationStatusAppearance,
        val amount: String,
        val dollarAmount: String,
        val totalAmount:String,
        val totalDollarAmount:String,
        val sender: String,
        val receiver: String,
        val fee: String,
        val dollarFee: String,
        val address: String,
        val isIncome:Boolean

    ) : Parcelable, OperationParcelizeModel()
}
