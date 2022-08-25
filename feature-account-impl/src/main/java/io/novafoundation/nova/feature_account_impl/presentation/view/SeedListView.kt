package io.novafoundation.nova.feature_account_impl.presentation.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import io.novafoundation.nova.feature_account_api.presenatation.account.add.SeedWord
import io.novafoundation.nova.common.utils.inflater
import io.novafoundation.nova.common.view.GridSpacingItemDecoration
import io.novafoundation.nova.feature_account_impl.R
import io.novafoundation.nova.feature_account_impl.databinding.ViewSeedListBinding

class SeedListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val adapter = SeedAdapter()
    private val binding: ViewSeedListBinding = ViewSeedListBinding.inflate(inflater, this, true)
    var showNumber = true
        set(value) {
            adapter.showNumber = value
            field = value
        }
    var onItemClick: ((SeedWord) -> Unit)? = null
        set(value) {
            adapter.onItemClick = value
            field = value
        }

    init {
        binding.rwList.adapter = adapter
    }

    fun submitList(list: List<SeedWord>) {
        val manager = binding.rwList.layoutManager as GridLayoutManager
        if (binding.rwList.itemDecorationCount == 0) {
            manager.spanCount = 3
            val spacing = resources.getDimensionPixelSize(R.dimen.margin_16)
            binding.rwList.addItemDecoration(GridSpacingItemDecoration(3, spacing, false))
        }
        adapter.items = list
    }
}
