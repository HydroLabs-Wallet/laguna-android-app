package io.novafoundation.nova.app.root.navigation

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asFlow
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.github.terrakok.cicerone.ResultListener
import com.github.terrakok.cicerone.Router
import io.novafoundation.nova.app.R
import io.novafoundation.nova.app.root.navigation.screens.Screens
import io.novafoundation.nova.app.root.presentation.RootRouter
import io.novafoundation.nova.common.data.model.ConfirmPayload
import io.novafoundation.nova.common.data.model.EditFieldPayload
import io.novafoundation.nova.common.data.model.SelectAccountPayload
import io.novafoundation.nova.common.navigation.DelayedNavigation
import io.novafoundation.nova.common.utils.postToUiThread
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload
import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import io.novafoundation.nova.feature_assets.presentation.asset_receive_chooser.AssetReceivePayload
import io.novafoundation.nova.feature_assets.presentation.model.OperationParcelizeModel
import io.novafoundation.nova.feature_assets.presentation.receive.ReceiveFragment
import io.novafoundation.nova.feature_assets.presentation.send.ContactPayload
import io.novafoundation.nova.feature_assets.presentation.send.TransferDraft
import io.novafoundation.nova.feature_assets.presentation.send_receive.SendReceivePayload
import io.novafoundation.nova.feature_crowdloan_impl.presentation.CrowdloanRouter
import io.novafoundation.nova.feature_crowdloan_impl.presentation.contribute.confirm.ConfirmContributeFragment
import io.novafoundation.nova.feature_crowdloan_impl.presentation.contribute.confirm.parcel.ConfirmContributePayload
import io.novafoundation.nova.feature_crowdloan_impl.presentation.contribute.custom.BonusPayload
import io.novafoundation.nova.feature_crowdloan_impl.presentation.contribute.custom.CustomContributeFragment
import io.novafoundation.nova.feature_crowdloan_impl.presentation.contribute.custom.model.CustomContributePayload
import io.novafoundation.nova.feature_crowdloan_impl.presentation.contribute.custom.moonbeam.terms.MoonbeamCrowdloanTermsFragment
import io.novafoundation.nova.feature_crowdloan_impl.presentation.contribute.select.CrowdloanContributeFragment
import io.novafoundation.nova.feature_crowdloan_impl.presentation.contribute.select.parcel.ContributePayload
import io.novafoundation.nova.feature_onboarding_impl.OnboardingRouter
import io.novafoundation.nova.feature_onboarding_impl.presentation.welcome.WelcomeFragment
import io.novafoundation.nova.feature_staking_impl.presentation.StakingRouter
import io.novafoundation.nova.feature_staking_impl.presentation.payouts.confirm.ConfirmPayoutFragment
import io.novafoundation.nova.feature_staking_impl.presentation.payouts.confirm.model.ConfirmPayoutPayload
import io.novafoundation.nova.feature_staking_impl.presentation.payouts.detail.PayoutDetailsFragment
import io.novafoundation.nova.feature_staking_impl.presentation.payouts.model.PendingPayoutParcelable
import io.novafoundation.nova.feature_staking_impl.presentation.staking.bond.confirm.ConfirmBondMoreFragment
import io.novafoundation.nova.feature_staking_impl.presentation.staking.bond.confirm.ConfirmBondMorePayload
import io.novafoundation.nova.feature_staking_impl.presentation.staking.bond.select.SelectBondMoreFragment
import io.novafoundation.nova.feature_staking_impl.presentation.staking.bond.select.SelectBondMorePayload
import io.novafoundation.nova.feature_staking_impl.presentation.staking.controller.confirm.ConfirmSetControllerFragment
import io.novafoundation.nova.feature_staking_impl.presentation.staking.controller.confirm.ConfirmSetControllerPayload
import io.novafoundation.nova.feature_staking_impl.presentation.staking.main.model.StakingStoryModel
import io.novafoundation.nova.feature_staking_impl.presentation.staking.rebond.confirm.ConfirmRebondFragment
import io.novafoundation.nova.feature_staking_impl.presentation.staking.rebond.confirm.ConfirmRebondPayload
import io.novafoundation.nova.feature_staking_impl.presentation.staking.redeem.RedeemFragment
import io.novafoundation.nova.feature_staking_impl.presentation.staking.redeem.RedeemPayload
import io.novafoundation.nova.feature_staking_impl.presentation.staking.rewardDestination.confirm.ConfirmRewardDestinationFragment
import io.novafoundation.nova.feature_staking_impl.presentation.staking.rewardDestination.confirm.parcel.ConfirmRewardDestinationPayload
import io.novafoundation.nova.feature_staking_impl.presentation.staking.unbond.confirm.ConfirmUnbondFragment
import io.novafoundation.nova.feature_staking_impl.presentation.staking.unbond.confirm.ConfirmUnbondPayload
import io.novafoundation.nova.feature_staking_impl.presentation.story.StoryFragment
import io.novafoundation.nova.feature_staking_impl.presentation.validators.details.ValidatorDetailsFragment
import io.novafoundation.nova.feature_staking_impl.presentation.validators.parcel.ValidatorDetailsParcelModel
import io.novafoundation.nova.splash.SplashRouter
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.flow.Flow

@Parcelize
class NavComponentDelayedNavigation(val globalActionId: Int, val extras: Bundle? = null) : DelayedNavigation

class Navigator(
    private val navigationHolder: NavigationHolderOld,
    private val router: Router
) :
    SplashRouter,
    OnboardingRouter,
    AccountRouter,
    WalletRouter,
    RootRouter,
    StakingRouter,
    CrowdloanRouter {

    private val navController: NavController?
        get() = navigationHolder.navController

    override fun setResult(key: String, data: Any) {
        router.sendResult(key, data)
    }

    override fun setResultListener(key: String, listener: ResultListener) {
        router.setResultListener(key, listener)
    }

    override fun lockApp() {
        router.newRootScreen(Screens.toLoginScreen())
    }

    override fun toPasswordConfirm(data: ConfirmPayload) {
        router.navigateTo(Screens.toPasswordConfirm(data))


    }

    override fun toEditField(data: EditFieldPayload) {
        router.navigateTo(Screens.toEditField(data))
    }

    // SplashScreen
    override fun toSplashScreen() {
        router.navigateTo(Screens.toSplashScreen())
    }

    //Onboarding and account creation
    override fun toSeedPromptScreen(payload: AddAccountPayload) {
        router.navigateTo(Screens.toSeedPromptScreen(payload))
    }

    override fun toSeedCreate(payload: AddAccountPayload) {
        router.navigateTo(Screens.toSeedCreateScreen(payload))
    }

    override fun toSeedInfo() {
        router.navigateTo(Screens.showSeedInfoDialog())
    }

    override fun toSeedWarning() {
        router.navigateTo(Screens.showSeedWarningDialog())

    }

    override fun toAccountImport(payload: AddAccountPayload) {
        router.navigateTo(Screens.toAccountImportScreen(payload))
    }

    override fun toAccountImportInfo() {
        router.navigateTo(Screens.toAccountImportInfoScreen())
    }


    override fun toCreatePassword(payload: AddAccountPayload) {
        router.navigateTo(Screens.toCreatePasswordScreen(payload))
    }

    override fun toAccountComplete(payload: AddAccountPayload) {
        router.navigateTo(Screens.toAccountCreatedScreen(payload))
    }

    override fun toSeedConfirm(payload: AddAccountPayload) {
        router.navigateTo(Screens.toSeedConfirmScreen(payload))
    }

    override fun toOnboardingScreen() {
        router.navigateTo(Screens.toOnboardingScreen(AddAccountPayload.MetaAccount(true)))
    }

    // Login
    override fun toLoginScreen() {
        router.navigateTo(Screens.toLoginScreen())
    }

    // Dashboard
    override fun backToDashBoard() {
        router.backTo(Screens.toDashboardScreen())
    }

    override fun toDashboard() {
        router.navigateTo(Screens.toDashboardScreen())
    }

    override fun toMenu() {
        router.navigateTo(Screens.toMenu())
    }

    override fun toChangePassword() {
        router.navigateTo(Screens.toChangePassword())
    }

    override fun toChangeAvatar() {
        router.navigateTo(Screens.toChangeAvatar())
    }

    override fun toChainsSettings() {
        router.navigateTo(Screens.toChainSettings())
    }

    override fun toSelectAccount(data: SelectAccountPayload) {
        router.navigateTo(Screens.toSelectAccountScreen(data))
    }

    // assets

    override fun toAssetSelectionToReceive() {
        router.navigateTo(Screens.toAssetChooseScreen())
    }

    override fun toAssetReceive(assetPayload: AssetPayload) {
        router.navigateTo(Screens.toAssetReceiveScreen(assetPayload))
    }

    override fun toAssetReceiveChooser(payload: AssetReceivePayload) {
        router.navigateTo(Screens.toAssetReceiveChooserScreen(payload))
    }

    override fun toAssetDetails(data: AssetPayload) {
        router.navigateTo(Screens.toAssetDetails(data))
    }

    override fun showSendReceiveDialog(data: SendReceivePayload) {
        router.navigateTo(Screens.toSendReceivePopupScreen(data))
    }

    // transactions
    override fun toAssetTransaction(data: AssetPayload) {
        router.navigateTo(Screens.toAssetTransactions(data))
    }

    override fun toTransferDetails(data: OperationParcelizeModel.Transfer) {
        router.navigateTo(Screens.toTransferDetails(data))
    }

    //send
    override fun toSendAssetChooser() {
        router.navigateTo(Screens.toSendAssetChooser())
    }

    override fun toSendAddressChooser(data: AssetPayload) {
        router.navigateTo(Screens.toSendAddressChooser(data))
    }

    override fun toSendFill(data: TransferDraft) {
        router.navigateTo(Screens.toSendFill(data))
    }

    override fun toSendConfirm(data: TransferDraft) {
        router.navigateTo(Screens.toSendConfirm(data))
    }

    override fun toCreateContact(data: ContactPayload) {
        router.navigateTo(Screens.toCreateContact(data))
    }

    override fun openInitialCheckPincode() {
//        val action = PinCodeAction.Check(NavComponentDelayedNavigation(R.id.action_open_main), ToolbarConfiguration())
//        val bundle = PincodeFragment.getPinCodeBundle(action)
//        navController?.navigate(R.id.action_splash_to_pin, bundle)
    }

    override fun openCreateAccount(addAccountPayload: AddAccountPayload.MetaAccount) {
//        navController?.navigate(R.id.action_welcomeFragment_to_createAccountFragment, CreateAccountFragment.getBundle(addAccountPayload))
    }

    override fun openMain() {
        navController?.navigate(R.id.action_open_main)
    }

    override fun openAfterPinCode(delayedNavigation: DelayedNavigation) {
        require(delayedNavigation is NavComponentDelayedNavigation)

        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.pincodeFragment, true)
            .build()

        navController?.navigate(delayedNavigation.globalActionId, delayedNavigation.extras, navOptions)
    }

//    override fun openCreatePincode() {
//        val bundle = buildCreatePinBundle()
//
//        when (navController?.currentDestination?.id) {
//            R.id.splashFragment -> navController?.navigate(R.id.action_splash_to_pin, bundle)
//            R.id.importAccountFragment -> navController?.navigate(R.id.action_importAccountFragment_to_pincodeFragment, bundle)
//            R.id.confirmMnemonicFragment -> navController?.navigate(R.id.action_confirmMnemonicFragment_to_pincodeFragment, bundle)
//        }
//    }


//    override fun openImportAccountScreen(payload: ImportAccountPayload) {
//        val destination = when (val currentDestinationId = navController?.currentDestination?.id) {
//            R.id.welcomeFragment -> R.id.action_welcomeFragment_to_import_nav_graph
//            R.id.accountDetailsFragment -> R.id.action_accountDetailsFragment_to_import_nav_graph
//            else -> throw IllegalArgumentException("Unknown current destination to open import account screen: $currentDestinationId")
//        }
//
//        navController?.navigate(destination, ImportAccountFragment.getBundle(payload))
//    }

    override fun openMnemonicScreen(accountName: String?, addAccountPayload: AddAccountPayload) {
//        val destination = when (val currentDestinationId = navController?.currentDestination?.id) {
//            R.id.welcomeFragment -> R.id.action_welcomeFragment_to_mnemonic_nav_graph
//            R.id.createAccountFragment -> R.id.action_createAccountFragment_to_mnemonic_nav_graph
//            R.id.accountDetailsFragment -> R.id.action_accountDetailsFragment_to_mnemonic_nav_graph
//            else -> throw IllegalArgumentException("Unknown current destination to open mnemonic screen: $currentDestinationId")
//        }
//
//        val payload = BackupMnemonicPayload.Create(accountName, addAccountPayload)
//        navController?.navigate(destination, BackupMnemonicFragment.getBundle(payload))
    }

    override fun openSetupStaking() {
        navController?.navigate(R.id.action_mainFragment_to_setupStakingFragment)
    }

    override fun openStartChangeValidators() {
        navController?.navigate(R.id.openStartChangeValidatorsFragment)
    }

    override fun openStory(story: StakingStoryModel) {
        navController?.navigate(R.id.open_staking_story, StoryFragment.getBundle(story))
    }

    override fun openPayouts() {
        navController?.navigate(R.id.action_mainFragment_to_payoutsListFragment)
    }

    override fun openPayoutDetails(payout: PendingPayoutParcelable) {
        navController?.navigate(R.id.action_payoutsListFragment_to_payoutDetailsFragment, PayoutDetailsFragment.getBundle(payout))
    }

    override fun openConfirmPayout(payload: ConfirmPayoutPayload) {
        navController?.navigate(R.id.action_open_confirm_payout, ConfirmPayoutFragment.getBundle(payload))
    }

    override fun openBondMore() {
        navController?.navigate(R.id.action_open_selectBondMoreFragment, SelectBondMoreFragment.getBundle(SelectBondMorePayload()))
    }

    override fun openConfirmBondMore(payload: ConfirmBondMorePayload) {
        navController?.navigate(R.id.action_selectBondMoreFragment_to_confirmBondMoreFragment, ConfirmBondMoreFragment.getBundle(payload))
    }

    override fun openSelectUnbond() {
        navController?.navigate(R.id.action_mainFragment_to_selectUnbondFragment)
    }

    override fun openConfirmUnbond(payload: ConfirmUnbondPayload) {
        navController?.navigate(R.id.action_selectUnbondFragment_to_confirmUnbondFragment, ConfirmUnbondFragment.getBundle(payload))
    }

    override fun openRedeem() {
        navController?.navigate(R.id.action_open_redeemFragment, RedeemFragment.getBundle(RedeemPayload()))
    }

    override fun openConfirmRebond(payload: ConfirmRebondPayload) {
        navController?.navigate(R.id.action_open_confirm_rebond, ConfirmRebondFragment.getBundle(payload))
    }

    override fun openContribute(payload: ContributePayload) {
        val bundle = CrowdloanContributeFragment.getBundle(payload)

        when (navController?.currentDestination?.id) {
            R.id.mainFragment -> navController?.navigate(R.id.action_mainFragment_to_crowdloanContributeFragment, bundle)
            R.id.moonbeamCrowdloanTermsFragment -> navController?.navigate(R.id.action_moonbeamCrowdloanTermsFragment_to_crowdloanContributeFragment, bundle)
        }
    }

    override val customBonusFlow: Flow<BonusPayload?>
        get() = navController!!.currentBackStackEntry!!.savedStateHandle
            .getLiveData<BonusPayload?>(CrowdloanContributeFragment.KEY_BONUS_LIVE_DATA)
            .asFlow()

    override val latestCustomBonus: BonusPayload?
        get() = navController!!.currentBackStackEntry!!.savedStateHandle
            .get(CrowdloanContributeFragment.KEY_BONUS_LIVE_DATA)

    override fun openCustomContribute(payload: CustomContributePayload) {
        navController?.navigate(R.id.action_crowdloanContributeFragment_to_customContributeFragment, CustomContributeFragment.getBundle(payload))
    }

    override fun setCustomBonus(payload: BonusPayload) {
        navController!!.previousBackStackEntry!!.savedStateHandle.set(CrowdloanContributeFragment.KEY_BONUS_LIVE_DATA, payload)
    }

    override fun openConfirmContribute(payload: ConfirmContributePayload) {
        navController?.navigate(R.id.action_crowdloanContributeFragment_to_confirmContributeFragment, ConfirmContributeFragment.getBundle(payload))
    }

    override fun back() {
        router.exit()
    }

    override fun openCustomRebond() {
        navController?.navigate(R.id.action_mainFragment_to_customRebondFragment)
    }

    override fun openCurrentValidators() {
        navController?.navigate(R.id.action_mainFragment_to_currentValidatorsFragment)
    }

    override fun returnToCurrentValidators() {
        navController?.navigate(R.id.action_confirmStakingFragment_back_to_currentValidatorsFragment)
    }

    override fun openChangeRewardDestination() {
        navController?.navigate(R.id.action_mainFragment_to_selectRewardDestinationFragment)
    }

    override fun openConfirmRewardDestination(payload: ConfirmRewardDestinationPayload) {
        navController?.navigate(
            R.id.action_selectRewardDestinationFragment_to_confirmRewardDestinationFragment,
            ConfirmRewardDestinationFragment.getBundle(payload)
        )
    }

    override val currentStackEntryLifecycle: Lifecycle
        get() = navController!!.currentBackStackEntry!!.lifecycle

    override fun openControllerAccount() {
        navController?.navigate(R.id.action_stakingBalanceFragment_to_setControllerAccountFragment)
    }

    override fun openConfirmSetController(payload: ConfirmSetControllerPayload) {
        navController?.navigate(
            R.id.action_stakingSetControllerAccountFragment_to_confirmSetControllerAccountFragment,
            ConfirmSetControllerFragment.getBundle(payload)
        )
    }

    override fun openRecommendedValidators() {
        navController?.navigate(R.id.action_startChangeValidatorsFragment_to_recommendedValidatorsFragment)
    }

    override fun openSelectCustomValidators() {
        navController?.navigate(R.id.action_startChangeValidatorsFragment_to_selectCustomValidatorsFragment)
    }

    override fun openCustomValidatorsSettings() {
        navController?.navigate(R.id.action_selectCustomValidatorsFragment_to_settingsCustomValidatorsFragment)
    }

    override fun openSearchCustomValidators() {
        navController?.navigate(R.id.action_selectCustomValidatorsFragment_to_searchCustomValidatorsFragment)
    }

    override fun openReviewCustomValidators() {
        navController?.navigate(R.id.action_selectCustomValidatorsFragment_to_reviewCustomValidatorsFragment)
    }

    override fun openConfirmStaking() {
        navController?.navigate(R.id.openConfirmStakingFragment)
    }

    override fun openConfirmNominations() {
        navController?.navigate(R.id.action_confirmStakingFragment_to_confirmNominationsFragment)
    }

    override fun returnToMain() {
        navController?.navigate(R.id.back_to_main)
    }

    override fun openMoonbeamFlow(payload: ContributePayload) {
        navController?.navigate(R.id.action_mainFragment_to_moonbeamCrowdloanTermsFragment, MoonbeamCrowdloanTermsFragment.getBundle(payload))
    }

    override fun openValidatorDetails(validatorDetails: ValidatorDetailsParcelModel) {
        navController?.navigate(R.id.open_validator_details, ValidatorDetailsFragment.getBundle(validatorDetails))
    }

    override fun openFilter() {
        navController?.navigate(R.id.action_mainFragment_to_filterFragment)
    }

    override fun openSend(assetPayload: AssetPayload, initialRecipientAddress: String?) {
//        val extras = SelectSendFragment.getBundle(assetPayload, initialRecipientAddress)
//
//        navController?.navigate(R.id.action_open_send, extras)
    }

    override fun openConfirmTransfer(transferDraft: TransferDraft) {
//        val bundle = ConfirmSendFragment.getBundle(transferDraft)
//
//        navController?.navigate(R.id.action_chooseAmountFragment_to_confirmTransferFragment, bundle)
    }

    override fun finishSendFlow() {
        navController?.navigate(R.id.finish_send_flow)
    }


//    override fun openAccounts(accountChosenNavDirection: AccountChosenNavDirection) {
//        navController?.navigate(R.id.action_open_accounts, AccountListFragment.getBundle(accountChosenNavDirection))
//    }

    override fun openNodes() {
        navController?.navigate(R.id.action_mainFragment_to_nodesFragment)
    }

    override fun openLanguages() {
        navController?.navigate(R.id.action_mainFragment_to_languagesFragment)
    }

    override fun openChangeAccount() {
//        openAccounts(AccountChosenNavDirection.BACK)
    }

    override fun openReceive(assetPayload: AssetPayload) {
        navController?.navigate(R.id.action_open_receive, ReceiveFragment.getBundle(assetPayload))
    }

    override fun openAssetFilters() {
        navController?.navigate(R.id.action_mainFragment_to_assetFiltersFragment)
    }

    override fun openNfts() {
        navController?.navigate(R.id.action_mainFragment_to_nfts_nav_graph)
    }

    override fun returnToWallet() {
        // to achieve smooth animation
        postToUiThread {
            navController?.navigate(R.id.action_return_to_wallet)
        }
    }

    override fun openAccountDetails(metaAccountId: Long) {
//        val extras = AccountDetailsFragment.getBundle(metaAccountId)

//        navController?.navigate(R.id.action_open_account_details, extras)
    }

    override fun openEditAccounts() {
        navController?.navigate(R.id.action_accountsFragment_to_editAccountsFragment)
    }

    override fun backToMainScreen() {
        navController?.navigate(R.id.action_editAccountsFragment_to_mainFragment)
    }

    override fun openNodeDetails(nodeId: Int) {
//        navController?.navigate(R.id.action_nodesFragment_to_nodeDetailsFragment, NodeDetailsFragment.getBundle(nodeId))
    }

    override fun openAssetDetails(assetPayload: AssetPayload) {
//        val bundle = BalanceDetailFragment.getBundle(assetPayload)
//
//        navController?.navigate(R.id.action_mainFragment_to_balanceDetailFragment, bundle)
    }

    override fun openAddNode() {
        navController?.navigate(R.id.action_nodesFragment_to_addNodeFragment)
    }

    override fun openAddAccount(payload: AddAccountPayload) {
        navController?.navigate(R.id.action_open_onboarding, WelcomeFragment.bundle(payload))
    }

    override fun openUserContributions() {
        navController?.navigate(R.id.action_mainFragment_to_userContributionsFragment)
    }

//    override fun exportMnemonicAction(exportPayload: ExportPayload): DelayedNavigation {
////        val payload = BackupMnemonicPayload.Confirm(exportPayload.chainId, exportPayload.metaId)
////        val extras = BackupMnemonicFragment.getBundle(payload)
//
//        return NavComponentDelayedNavigation(R.id.action_open_mnemonic_nav_graph, bundleOf())
//    }

//    override fun exportSeedAction(exportPayload: ExportPayload): DelayedNavigation {
//        val extras = ExportSeedFragment.getBundle(exportPayload)
//
//        return NavComponentDelayedNavigation(R.id.action_export_seed, extras)
//    }
//
//    override fun exportJsonPasswordAction(exportPayload: ExportPayload): DelayedNavigation {
//        val extras = ExportJsonPasswordFragment.getBundle(exportPayload)
//
//        return NavComponentDelayedNavigation(R.id.action_export_json, extras)
//    }
//
//    override fun openExportJsonConfirm(payload: ExportJsonConfirmPayload) {
//        val extras = ExportJsonConfirmFragment.getBundle(payload)
//
//        navController?.navigate(R.id.action_exportJsonPasswordFragment_to_exportJsonConfirmFragment, extras)
//    }

    override fun finishExportFlow() {
        navController?.navigate(R.id.finish_export_flow)
    }

//    override fun openChangePinCode() {
//        val action = PinCodeAction.Change
//        val bundle = PincodeFragment.getPinCodeBundle(action)
//        navController?.navigate(R.id.action_mainFragment_to_pinCodeFragment, bundle)
//    }

//    override fun withPinCodeCheckRequired(
//        delayedNavigation: DelayedNavigation,
//        createMode: Boolean,
//        pinCodeTitleRes: Int?,
//    ) {
//        val action = if (createMode) {
//            PinCodeAction.Create(delayedNavigation)
//        } else {
//            PinCodeAction.Check(delayedNavigation, ToolbarConfiguration(pinCodeTitleRes, true))
//        }
//
//        val extras = PincodeFragment.getPinCodeBundle(action)
//
//        navController?.navigate(R.id.open_pincode_check, extras)
//    }
//
//    private fun buildCreatePinBundle(): Bundle {
//        val delayedNavigation = NavComponentDelayedNavigation(R.id.action_open_main)
//        val action = PinCodeAction.Create(delayedNavigation)
//        return PincodeFragment.getPinCodeBundle(action)
//    }
}
