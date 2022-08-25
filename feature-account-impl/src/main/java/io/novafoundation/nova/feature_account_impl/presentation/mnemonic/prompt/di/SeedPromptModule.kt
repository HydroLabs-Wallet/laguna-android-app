package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt.di

import dagger.Module
import dagger.Provides
import io.novafoundation.nova.feature_account_impl.presentation.AdvancedEncryptionCommunicator
import io.novafoundation.nova.feature_account_impl.presentation.AdvancedEncryptionRequester

@Module()
class SeedPromptModule {

    @Provides
    fun provideRequester(
        advancedEncryptionRequester: AdvancedEncryptionCommunicator

    ): AdvancedEncryptionRequester = advancedEncryptionRequester

}
