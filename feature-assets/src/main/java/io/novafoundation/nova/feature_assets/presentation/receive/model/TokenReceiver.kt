package io.novafoundation.nova.feature_assets.presentation.receive.model

import io.novafoundation.nova.common.address.AddressModel
import io.novafoundation.nova.feature_account_api.presenatation.chain.ChainUi
import io.novafoundation.nova.runtime.multiNetwork.chain.model.Chain

class TokenReceiver(
    val asset:Chain.Asset?=null,
    val addressModel: AddressModel,
    val chain: ChainUi,
)
