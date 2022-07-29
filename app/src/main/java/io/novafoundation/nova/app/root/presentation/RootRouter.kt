package io.novafoundation.nova.app.root.presentation

import com.github.terrakok.cicerone.ResultListener
import io.novafoundation.nova.common.data.model.SelectAccountPayload
import io.novafoundation.nova.feature_assets.presentation.AssetPayload

interface RootRouter {
    fun setResult(key:String,data:Any)
    fun setResultListener(key:String, listener: ResultListener)
    fun backToDashBoard()
    fun returnToWallet()

    fun toDashboard()
    fun toChainsSettings()
    fun toSelectAccount(data: SelectAccountPayload)

    fun toAssetSelectionToReceive()
    fun toAssetReceive(assetPayload: AssetPayload)
    fun openAssetDetails(assetPayload: AssetPayload)
}
