package io.novafoundation.nova.feature_account_api.presenatation.account.add

import android.os.Parcelable
import io.novafoundation.nova.runtime.multiNetwork.chain.model.ChainId
import kotlinx.android.parcel.Parcelize

sealed class AddAccountPayload : Parcelable {

    @Parcelize
    class MetaAccount(val isAuth: Boolean, var seed: List<SeedWord> = emptyList()) : AddAccountPayload()

    @Parcelize
    class ChainAccount(val chainId: ChainId, val metaId: Long, var seed: List<SeedWord> = emptyList()) : AddAccountPayload()


}

val AddAccountPayload.chainIdOrNull
    get() = (this as? AddAccountPayload.ChainAccount)?.chainId

fun AddAccountPayload.isAuth(): Boolean {
    return when (this) {
        is AddAccountPayload.ChainAccount -> false
        is AddAccountPayload.MetaAccount -> this.isAuth
    }
}

fun AddAccountPayload.setSeedWord(seed: List<SeedWord>) {
    when (this) {
        is AddAccountPayload.ChainAccount -> this.seed = seed
        is AddAccountPayload.MetaAccount -> this.seed = seed
    }
}

fun AddAccountPayload.getSeedWord(): List<SeedWord> {
    return when (this) {
        is AddAccountPayload.ChainAccount -> this.seed
        is AddAccountPayload.MetaAccount -> this.seed
    }
}
