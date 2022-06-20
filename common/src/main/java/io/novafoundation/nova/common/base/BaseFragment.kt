package io.novafoundation.nova.common.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import moxy.MvpAppCompatFragment
import java.lang.ref.WeakReference

abstract class BaseFragment : MvpAppCompatFragment(), BackButtonListener {

    abstract fun inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onAttach(context: Context) {
       inject()
        super.onAttach(context)

        val activity = activity
        if (activity is ChainHolder) {
            (activity as ChainHolder).chain.add(WeakReference<Fragment>(this))
        }
    }

    override fun onDetach() {
        val activity = activity
        if (activity is ChainHolder) {
            val chain = (activity as ChainHolder).chain
            val it = chain.iterator()
            while (it.hasNext()) {
                val fragmentReference = it.next()
                val fragment = fragmentReference.get()
                if (fragment != null && fragment === this) {
                    it.remove()
                    break
                }
            }
        }
        super.onDetach()
    }

    @Suppress("UNCHECKED_CAST")
    fun <A> argument(key: String): A = arguments!![key] as A
}
