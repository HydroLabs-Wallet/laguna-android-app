package io.novafoundation.nova.feature_account_impl.presentation

import com.github.terrakok.cicerone.ResultListener
import io.novafoundation.nova.common.navigation.SecureRouter
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload
import io.novafoundation.nova.feature_account_api.presenatation.account.add.ImportAccountPayload
import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.create.SeedWord

interface AccountRouter : SecureRouter {
    fun setResult(key: String, data: Any)
    fun setResultListener(key: String, listener: ResultListener)
    fun toOnboardingScreen()

    fun toSeedCreate(isAuth: Boolean)
    fun toSeedInfo()
    fun toSeedWarning()
    fun toSeedConfirm(isAuth: Boolean, data: List<SeedWord>)
    fun toCreatePassword()
    fun toAccountComplete(isAuth: Boolean)

    fun toAccountImport(isAuth: Boolean)
    fun toAccountImportInfo()
    fun openMain()
    fun toLoginScreen()

    fun toDashboard()
//    fun openCreatePincode()

    fun openMnemonicScreen(accountName: String?, payload: AddAccountPayload)


    fun back()

    fun openNodes()

    fun openLanguages()

    fun openAddAccount(payload: AddAccountPayload)

    fun openAccountDetails(metaAccountId: Long)

    fun openEditAccounts()

    fun backToMainScreen()

    fun openNodeDetails(nodeId: Int)

    fun openAddNode()
//
//    @PinRequired
//    fun exportMnemonicAction(exportPayload: ExportPayload): DelayedNavigation
//
//    @PinRequired
//    fun exportSeedAction(exportPayload: ExportPayload): DelayedNavigation
//
//    @PinRequired
//    fun exportJsonPasswordAction(exportPayload: ExportPayload): DelayedNavigation
//
//    fun openExportJsonConfirm(payload: ExportJsonConfirmPayload)

//    fun openImportAccountScreen(payload: ImportAccountPayload)

    fun returnToWallet()

    fun finishExportFlow()

//    fun openChangePinCode()
}
