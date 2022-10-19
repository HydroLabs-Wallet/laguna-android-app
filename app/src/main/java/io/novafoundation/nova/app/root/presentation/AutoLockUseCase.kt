package io.novafoundation.nova.app.root.presentation

import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountRepository
import kotlinx.coroutines.flow.Flow

class AutoLockUseCase(private val accountRepository: AccountRepository) {

    fun getTimerFlow(): Flow<String> {
        return accountRepository.getAutoLockTimer()
    }
}
