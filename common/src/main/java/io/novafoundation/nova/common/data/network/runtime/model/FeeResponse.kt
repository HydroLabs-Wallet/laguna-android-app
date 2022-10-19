package io.novafoundation.nova.common.data.network.runtime.model

import java.math.BigInteger

class FeeResponse(
    val partialFee: String,
    val ref_time: RefTime
)
class RefTime(
    weight: Long
)
