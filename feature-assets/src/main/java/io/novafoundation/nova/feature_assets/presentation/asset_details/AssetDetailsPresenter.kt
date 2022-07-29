package io.novafoundation.nova.feature_assets.presentation.asset_details

import io.novafoundation.nova.common.utils.WithCoroutineScopeExtensions
import io.novafoundation.nova.common.utils.inBackground
import io.novafoundation.nova.common.utils.patternWith
import io.novafoundation.nova.feature_assets.data.mappers.mappers.mapTokenToTokenModel
import io.novafoundation.nova.feature_assets.domain.WalletInteractor
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import io.novafoundation.nova.feature_assets.presentation.balance.detail.AssetDetailsModel
import io.novafoundation.nova.feature_wallet_api.domain.model.Asset
import io.novafoundation.nova.feature_wallet_api.presentation.model.mapAmountToAmountModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.presenterScope
import javax.inject.Inject

@InjectViewState
class AssetDetailsPresenter @Inject constructor(
    private val interactor: WalletInteractor,
    private val router: WalletRouter,
    val payload: AssetPayload
) :
    MvpPresenter<AssetDetailsView>(), WithCoroutineScopeExtensions {
    override val coroutineScope = presenterScope


    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        val assetFlow = interactor.assetFlow(payload.chainId, payload.chainAssetId)
            .map { mapAssetToUi(it) }
            .inBackground()
            .share()

        assetFlow
            .onEach { viewState.setAssetData(it) }
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

    private fun mapAssetToUi(asset: Asset): AssetDetailsModel {
        return AssetDetailsModel(
            token = mapTokenToTokenModel(asset.token),
            total = mapAmountToAmountModel(asset.total, asset),
            transferable = mapAmountToAmountModel(asset.transferable, asset),
            locked = mapAmountToAmountModel(asset.locked, asset)
        )
    }
}
