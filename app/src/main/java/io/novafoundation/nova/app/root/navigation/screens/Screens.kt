package io.novafoundation.nova.app.root.navigation.screens

import com.github.terrakok.cicerone.androidx.FragmentScreen
import io.novafoundation.nova.app.root.presentation.dashboard.DashboardFragment
import io.novafoundation.nova.app.root.presentation.dashboard.сhain_setting.ChainSettingsFragment
import io.novafoundation.nova.feature_account_impl.presentation.edit_field.EditFieldFragment
import io.novafoundation.nova.app.root.presentation.menu.MenuNavDrawerFragment
import io.novafoundation.nova.common.data.model.ConfirmPayload
import io.novafoundation.nova.common.data.model.EditFieldPayload
import io.novafoundation.nova.common.data.model.SelectAccountPayload
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload
import io.novafoundation.nova.feature_account_impl.presentation.account_import.AccountImportFragment
import io.novafoundation.nova.feature_account_impl.presentation.account_import.info.AccountImportInfoFragment
import io.novafoundation.nova.feature_account_impl.presentation.login.LoginFragment
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.confirm.SeedConfirmFragment
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.create.SeedCreateFragment
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt.SeedPromptFragment
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt.info.SeedInfoFragment
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt.warning.SeedWarningFragment
import io.novafoundation.nova.feature_account_impl.presentation.onboarding_complete.AccountCreatedFragment
import io.novafoundation.nova.feature_account_impl.presentation.password.CreatePasswordFragment
import io.novafoundation.nova.feature_account_impl.presentation.password_confirm.PasswordConfirmFragment
import io.novafoundation.nova.feature_account_impl.presentation.select_account.SelectAccountFragment
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import io.novafoundation.nova.feature_assets.presentation.asset_choose.AssetChooseFragment
import io.novafoundation.nova.feature_assets.presentation.asset_details.AssetDetailsFragment
import io.novafoundation.nova.feature_assets.presentation.asset_receive.AssetReceiveFragment
import io.novafoundation.nova.feature_assets.presentation.asset_receive_chooser.AssetReceiveChooserFragment
import io.novafoundation.nova.feature_assets.presentation.asset_receive_chooser.AssetReceivePayload
import io.novafoundation.nova.feature_assets.presentation.asset_transactions.AssetTransactionsFragment
import io.novafoundation.nova.feature_assets.presentation.asset_transactions.details.transfer.TransferDetailsFragment
import io.novafoundation.nova.feature_assets.presentation.model.OperationParcelizeModel
import io.novafoundation.nova.feature_assets.presentation.send.ContactPayload
import io.novafoundation.nova.feature_assets.presentation.send.TransferDraft
import io.novafoundation.nova.feature_assets.presentation.send.address_choose.SendAddressChooseFragment
import io.novafoundation.nova.feature_assets.presentation.send.asset_choose.SendAssetChooseFragment
import io.novafoundation.nova.feature_assets.presentation.send.confirm.SendConfirmFragment
import io.novafoundation.nova.feature_assets.presentation.send.create_contact.CreateContactFragment
import io.novafoundation.nova.feature_assets.presentation.send.fill.SendFillFragment
import io.novafoundation.nova.feature_assets.presentation.send_receive.SendReceiveFragment
import io.novafoundation.nova.feature_assets.presentation.send_receive.SendReceivePayload
import io.novafoundation.nova.feature_onboarding_impl.presentation.welcome.OnboardingFragment
import io.novafoundation.nova.splash.presentation.SplashFragment

object Screens {
    fun toSplashScreen() = FragmentScreen { SplashFragment.getNewInstance() }

    //Onboarding and account creation
    fun toOnboardingScreen(payload: AddAccountPayload = AddAccountPayload.MetaAccount(true)) =
        FragmentScreen { OnboardingFragment.getNewInstance(payload) }

    fun toSeedPromptScreen(payload: AddAccountPayload = AddAccountPayload.MetaAccount(true)) =
        FragmentScreen { SeedPromptFragment.getNewInstance(payload) }

    fun showSeedInfoDialog() =
        FragmentScreen(clearContainer = false) { SeedInfoFragment.getNewInstance() }

    fun showSeedWarningDialog() =
        FragmentScreen(clearContainer = false) { SeedWarningFragment.getNewInstance() }

    fun toSeedCreateScreen(payload: AddAccountPayload = AddAccountPayload.MetaAccount(true)) =
        FragmentScreen { SeedCreateFragment.getNewInstance(payload) }

    fun toSeedConfirmScreen(payload: AddAccountPayload) =
        FragmentScreen { SeedConfirmFragment.getNewInstance(payload) }

    fun toAccountImportScreen(payload: AddAccountPayload = AddAccountPayload.MetaAccount(true)) =
        FragmentScreen { AccountImportFragment.getNewInstance(payload) }

    fun toAccountImportInfoScreen() =
        FragmentScreen(clearContainer = false) { AccountImportInfoFragment.getNewInstance() }

    fun toCreatePasswordScreen(payload: AddAccountPayload) = FragmentScreen { CreatePasswordFragment.getNewInstance(payload) }
    fun toAccountCreatedScreen(payload: AddAccountPayload = AddAccountPayload.MetaAccount(true)) =
        FragmentScreen { AccountCreatedFragment.getNewInstance(payload) }

    // Login
    fun toLoginScreen() = FragmentScreen { LoginFragment.getNewInstance() }

    // Dashboard
    fun toMenu() = FragmentScreen { MenuNavDrawerFragment.getNewInstance() }
    fun toPasswordConfirm(data: ConfirmPayload) = FragmentScreen(clearContainer = false) { PasswordConfirmFragment.getNewInstance(data) }
    fun toEditField(data: EditFieldPayload) = FragmentScreen(clearContainer = false) { EditFieldFragment.getNewInstance(data) }
    fun toDashboardScreen() = FragmentScreen { DashboardFragment.getNewInstance() }

    fun toChainSettings() = FragmentScreen { ChainSettingsFragment.getNewInstance() }
    fun toSelectAccountScreen(data: SelectAccountPayload) =
        FragmentScreen(clearContainer = false) { SelectAccountFragment.getNewInstance(data) }


    // Assets
    fun toAssetChooseScreen() = FragmentScreen { AssetChooseFragment.getNewInstance() }
    fun toAssetReceiveScreen(receive: AssetPayload) = FragmentScreen { AssetReceiveFragment.getNewInstance(receive) }
    fun toAssetDetails(payload: AssetPayload) = FragmentScreen { AssetDetailsFragment.getNewInstance(payload) }
    fun toSendReceivePopupScreen(data: SendReceivePayload) = FragmentScreen(clearContainer = false) { SendReceiveFragment.getNewInstance(data) }
    fun toAssetReceiveChooserScreen(data: AssetReceivePayload) = FragmentScreen { AssetReceiveChooserFragment.getNewInstance(data) }

    //transactions
    fun toAssetTransactions(payload: AssetPayload) =
        FragmentScreen { AssetTransactionsFragment.getNewInstance(payload, true) }

    fun toTransferDetails(data: OperationParcelizeModel.Transfer) =
        FragmentScreen(clearContainer = false) { TransferDetailsFragment.getNewInstance(data) }

    //Send
    fun toSendAssetChooser() = FragmentScreen { SendAssetChooseFragment.getNewInstance() }
    fun toSendAddressChooser(data: AssetPayload) = FragmentScreen { SendAddressChooseFragment.getNewInstance(data) }
    fun toCreateContact(data: ContactPayload) = FragmentScreen(clearContainer = false) { CreateContactFragment.getNewInstance(data) }
    fun toSendFill(data: TransferDraft) = FragmentScreen { SendFillFragment.getNewInstance(data) }
    fun toSendConfirm(data: TransferDraft) = FragmentScreen { SendConfirmFragment.getNewInstance(data) }

//

//


//


//


//


}
