package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.prompt

import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountInteractor
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload
import io.novafoundation.nova.feature_account_impl.R
import io.novafoundation.nova.feature_account_impl.data.mappers.mapAddAccountPayloadToAddAccountType
import io.novafoundation.nova.feature_account_impl.data.mappers.mapAdvancedEncryptionResponseToAdvancedEncryption
import io.novafoundation.nova.feature_account_impl.data.mappers.mapOptionalNameToNameChooserState
import io.novafoundation.nova.feature_account_impl.domain.account.add.AddAccountInteractor
import io.novafoundation.nova.feature_account_impl.domain.account.advancedEncryption.AdvancedEncryptionInteractor
import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import io.novafoundation.nova.feature_account_impl.presentation.AdvancedEncryptionRequester
import io.novafoundation.nova.feature_account_impl.presentation.lastResponseOrDefault
import jp.co.soramitsu.fearless_utils.encrypt.junction.BIP32JunctionDecoder
import jp.co.soramitsu.fearless_utils.encrypt.junction.JunctionDecoder
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class SeedPromptPresenter @Inject constructor(
    private val router: AccountRouter,
    private val payload: AddAccountPayload,
    private val interactor: AccountInteractor,
    private val advancedEncryptionRequester: AdvancedEncryptionRequester,
    private val advancedEncryptionInteractor: AdvancedEncryptionInteractor,
    private val addAccountInteractor: AddAccountInteractor,
    private val resourceManager: ResourceManager,

    ) :
    BasePresenter<SeedPromptView>() {

    fun onCreateClick() {
        router.toSeedCreate(payload)
    }

    fun onInfoClick() {
        router.toSeedInfo()
    }

    fun onSkipClick() {
        router.setResultListener(SeedPromptFragment.RESULT_PROMPT) { data ->
            val isAuth = when (payload) {
                is AddAccountPayload.ChainAccount -> false
                is AddAccountPayload.MetaAccount -> payload.isAuth
            }
            when (data as SeedPromptFragment.ResultPrompt) {
                SeedPromptFragment.ResultPrompt.SKIP -> {
                    presenterScope.launch {
                        val seed = interactor.generateMnemonic()
                        val mnemonicString = seed.wordList.joinToString(" ")
                        val advancedEncryptionResponse = advancedEncryptionRequester.lastResponseOrDefault(payload, advancedEncryptionInteractor)
                        val accountNameState = mapOptionalNameToNameChooserState("")
                        val addAccountType = mapAddAccountPayloadToAddAccountType(payload, accountNameState)
                        val advancedEncryption = mapAdvancedEncryptionResponseToAdvancedEncryption(advancedEncryptionResponse)

                        addAccountInteractor.createAccount(mnemonicString, advancedEncryption, addAccountType)
                            .onSuccess {
                                if (interactor.isCodeSet()) {
                                    router.toDashboard()

                                } else {
                                    router.toCreatePassword(payload)
                                }

                            }
                            .onFailure(::showAccountCreationError)


                    }

                }
                SeedPromptFragment.ResultPrompt.SECURE -> {
                    router.toSeedCreate(payload)
                }
            }
        }
        router.toSeedWarning()
    }

    private fun showAccountCreationError(throwable: Throwable) {
        val (title, message) = when (throwable) {
            is JunctionDecoder.DecodingError, is BIP32JunctionDecoder.DecodingError -> {
                resourceManager.getString(R.string.account_invalid_derivation_path_title) to
                    resourceManager.getString(R.string.account_invalid_derivation_path_message_v2_2_0)
            }
            else -> {
                resourceManager.getString(R.string.common_error_general_title) to
                    resourceManager.getString(R.string.common_undefined_error_message)
            }
        }

        viewState.showError(message)
    }

    fun onBackCommandClick() {
        router.back()
    }
}
