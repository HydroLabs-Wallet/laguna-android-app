package io.novafoundation.nova.feature_onboarding_impl

import com.github.terrakok.cicerone.ResultListener
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload
import io.novafoundation.nova.feature_account_api.presenatation.account.add.ImportAccountPayload

interface OnboardingRouter {
    fun setResult(key:String,data:Any)
    fun setResultListener(key:String, listener: ResultListener)
    fun toSeedPromptScreen(isAuth: Boolean)

    fun openCreateAccount(addAccountPayload: AddAccountPayload.MetaAccount)

    fun openMnemonicScreen(accountName: String?, payload: AddAccountPayload)
    fun toAccountImport(isAuth: Boolean)

//    fun openImportAccountScreen(payload: ImportAccountPayload)

    fun back()
}
