package io.novafoundation.nova.common.mixin.impl

import io.novafoundation.nova.common.base.BaseFragmentOld
import io.novafoundation.nova.common.base.BaseViewModel
import io.novafoundation.nova.common.mixin.api.Browserable
import io.novafoundation.nova.common.utils.showBrowser

fun <T> BaseFragmentOld<T>.observeBrowserEvents(viewModel: T) where T : BaseViewModel, T : Browserable {
    viewModel.openBrowserEvent.observeEvent(this::showBrowser)
}
