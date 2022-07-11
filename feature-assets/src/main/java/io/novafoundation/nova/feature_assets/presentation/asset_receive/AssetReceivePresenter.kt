package io.novafoundation.nova.feature_assets.presentation.asset_receive

import io.novafoundation.nova.common.address.AddressIconGenerator
import io.novafoundation.nova.common.utils.WithCoroutineScopeExtensions
import io.novafoundation.nova.common.utils.inBackground
import io.novafoundation.nova.common.utils.invoke
import io.novafoundation.nova.common.utils.lazyAsync
import io.novafoundation.nova.feature_account_api.data.mappers.mapChainToUi
import io.novafoundation.nova.feature_account_api.domain.interfaces.SelectedAccountUseCase
import io.novafoundation.nova.feature_account_api.domain.model.addressIn
import io.novafoundation.nova.feature_account_api.presenatation.account.icon.createAddressModel
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import io.novafoundation.nova.feature_assets.presentation.asset_receive_chooser.AssetReceiveChooserFragment
import io.novafoundation.nova.feature_assets.presentation.asset_receive_chooser.AssetReceivePayload
import io.novafoundation.nova.feature_assets.presentation.receive.model.TokenReceiver
import io.novafoundation.nova.runtime.multiNetwork.ChainRegistry
import io.novafoundation.nova.runtime.multiNetwork.chainWithAsset
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class AssetReceivePresenter @Inject constructor(
    private val addressIconGenerator: AddressIconGenerator,
    private val chainRegistry: ChainRegistry,
    private val selectedAccountUseCase: SelectedAccountUseCase,
    private val router: WalletRouter,
    private var payload: AssetPayload
) : MvpPresenter<AssetReceiveView>(), WithCoroutineScopeExtensions {
    override val coroutineScope = presenterScope
    var currentAsset: TokenReceiver? = null
    private var updatedPayLoad = payload


    fun onBackCommandClick() {
        router.back()
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        if (currentAsset == null) {
            presenterScope.launch { setAsset(requestAsset().first()) }
        } else {
            setAsset(currentAsset!!)
        }
    }

    fun onAssetClick() {
        currentAsset?.asset?.let {
            router.setResultListener(AssetReceiveChooserFragment.RESULT){
                updatedPayLoad = it as AssetPayload
                presenterScope.launch { setAsset(requestAsset().first()) }
            }
            router.toAssetReceiveChooser(AssetReceivePayload(it.priceId.orEmpty().ifEmpty { it.name.lowercase() }))
        }
    }

    fun setAsset(asset: TokenReceiver) {
        this.currentAsset = asset
        viewState.onAssetChanged(asset)
    }

    private fun requestAsset(): Flow<TokenReceiver> {
        val chainWithAssetAsync by presenterScope.lazyAsync {
            chainRegistry.chainWithAsset(updatedPayLoad.chainId, updatedPayLoad.chainAssetId)
        }

        val assetReceiver = selectedAccountUseCase.selectedMetaAccountFlow()
            .map {
                val (chain, asset) = chainWithAssetAsync()
                val address = it.addressIn(chain)!!

                TokenReceiver(
                    asset = asset,
                    addressModel = addressIconGenerator.createAddressModel(chain, address, AddressIconGenerator.SIZE_BIG, it.name),
                    chain = mapChainToUi(chain),
                )
            }
            .inBackground()
            .share()
        return assetReceiver
    }
}
