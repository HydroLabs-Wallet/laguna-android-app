package io.novafoundation.nova.app.root.navigation.screens

import com.github.terrakok.cicerone.androidx.FragmentScreen
import io.novafoundation.nova.app.root.presentation.dashboard.DashboardFragment
import io.novafoundation.nova.app.root.presentation.dashboard.сhain_setting.ChainSettingsFragment
import io.novafoundation.nova.feature_account_impl.presentation.account_import.AccountImportWFragment
import io.novafoundation.nova.feature_account_impl.presentation.account_import.info.AccountImportInfoFragment
import io.novafoundation.nova.feature_account_impl.presentation.login.LoginFragment
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.confirm.SeedConfirmFragment
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.create.SeedCreateFragment
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.create.SeedWord
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt.SeedPromptFragment
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt.info.SeedInfoFragment
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt.warning.SeedWarningFragment
import io.novafoundation.nova.feature_account_impl.presentation.onboarding_complete.AccountCreatedFragment
import io.novafoundation.nova.feature_account_impl.presentation.password.CreatePasswordFragment
import io.novafoundation.nova.feature_account_impl.presentation.select_account.SelectAccountFragment
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import io.novafoundation.nova.feature_assets.presentation.asset_choose.AssetChooseFragment
import io.novafoundation.nova.feature_assets.presentation.asset_details.AssetDetailsFragment
import io.novafoundation.nova.feature_assets.presentation.asset_receive.AssetReceiveFragment
import io.novafoundation.nova.feature_assets.presentation.asset_transactions.AssetTransactionsFragment
import io.novafoundation.nova.feature_assets.presentation.send_receive.SendReceiveFragment
import io.novafoundation.nova.feature_onboarding_impl.presentation.welcome.OnboardingFragment
import io.novafoundation.nova.splash.presentation.SplashFragment

object Screens {
    fun toSplashScreen() = FragmentScreen { SplashFragment.getNewInstance() }

    //Onboarding and account creation
    fun toOnboardingScreen(isAuth: Boolean = true) =
        FragmentScreen { OnboardingFragment.getNewInstance(isAuth) }

    fun toSeedPromptScreen(isAuth: Boolean = true) =
        FragmentScreen { SeedPromptFragment.getNewInstance(isAuth) }

    fun showSeedInfoDialog() =
        FragmentScreen(clearContainer = false) { SeedInfoFragment.getNewInstance() }

    fun showSeedWarningDialog() =
        FragmentScreen(clearContainer = false) { SeedWarningFragment.getNewInstance() }

    fun toSeedCreateScreen(isAuth: Boolean = true) =
        FragmentScreen { SeedCreateFragment.getNewInstance(isAuth) }

    fun toSeedConfirmScreen(isAuth: Boolean = true, data: List<SeedWord>) =
        FragmentScreen { SeedConfirmFragment.getNewInstance(isAuth, data) }

    fun toAccountImportScreen(isAuth: Boolean = true) =
        FragmentScreen { AccountImportWFragment.getNewInstance(isAuth) }

    fun toAccountImportInfoScreen() =
        FragmentScreen(clearContainer = false) { AccountImportInfoFragment.getNewInstance() }

    fun toCreatePasswordScreen() = FragmentScreen { CreatePasswordFragment.getNewInstance() }
    fun toAccountCreatedScreen(isAuth: Boolean = true) =
        FragmentScreen { AccountCreatedFragment.getNewInstance(isAuth) }

    // Login
    fun toLoginScreen() = FragmentScreen { LoginFragment.getNewInstance() }

    // Dashboard
    fun toDashboardScreen() = FragmentScreen { DashboardFragment.getNewInstance() }

    fun toChainSettings() = FragmentScreen { ChainSettingsFragment.getNewInstance() }
    fun toSelectAccountScreen() =
        FragmentScreen(clearContainer = false) { SelectAccountFragment.getNewInstance() }


    // Assets
    fun toAssetChooseScreen() = FragmentScreen { AssetChooseFragment.getNewInstance() }
    fun toAssetReceiveScreen(receive: AssetPayload) = FragmentScreen { AssetReceiveFragment.getNewInstance(receive) }
    fun toAssetDetails(payload: AssetPayload) =  FragmentScreen { AssetDetailsFragment.getNewInstance(payload) }
        fun toSendReceivePopupScreen() = FragmentScreen(clearContainer = false) { SendReceiveFragment.getNewInstance() }

    //transactions
        fun toAssetTransactions(payload: AssetPayload) =
        FragmentScreen { AssetTransactionsFragment.getNewInstance(payload, true) }
//

//

//    fun toTransactionDetails(data: TransactionPayload) =
//        FragmentScreen(clearContainer = false) { TransactionDetailsFragment.getNewInstance(data) }
//



//


//


}
