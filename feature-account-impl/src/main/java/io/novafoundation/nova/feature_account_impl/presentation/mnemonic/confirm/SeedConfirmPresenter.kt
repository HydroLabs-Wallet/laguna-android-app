package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.confirm

import androidx.room.PrimaryKey
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
import io.novafoundation.nova.feature_account_api.presenatation.account.add.SeedWord
import jp.co.soramitsu.fearless_utils.encrypt.junction.BIP32JunctionDecoder
import jp.co.soramitsu.fearless_utils.encrypt.junction.JunctionDecoder
import kotlinx.coroutines.launch
import moxy.InjectViewState
import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.feature_account_api.presenatation.account.add.getSeedWord
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class SeedConfirmPresenter  @Inject constructor(
    private val router: AccountRouter,
    private val interactor: AccountInteractor,
    private val addAccountInteractor: AddAccountInteractor,
    private val resourceManager: ResourceManager,
    private val advancedEncryptionInteractor: AdvancedEncryptionInteractor,
    private val advancedEncryptionRequester: AdvancedEncryptionRequester,
    private val payload: AddAccountPayload

) :
    BasePresenter<SeedConfirmView>() {
    var shuffledSeedList = mutableListOf<SeedWord>()
    private lateinit var initSelection: List<SeedWord>
    private var selection: MutableList<SeedWord> = mutableListOf()
    var isAuth = true

    // cool segment degree armor grocery rack busines drill trick chef screen forget
    var focusedItem = 0
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        shuffledSeedList.clear()

        shuffledSeedList.addAll(payload.getSeedWord())
        shuffledSeedList.shuffle()
        createInitialSelection()
        viewState.setList(shuffledSeedList)
        viewState.setSelection(selection)
    }

    private fun createInitialSelection() {
        val array = resourceManager.getStringArray(R.array.seed_selection)
        val prompts = array.mapIndexed { index, s ->
            SeedWord(number = index + 1, word = s, isVerify = true)
        }.toMutableList()
        prompts.shuffle()
        var prompts2 = prompts.take(3).toMutableList()
        prompts2 = prompts2.sortedBy { it.number }.toMutableList()
        prompts2[0] = prompts2[0].copy(isFocused = true)
        initSelection = prompts2
        selection.clear()
        selection.addAll(initSelection)
    }

    /**
     * dentist, turtle, erupt, hub, account, sting, club, napkin, slim, harbor, dust, repair
     */
    fun onSelectionChanged(newFocus: Int) {
        selection = selection.mapIndexed { index, seedWord ->
            if (index == newFocus) {
                seedWord.copy(isFocused = true)
            } else {
                seedWord.copy(isFocused = false)

            }
        }.toMutableList()
        focusedItem = newFocus
        viewState.setSelection(selection)
    }

    fun onSeedListItemClick(item: SeedWord) {
        if (focusedItem == -1) {

        } else {
            val prevSelection = selection[focusedItem]

            var prevWord = shuffledSeedList.first { it.number == prevSelection.number }!!
            selection[focusedItem] =
                item.copy(isVerify = true, isChecked = true, isFocused = false)
            shuffledSeedList = shuffledSeedList.map {
                if (it.number == prevSelection.number) prevWord.copy(
                    isFocused = false,
                    isChecked = false,
                    isVerify = false
                ) else it
            }.toMutableList()
            shuffledSeedList = shuffledSeedList.map {
                if (it.number == item.number) {
                    item.copy(isFocused = false, isChecked = true, isVerify = false)
                } else it
            }.toMutableList()
            viewState.setList(shuffledSeedList)
            var foundNextFocus = false
            for (i in 0 until 3) {
                if (!selection[i].isChecked) {
                    foundNextFocus = true
                    focusedItem = i
                    break
                }
            }
            if (!foundNextFocus) {
                if (validate()) {
                    focusedItem = -1
                    viewState.enableButton(true)
                } else {
                    focusedItem = 0
                    showError("Seed words does not match. Please, try again")
                    selection.clear()
                    selection.addAll(initSelection)
                    shuffledSeedList = shuffledSeedList
                        .map { if (it.isChecked) it.copy(isChecked = false) else it }
                        .toMutableList()
                    viewState.setList(shuffledSeedList)
                }
            } else {
                selection[focusedItem] = selection[focusedItem].copy(isFocused = true)
            }
            viewState.setSelection(selection)

        }
    }

    fun validate(): Boolean {
        initSelection.forEachIndexed { index, seedWord ->
            if (selection[index].number != seedWord.number) {
                return false
            }
        }
        return true
    }

    fun onNextClick() {
        createAccount()
    }

    private fun createAccount() {

        presenterScope.launch {
            val mnemonicString = payload.getSeedWord().joinToString(" ") { it.word }
            val advancedEncryptionResponse = advancedEncryptionRequester.lastResponseOrDefault(payload, advancedEncryptionInteractor)
            val accountNameState = mapOptionalNameToNameChooserState("")
            val addAccountType = mapAddAccountPayloadToAddAccountType(payload, accountNameState)
            val advancedEncryption = mapAdvancedEncryptionResponseToAdvancedEncryption(advancedEncryptionResponse)

            addAccountInteractor.createAccount(mnemonicString, advancedEncryption, addAccountType)
                .onSuccess { continueBasedOnCodeStatus() }
                .onFailure(::showAccountCreationError)

        }
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


    private suspend fun continueBasedOnCodeStatus() {
        if (interactor.isCodeSet()) {
            router.toDashboard()
        } else {
            router.toCreatePassword(payload)
        }
    }

    fun onBackCommandClick() {
        router.back()
    }
}
