package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.create

import io.novafoundation.nova.common.utils.BaseMapper
import javax.inject.Inject

class SeedMapper @Inject constructor() : BaseMapper<List<String>, List<SeedWord>>() {
    override fun map(from: List<String>): List<SeedWord> {
        return from.mapIndexed { index, s -> SeedWord(index + 1, s) }
    }
}
