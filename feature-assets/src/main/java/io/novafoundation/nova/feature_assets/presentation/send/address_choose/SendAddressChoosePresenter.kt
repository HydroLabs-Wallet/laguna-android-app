package io.novafoundation.nova.feature_assets.presentation.send.address_choose

import com.github.terrakok.cicerone.ResultListener
import io.novafoundation.nova.common.data.model.SelectAccountPayload
import io.novafoundation.nova.common.resources.ResourceManager
import io.novafoundation.nova.common.utils.WithCoroutineScopeExtensions
import io.novafoundation.nova.common.utils.ellipsis
import io.novafoundation.nova.common.utils.systemCall.ScanQrCodeCall
import io.novafoundation.nova.common.utils.systemCall.SystemCallExecutor
import io.novafoundation.nova.common.utils.systemCall.onSystemCallFailure
import io.novafoundation.nova.feature_account_api.domain.interfaces.AccountRepository
import io.novafoundation.nova.feature_account_api.domain.model.addressIn
import io.novafoundation.nova.feature_assets.domain.send.SendInteractor
import io.novafoundation.nova.feature_assets.presentation.AssetPayload
import io.novafoundation.nova.feature_assets.presentation.WalletRouter
import io.novafoundation.nova.feature_assets.presentation.send.ContactPayload
import io.novafoundation.nova.feature_assets.presentation.send.ContactUi
import io.novafoundation.nova.feature_assets.presentation.send.ContactUiHeader
import io.novafoundation.nova.feature_assets.presentation.send.TransferDraft
import io.novafoundation.nova.runtime.multiNetwork.ChainRegistry
import io.novafoundation.nova.runtime.multiNetwork.chainWithAsset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import moxy.InjectViewState
import io.novafoundation.nova.common.base.BasePresenter
import moxy.presenterScope
import java.math.BigDecimal
import javax.inject.Inject

@InjectViewState
class SendAddressChoosePresenter @Inject constructor(
    private val router: WalletRouter,
    private val payload: AssetPayload,
    private val sendInteractor: SendInteractor,
    private val accountRepository: AccountRepository,
    private val chainRegistry: ChainRegistry,
    private val systemCallExecutor: SystemCallExecutor,
    private val resourceManager: ResourceManager,

    ) : BasePresenter<SendAddressChooseView>(), WithCoroutineScopeExtensions {

    override val coroutineScope: CoroutineScope = presenterScope
    private val searchQueryFlow = MutableStateFlow("")


    private var isPaste = true
    private var query = ""

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        combine(
            sendInteractor.getContacts(),
            searchQueryFlow
        ) { assets, query ->
            this.query = query
            if (query.isEmpty()) {
                isPaste = !(assets.isEmpty() && query.isNotBlank())
                viewState.submitList(assets, query)
            } else {

                var filtered = assets.filter {
                    when (val item = it) {
                        is ContactUi -> {
                            item.name.contains(query) ||
                                item.address.contains(query)
                        }
                        else -> true
                    }

                }
                filtered = filtered.filterIndexed { index, marker ->
                    if (index == filtered.size - 1) {
                        marker !is ContactUiHeader
                    } else {
                        marker is ContactUiHeader && filtered[index + 1] is ContactUi
                    }
                }

                isPaste = !(filtered.isEmpty() && query.isNotBlank())
                viewState.submitList(filtered, query)
            }
        }.launchIn(presenterScope)


    }

    fun onSearchChanged(text: String) {
        searchQueryFlow.value = text.trimIndent()
    }

    fun onMyAccountClick() {
        val selectPayload = SelectAccountPayload("SendAddressChoosePresenter.selectAccount", false)
        val resultListener = ResultListener {
            presenterScope.launch {
                val account = accountRepository.getMetaAccount(it as Long)
                val (chain, asset) = chainRegistry.chainWithAsset(payload.chainId, payload.chainAssetId)

                val address = account.addressIn(chain) ?: ""
                val name = account.name.ifEmpty { address.ellipsis() }
                onItemClicked(ContactUi(name, address))
            }
        }
        router.setResultListener(selectPayload.tag, resultListener)
        router.toSelectAccount(selectPayload)
    }

    fun searchButtonClicked() {
        if (isPaste) {
            viewState.paste()
        } else {
            router.toCreateContact(ContactPayload(query))
        }
    }

    fun onSearchResultClicked() {
        onItemClicked(ContactUi(query, query))
    }

    fun onQrClicked() {
        presenterScope.launch {
            systemCallExecutor.executeSystemCall(ScanQrCodeCall())
                .onSuccess { address ->
                    val contact = ContactUi(address.ellipsis(), address)
                    onItemClicked(contact)
                }.onSystemCallFailure {
                    viewState.showError(resourceManager.getString(io.novafoundation.nova.feature_account_api.R.string.invoice_scan_error_no_info))
                }
        }
    }

    fun onItemClicked(data: ContactUi) {
        val payload = TransferDraft(
            amount = BigDecimal.ZERO,
            fee = BigDecimal.ZERO,
            assetPayload = payload,
            contact = data
        )
        router.toSendFill(payload)
    }

    fun onBackCommandClick() {
        router.back()
    }
}
