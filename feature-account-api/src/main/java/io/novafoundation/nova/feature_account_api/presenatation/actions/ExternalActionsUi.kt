package io.novafoundation.nova.feature_account_api.presenatation.actions

import android.content.Context
import io.novafoundation.nova.common.base.BaseFragmentOld
import io.novafoundation.nova.common.base.BaseViewModel
import io.novafoundation.nova.common.mixin.impl.observeBrowserEvents

fun <T> BaseFragmentOld<T>.setupExternalActions(viewModel: T) where T : BaseViewModel, T : ExternalActions {
    setupExternalActions(viewModel) { context, payload ->
        ExternalActionsSheet(
            context,
            payload,
            viewModel::copyAddressClicked,
            viewModel::viewExternalClicked
        )
    }
}

inline fun <T> BaseFragmentOld<T>.setupExternalActions(
    viewModel: T,
    crossinline customSheetCreator: (Context, ExternalActions.Payload) -> ExternalActionsSheet,
) where T : BaseViewModel, T : ExternalActions {
    observeBrowserEvents(viewModel)

    viewModel.showExternalActionsEvent.observeEvent {
        customSheetCreator(requireContext(), it)
            .show()
    }
}

fun <T> T.copyAddressClicked(address: String) where T : BaseViewModel, T : ExternalActions {
    copyAddress(address, ::showMessage)
}
