package io.novafoundation.nova.feature_account_impl.presentation.mnemonic.create

import android.text.TextUtils
import io.novafoundation.nova.common.utils.flowOf
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountInteractor
import io.novafoundation.nova.feature_account_impl.presentation.AccountRouter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class SeedCreatePresenter @Inject constructor(
    private val interactor: AccountInteractor,
    private val router: AccountRouter,
    private val seedMapper: SeedMapper
) :
    MvpPresenter<SeedCreateView>() {
    var isAuth = true
    var seed: String = ""
    var seedWords = emptyList<SeedWord>()
    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        getSeedPhrase()
    }

    fun getSeedPhrase() {
        flowOf { interactor.generateMnemonic() }
            .onEach {
                val list = it.wordList
                seed = TextUtils.join(", ", list)
                seedWords = seedMapper.map(list)
                viewState.setSeeds(seedWords)
            }
            .launchIn(presenterScope)
    }

    fun onCopyClick() {
        viewState.copyToClipboard(seed)
    }

    fun onNextClick() {
        router.toSeedConfirm(isAuth, seedWords)
    }

    fun onInfoClick() {
        router.toSeedInfo()
    }

    fun onBackCommandClick() {
        router.back()
    }
}
