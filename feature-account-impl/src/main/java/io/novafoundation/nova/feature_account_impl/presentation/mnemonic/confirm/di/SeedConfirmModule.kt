package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.confirm.di

import dagger.Module
import dagger.Provides
import io.novafoundation.nova.feature_account_impl.presentation.AdvancedEncryptionCommunicator
import io.novafoundation.nova.feature_account_impl.presentation.AdvancedEncryptionRequester

@Module()
class SeedConfirmModule {

    @Provides
    fun provideRequester(
        advancedEncryptionRequester: AdvancedEncryptionCommunicator

    ): AdvancedEncryptionRequester = advancedEncryptionRequester

}
