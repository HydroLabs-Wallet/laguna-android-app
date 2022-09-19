package io.novafoundation.nova.feature_account_impl.di

import dagger.BindsInstance
import dagger.Component
import io.novafoundation.nova.common.di.CommonApi
import io.novafoundation.nova.common.di.scope.FeatureScope
import io.novafoundation.nova.core_db.di.DbApi
import io.novafoundation.nova.feature_account_api.di.AccountFeatureApi
import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import io.novafoundation.nova.feature_account_impl.presentation.AdvancedEncryptionCommunicator
import io.novafoundation.nova.feature_account_impl.presentation.account.advancedEncryption.di.AdvancedEncryptionComponent
import io.novafoundation.nova.feature_account_impl.presentation.account_import.di.AccountImportComponent
import io.novafoundation.nova.feature_account_impl.presentation.account_import.info.di.AccountImportInfoComponent
import io.novafoundation.nova.feature_account_impl.presentation.add_existing_account_complete.di.AddToExistingAccountCompleteComponent
import io.novafoundation.nova.feature_account_impl.presentation.add_to_existing.di.AddToExistingAccountComponent
import io.novafoundation.nova.feature_account_impl.presentation.edit_field.di.EditFieldComponent
import io.novafoundation.nova.feature_account_impl.presentation.language.di.LanguagesComponent
import io.novafoundation.nova.feature_account_impl.presentation.list.di.AccountListComponent
import io.novafoundation.nova.feature_account_impl.presentation.login.di.LoginComponent
import io.novafoundation.nova.feature_account_impl.presentation.menu.change_autolock.di.ChangeAutoLockComponent
import io.novafoundation.nova.feature_account_impl.presentation.menu.change_avatar.di.ChangeAvatarComponent
import io.novafoundation.nova.feature_account_impl.presentation.menu.change_password.di.ChangePasswordComponent
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.confirm.di.SeedConfirmComponent
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.create.di.SeedCreateComponent
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt.di.SeedPromptComponent
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt.info.di.SeedPromptInfoComponent
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt.warning.di.SeedPromptWarningComponent
import io.novafoundation.nova.feature_account_impl.presentation.onboarding_complete.di.AccountCreatedComponent
import io.novafoundation.nova.feature_account_impl.presentation.password.di.CreatePasswordComponent
import io.novafoundation.nova.feature_account_impl.presentation.password_confirm.di.PasswordConfirmComponent
import io.novafoundation.nova.feature_account_impl.presentation.select_account.di.SelectAccountComponent
import io.novafoundation.nova.feature_account_impl.presentation.settings.di.SettingsComponent
import io.novafoundation.nova.runtime.di.RuntimeApi

@Component(
    dependencies = [
        AccountFeatureDependencies::class,
    ],
    modules = [
        AccountFeatureModule::class,
        ExportModule::class
    ]
)
@FeatureScope
interface AccountFeatureComponent : AccountFeatureApi {

    fun seedPromptComponentFactory(): SeedPromptComponent.Factory
    fun seedPromptInfoComponentFactory(): SeedPromptInfoComponent.Factory
    fun seedPromptWarningComponentFactory(): SeedPromptWarningComponent.Factory
    fun seedCreateComponentFactory(): SeedCreateComponent.Factory
    fun seedConfirmComponentFactory(): SeedConfirmComponent.Factory
    fun createPasswordComponentFactory(): CreatePasswordComponent.Factory
    fun passwordConfirmComponentFactory(): PasswordConfirmComponent.Factory

    fun accountCreatedComponentFactory(): AccountCreatedComponent.Factory
    fun addToExistingAccountCompleteComponentFactory(): AddToExistingAccountCompleteComponent.Factory

    fun accountImportInfoComponent(): AccountImportInfoComponent.Factory
    fun accountImportComponent(): AccountImportComponent.Factory

    fun addToExistingAccountComponent(): AddToExistingAccountComponent.Factory

    fun loginComponent(): LoginComponent.Factory

    fun profileComponentFactory(): SettingsComponent.Factory
    fun editFieldComponentFactory(): EditFieldComponent.Factory
    fun changePasswordComponentFactory(): ChangePasswordComponent.Factory
    fun changeAvatarComponentFactory(): ChangeAvatarComponent.Factory
    fun changeAutoLockComponentFactory(): ChangeAutoLockComponent.Factory

    fun languagesComponentFactory(): LanguagesComponent.Factory

    fun advancedEncryptionComponentFactory(): AdvancedEncryptionComponent.Factory
    fun accountsComponentFactory(): AccountListComponent.Factory
    fun selectAccountComponentFactory(): SelectAccountComponent.Factory

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance accountRouter: AccountRouter,
            @BindsInstance advancedEncryptionCommunicator: AdvancedEncryptionCommunicator,
            deps: AccountFeatureDependencies
        ): AccountFeatureComponent
    }

    @Component(
        dependencies = [
            CommonApi::class,
            RuntimeApi::class,
            DbApi::class
        ]
    )
    interface AccountFeatureDependenciesComponent : AccountFeatureDependencies
}
