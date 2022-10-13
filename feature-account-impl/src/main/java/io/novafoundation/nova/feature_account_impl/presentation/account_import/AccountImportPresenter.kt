package io.novafoundation.nova.feature_account_impl.presentation.account_import

import android.net.Uri
import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountAlreadyExistsException
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountInteractor
import io.novafoundation.nova.feature_account_api.domain.model.AddAccountType
import io.novafoundation.nova.feature_account_api.domain.model.ImportJsonMetaData
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload
import io.novafoundation.nova.feature_account_impl.R
import io.novafoundation.nova.feature_account_impl.domain.account.add.AddAccountInteractor
import io.novafoundation.nova.feature_account_impl.domain.account.advancedEncryption.AdvancedEncryptionInteractor
import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import io.novafoundation.nova.feature_account_impl.presentation.AdvancedEncryptionRequester
import io.novafoundation.nova.feature_account_impl.presentation.lastAdvancedEncryptionOrDefault
import jp.co.soramitsu.fearless_utils.encrypt.json.JsonSeedDecodingException
import jp.co.soramitsu.fearless_utils.exceptions.Bip39Exception
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class AccountImportPresenter @Inject constructor(
    private val router: AccountRouter,
    private val fileReader: FileReader,
    private val interactor: AccountInteractor,
    private val addAccountInteractor: AddAccountInteractor,
    private val advancedEncryptionCommunicator: AdvancedEncryptionRequester,
    private val advancedEncryptionInteractor: AdvancedEncryptionInteractor,
    private val payload: AddAccountPayload,
    private val resourceManager: ResourceManager

) :
    BasePresenter<AccountImportView>() {
    var mode = AccountImportFragment.ImportMode.SEED

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
            AccountImportFragment.ImportMode.SEED -> {
                seed.isNotEmpty()
            }
            AccountImportFragment.ImportMode.JSON -> {
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
            mode = AccountImportFragment.ImportMode.JSON
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
        if (mode == AccountImportFragment.ImportMode.SEED) {
            val words = seed.split(" ")


            if (words.size != 12 && words.size != 24) {
                showError(resourceManager.getString(R.string.invalid_word_count))
                return
            }
        }
        viewState.showProgress(true)
        presenterScope.launch {

            var result = import()
            if (result.isSuccess) {
                if (interactor.isCodeSet()) {
                    router.toAddToExistingComplete(payload)
                } else {
                    router.toCreatePassword(payload)
                }

            } else {
                val throwable = result.exceptionOrNull()
                val errorMessage = when (throwable) {
                    is AccountAlreadyExistsException->{
                        resourceManager.getString(R.string.account_add_already_exists_message)
                    }
                    is JsonSeedDecodingException.InvalidJsonException -> {
                        resourceManager.getString(R.string.invalid_json)
                    }
                    is JsonSeedDecodingException.IncorrectPasswordException -> {
                        resourceManager.getString(R.string.invalid_password)
                    }
                    is JsonSeedDecodingException.UnsupportedEncryptionTypeException -> {
                        resourceManager.getString(R.string.invalid_encryption)
                    }
                    is Bip39Exception -> resourceManager.getString(R.string.invalid_blockchain_address)

                    else -> {
                        result.exceptionOrNull()?.toString() ?: resourceManager.getString(R.string.unknown_error)
                    }
                }
                onBackCommandClick()
                showError(errorMessage)
            }
            viewState.showProgress(false)
        }

    }

    private suspend fun import(
    ): Result<Unit> {
        val advancedEncryption = advancedEncryptionCommunicator.lastAdvancedEncryptionOrDefault(payload, advancedEncryptionInteractor)

        return when (mode) {
            AccountImportFragment.ImportMode.SEED -> addAccountInteractor.importFromMnemonic(seed, advancedEncryption, AddAccountType.MetaAccount(name))
            AccountImportFragment.ImportMode.JSON -> addAccountInteractor.importFromJson(json, password, AddAccountType.MetaAccount(name))
        }

    }

    fun onBackCommandClick() {
        if (mode == AccountImportFragment.ImportMode.JSON) {
            mode = AccountImportFragment.ImportMode.SEED
            viewState.updateMode(mode)
        } else {
            router.back()
        }
    }
}
