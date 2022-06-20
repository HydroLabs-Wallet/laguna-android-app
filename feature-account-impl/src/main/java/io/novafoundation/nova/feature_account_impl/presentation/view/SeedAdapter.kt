package io.novafoundation.nova.feature_account_impl.presentation.view

import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.create.SeedWord
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import io.novafoundation.nova.common.utils.DefaultDiffUtilCallback
import io.novafoundation.nova.feature_account_impl.databinding.ListitemSeedWordBinding

class SeedAdapter : AsyncListDifferDelegationAdapter<SeedWord>(DefaultDiffUtilCallback()) {
    var showNumber = true
    var onItemClick: ((SeedWord) -> Unit)? = null

    init {
        delegatesManager.addDelegate(delegate())
    }

    private fun delegate() =
        adapterDelegateViewBinding<SeedWord, SeedWord, ListitemSeedWordBinding>(
            { layoutInflater, root ->
                ListitemSeedWordBinding.inflate(layoutInflater, root, false)
            }
        ) {
            bind {
                binding.word.setData(item, showNumber)
                binding.root.setOnClickListener {
                    onItemClick?.invoke(item)
                }
            }
        }
}
