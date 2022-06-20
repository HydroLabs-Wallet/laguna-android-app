package io.novafoundation.nova.feature_account_impl.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

import io.novafoundation.nova.feature_account_impl.presentation.mnemonic.create.SeedWord
import io.novafoundation.nova.common.utils.inflater
import io.novafoundation.nova.feature_account_impl.databinding.ViewSeedConfirmBinding

class SeedConfirmView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ViewSeedConfirmBinding =
        ViewSeedConfirmBinding.inflate(inflater, this, true)
    private val words = listOf<SeedWordView>(binding.word1, binding.word2, binding.word3)

    fun setOnSelectionChangedListener(listener: (Int) -> Unit) {
        words.forEachIndexed { index, wordView ->
            wordView.setOnClickListener { listener.invoke(index) }
        }
    }

    fun setData(data: List<SeedWord>) {
        if (data.size != 3) throw RuntimeException("Wrong selection size")
        data.forEachIndexed { index, item ->
            words[index].setData(item, false)
        }
    }


}
