package io.novafoundation.nova.feature_assets.presentation.asset_details

import io.novafoundation.nova.common.base.BasePresenter
import io.novafoundation.nova.common.utils.WithCoroutineScopeExtensions
import io.novafoundation.nova.common.utils.inBackground
import io.novafoundation.nova.feature_assets.data.mappers.mappers.mapTokenToTokenModel
import io.novafoundation.nova.feature_assets.domain.WalletInteractor
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import io.novafoundation.nova.feature_assets.presentation.balance.detail.AssetDetailsModel
import io.novafoundation.nova.feature_wallet_api.domain.model.Asset
import io.novafoundation.nova.feature_wallet_api.presentation.model.mapAmountToAmountModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import moxy.InjectViewState
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class AssetDetailsPresenter @Inject constructor(
    private val interactor: WalletInteractor,
    private val router: WalletRouter,
    val payload: AssetPayload
) :
    BasePresenter<AssetDetailsView>(), WithCoroutineScopeExtensions {
    override val coroutineScope = presenterScope

    private val showValuesFlow = interactor.assetValueVisibleFlow()
        .inBackground()
        .share()

    val assetFlow = interactor.assetFlow(payload.chainId, payload.chainAssetId)
        .inBackground()
        .share()
    val uiAssetFLow = combine(assetFlow, showValuesFlow) { asset, visible ->
        mapAssetToUi(asset, visible)

    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        uiAssetFLow.onEach { viewState.setAssetData(it) }
            .launchIn(presenterScope)

    }

    fun onSeeAllClick() {
        router.toAssetTransaction(payload)
    }

    fun onSendClick() {
        val payload = AssetPayload(
            chainId = payload.chainId,
            chainAssetId = payload.chainAssetId
        )
        router.toSendAddressChooser(payload)
    }

    fun onReceiveClick() {
        val payload = AssetPayload(
            chainId = payload.chainId,
            chainAssetId = payload.chainAssetId
        )
        router.toAssetReceive(payload)
    }

    fun onBackCommandClick() {
        router.back()
    }

    private fun mapAssetToUi(asset: Asset, showValues: Boolean): AssetDetailsModel {
        return AssetDetailsModel(
            token = mapTokenToTokenModel(asset.token),
            total = mapAmountToAmountModel(asset.total, asset),
            transferable = mapAmountToAmountModel(asset.transferable, asset),
            locked = mapAmountToAmountModel(asset.locked, asset),
            showValues = showValues
        )
    }
}
