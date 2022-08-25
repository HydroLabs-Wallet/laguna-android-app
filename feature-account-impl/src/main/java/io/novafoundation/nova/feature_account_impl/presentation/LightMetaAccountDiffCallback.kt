package io.novafoundation.nova.feature_account_impl.presentation

import androidx.recyclerview.widget.DiffUtil
import io.novafoundation.nova.feature_account_impl.presentation.model.LightMetaAccountUi

object LightMetaAccountDiffCallback : DiffUtil.ItemCallback<LightMetaAccountUi>() {

    override fun areItemsTheSame(oldItem: LightMetaAccountUi, newItem: LightMetaAccountUi): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LightMetaAccountUi, newItem: LightMetaAccountUi): Boolean {
        return oldItem == newItem
    }
}
