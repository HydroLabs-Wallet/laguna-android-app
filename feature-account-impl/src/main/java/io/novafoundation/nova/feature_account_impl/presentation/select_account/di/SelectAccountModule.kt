package io.novafoundation.nova.feature_account_impl.presentation.select_account.di

import dagger.Module
import dagger.Provides
import io.novafoundation.nova.common.address.AddressIconGenerator
import io.novafoundation.nova.common.di.scope.ScreenScope
import io.novafoundation.nova.common.di.viewmodel.ViewModelModule
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountInteractor
import io.novafoundation.nova.feature_account_impl.presentation.mixin.impl.AccountListingProvider
import io.novafoundation.nova.feature_account_impl.presentation.mixin.api.AccountListingMixin

@Module(includes = [ViewModelModule::class])
class SelectAccountModule {

    @Provides
    @ScreenScope
    fun provideAccountListingMixin(
        interactor: AccountInteractor,
        addressIconGenerator: AddressIconGenerator
    ): AccountListingMixin = AccountListingProvider(interactor, addressIconGenerator)
}
