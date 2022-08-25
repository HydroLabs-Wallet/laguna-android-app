package io.novafoundation.nova.feature_account_impl.presentation.common.mixin.addAccountChooser.ui

import io.novafoundation.nova.common.base.BaseFragmentOld
import io.novafoundation.nova.feature_account_api.presenatation.mixin.importType.ImportTypeChooserMixin
import io.novafoundation.nova.feature_account_api.presenatation.mixin.importType.setupImportTypeChooser
import io.novafoundation.nova.feature_account_impl.presentation.common.mixin.addAccountChooser.AddAccountLauncherMixin

fun BaseFragmentOld<*>.setupAddAccountLauncher(mixin: AddAccountLauncherMixin) {
    val asImportTypeChooser = object : ImportTypeChooserMixin {
        override val showChooserEvent = mixin.showImportTypeChooser
    }
    setupImportTypeChooser(asImportTypeChooser)

    mixin.showAddAccountTypeChooser.observeEvent {
        AddAccountChooserBottomSheet(
            context = requireContext(),
            payload = it
        ).show()
    }
}
