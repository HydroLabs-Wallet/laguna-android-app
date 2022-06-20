package io.novafoundation.nova.feature_staking_impl.presentation.staking.main.unbonding

import io.novafoundation.nova.common.base.BaseFragmentOld
import io.novafoundation.nova.feature_staking_impl.presentation.staking.main.unbonding.rebond.ChooseRebondKindBottomSheet

fun BaseFragmentOld<*>.setupUnbondingMixin(mixin: UnbondingMixin, view: UnbondingsView) {
    mixin.rebondKindAwaitable.awaitableActionLiveData.observeEvent {
        ChooseRebondKindBottomSheet(requireContext(), it.onSuccess, it.onCancel)
            .show()
    }

    view.onCancelClicked { mixin.cancelClicked() }
    view.onRedeemClicked { mixin.redeemClicked() }

    mixin.state.observe(view::setState)
}
