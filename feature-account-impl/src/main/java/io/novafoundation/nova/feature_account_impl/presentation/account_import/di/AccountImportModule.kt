package io.novafoundation.nova.feature_account_impl.presentation.account_import.di

import android.content.Context
import dagger.Module
import dagger.Provides
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.feature_account_impl.presentation.AdvancedEncryptionCommunicator
import io.novafoundation.nova.feature_account_impl.presentation.AdvancedEncryptionRequester
import io.novafoundation.nova.feature_account_impl.presentation.account_import.FileReader

@Module
class AccountImportModule {

    @Provides
    @ScreenScope
    fun provideFileReader(context: Context) = FileReader(context)

    @Provides
    fun provideRequester(
        advancedEncryptionRequester: AdvancedEncryptionCommunicator
    ): AdvancedEncryptionRequester = advancedEncryptionRequester

}
