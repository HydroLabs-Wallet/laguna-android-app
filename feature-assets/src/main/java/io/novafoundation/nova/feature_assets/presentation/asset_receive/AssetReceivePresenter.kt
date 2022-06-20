package io.novafoundation.nova.feature_assets.presentation.asset_receive

import io.novafoundation.nova.common.address.AddressIconGenerator
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.common.utils.*
import io.novafoundation.nova.feature_account_api.data.mappers.mapChainToUi
import io.novafoundation.nova.feature_account_api.domain.interfaces.SelectedAccountUseCase
import io.novafoundation.nova.feature_account_api.domain.model.addressIn
import io.novafoundation.nova.feature_account_api.presenatation.account.icon.createAddressModel
import io.novafoundation.nova.feature_account_api.presenatation.actions.ExternalActions
import io.novafoundation.nova.feature_assets.domain.receive.ReceiveInteractor
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import io.novafoundation.nova.feature_assets.presentation.receive.model.TokenReceiver
import io.novafoundation.nova.runtime.multiNetwork.ChainRegistry
import io.novafoundation.nova.runtime.multiNetwork.chainWithAsset
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class AssetReceivePresenter @Inject constructor(
    private val interactor: ReceiveInteractor,
    private val qrCodeGenerator: QrCodeGenerator,
    private val addressIconGenerator: AddressIconGenerator,
    private val resourceManager: ResourceManager,
    private val externalActions: ExternalActions.Presentation,
    private val chainRegistry: ChainRegistry,
    private val selectedAccountUseCase: SelectedAccountUseCase,
    private val router: WalletRouter,
) : MvpPresenter<AssetReceiveView>(), WithCoroutineScopeExtensions {
    override val coroutineScope = presenterScope

    lateinit var asset: AssetPayload
    lateinit var currentAsset: TokenReceiver




    fun onBackCommandClick() {
        router.back()
    }

    fun setInitialAsset(asset: AssetPayload) {
        this.asset = asset
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
         val chainWithAssetAsync by presenterScope.lazyAsync {
            chainRegistry.chainWithAsset(asset.chainId, asset.chainAssetId)
        }

        val receiver = selectedAccountUseCase.selectedMetaAccountFlow()
            .map {
                val (chain, _) = chainWithAssetAsync()
                val address = it.addressIn(chain)!!

                TokenReceiver(
                    addressModel = addressIconGenerator.createAddressModel(chain, address, AddressIconGenerator.SIZE_BIG, it.name),
                    chain = mapChainToUi(chain),
                )
            }
            .inBackground()
            .share()
        receiver.onEach { setAsset(it) }

    }

    fun setAsset(asset: TokenReceiver) {
        this.currentAsset = asset
        viewState.onAssetChanged(asset)
    }

}
