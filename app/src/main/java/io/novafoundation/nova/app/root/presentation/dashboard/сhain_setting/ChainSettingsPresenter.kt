package io.novafoundation.nova.app.root.presentation.dashboard.сhain_setting

import io.novafoundation.nova.common.address.AddressIconGenerator
import io.novafoundation.nova.common.utils.WithCoroutineScopeExtensions
import io.novafoundation.nova.common.utils.inBackground
import io.novafoundation.nova.feature_account_api.domain.interfaces.SelectedAccountUseCase
import io.novafoundation.nova.feature_assets.data.mappers.mappers.mapAssetToAssetModel
import io.novafoundation.nova.feature_assets.domain.WalletInteractor
import io.novafoundation.nova.feature_assets.domain.assets.list.AssetsListInteractor
import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import io.novafoundation.nova.feature_assets.presentation.model.AssetModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import moxy.InjectViewState
import io.novafoundation.nova.common.base.BasePresenter
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class ChainSettingsPresenter @Inject constructor(
    private val interactor: WalletInteractor,
    private val assetsListInteractor: AssetsListInteractor,
    private val addressIconGenerator: AddressIconGenerator,
    private val selectedAccountUseCase: SelectedAccountUseCase,
    private val router: WalletRouter,
) :
    BasePresenter<ChainSettingsView>(), WithCoroutineScopeExtensions {

    override val coroutineScope: CoroutineScope = presenterScope


    private val searchQueryFlow = MutableStateFlow("")
    private val balancesFlow = interactor.balancesFlow()
        .inBackground()
        .share()
    val assetsFlow: Flow<List<AssetModel>> = balancesFlow.map { balances ->
        balances.assets
            .map { entry-> entry.value.map{mapAssetToAssetModel(entry.key.chain,it)} }
            .flatten()

    }
        .distinctUntilChanged()
        .inBackground()
        .share()


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        combine(
            assetsFlow,
            searchQueryFlow
        ) { assets, query ->
            if (query.isEmpty()) {
                viewState.submitList(assets)
            } else {
                val filtered = assets.filter {
                    val configuration = it.token.configuration
                    configuration.name == query ||
                        configuration.symbol == query ||
                        configuration.chainId == query
                }
                viewState.submitList(filtered)
            }


        }.launchIn(presenterScope)
    }

    fun onSearchChanged(text: String) {
        searchQueryFlow.value = text.trimIndent()
    }

    fun onCheckChanged(asset: AssetModel, enabled: Boolean) {
        presenterScope.launch {
//            walletRepository.updateAssetEnabled(asset = asset.asset, enabled = enabled)
        }
    }


    fun onBackCommandClick() {
        router.back()
    }
}
