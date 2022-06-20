package io.novafoundation.nova.feature_account_impl.presentation.account_import

import android.net.Uri
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountInteractor
import io.novafoundation.nova.feature_account_api.domain.model.AddAccountType
import io.novafoundation.nova.feature_account_api.domain.model.ImportJsonMetaData
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload
import io.novafoundation.nova.feature_account_impl.domain.account.add.AddAccountInteractor
import io.novafoundation.nova.feature_account_impl.domain.account.advancedEncryption.AdvancedEncryptionInteractor
import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import io.novafoundation.nova.feature_account_impl.presentation.AdvancedEncryptionRequester
import io.novafoundation.nova.feature_account_impl.presentation.lastAdvancedEncryptionOrDefault
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class AccountImportPresenter @Inject constructor(
    private val router: AccountRouter,
    private val fileReader: FileReader,
    private val interactor: AccountInteractor,
    private val addAccountInteractor: AddAccountInteractor,
    private val advancedEncryptionCommunicator: AdvancedEncryptionRequester,
    private val advancedEncryptionInteractor: AdvancedEncryptionInteractor

) :
    MvpPresenter<AccountImportView>() {
    var mode = AccountImportWFragment.ImportMode.SEED
    var isAuth = true

    var password = ""
    var seed = ""
    var json = ""
    var name = ""

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.updateMode(mode)
        validate()
    }

    fun onTextChanged(text: String) {
        seed = text
        viewState.enableButton(seed.isNotEmpty())
    }

    fun onPasswordChanged(text: String) {
        password = text
        validate()
    }

    private fun validate() {
        val isValid = when (mode) {
            AccountImportWFragment.ImportMode.SEED -> {
                seed.isNotEmpty()
            }
            AccountImportWFragment.ImportMode.JSON -> {
                json.isNotEmpty() && password.isNotEmpty()

            }
            else -> {
                false
            }
        }
        viewState.enableButton(isValid)
    }

    fun onFileUriReceived(uri: Uri) {
        presenterScope.launch {
            json = fileReader.readFile(uri)!!
            jsonReceived(json)
            mode = AccountImportWFragment.ImportMode.JSON
            viewState.updateMode(mode)
            validate()
        }
    }

    private fun jsonReceived(newJson: String) {

        presenterScope.launch {
            val result = addAccountInteractor.extractJsonMetadata(newJson)

            if (result.isSuccess) {
                handleParsedImportData(result.getOrThrow())
            }
        }
    }

    private fun handleParsedImportData(importJsonData: ImportJsonMetaData) {
        name = importJsonData.name ?: ""
    }

    fun onHelpClick() {
        router.toAccountImportInfo()
    }

    fun onNextClick() {
        viewState.showProgress(true)
        presenterScope.launch {
            var result = import()
            if (result.isSuccess) {
                router.toCreatePassword()
            } else {
                viewState.showError(result.exceptionOrNull()?.toString() ?: "Unknown error")
            }
            viewState.showProgress(false)
        }

    }

    private suspend fun import(
    ): Result<Unit> {
        val advancedEncryption = advancedEncryptionCommunicator.lastAdvancedEncryptionOrDefault(AddAccountPayload.MetaAccount, advancedEncryptionInteractor)
        return when (mode) {
            AccountImportWFragment.ImportMode.SEED -> addAccountInteractor.importFromMnemonic(seed, advancedEncryption, AddAccountType.MetaAccount(name))
            AccountImportWFragment.ImportMode.JSON -> addAccountInteractor.importFromJson(json, password, AddAccountType.MetaAccount(name))
        }

    }

    fun onBackCommandClick() {
        if (mode == AccountImportWFragment.ImportMode.JSON) {
            mode = AccountImportWFragment.ImportMode.SEED
            viewState.updateMode(mode)
        } else {
            router.back()
        }
    }
}
