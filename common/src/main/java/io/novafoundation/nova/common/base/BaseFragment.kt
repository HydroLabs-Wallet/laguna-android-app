package io.novafoundation.nova.common.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import io.novafoundation.nova.common.view.CustomSnack
import moxy.MvpAppCompatFragment
import java.lang.ref.WeakReference

abstract class BaseFragment<T : BasePresenter<*>> : MvpAppCompatFragment(), BackButtonListener, BaseView {

    abstract fun inject()
    open val isAuthorisedContent = true
//    @Inject
//    lateinit var presenterProvider: Provider<T>
//
//    @InjectPresenter
//    lateinit var presenter: T
//
//    @ProvidePresenter
//    open fun createPresenter(): T {
//        return presenterProvider.get()
//    }
//    private val presenter: T by moxyPresenter { presenterProvider.get() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        inject()
        super.onAttach(context)

        val activity = activity
        if (activity is ChainHolder) {
            activity.chain.add(WeakReference<Fragment>(this))
        }else{
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
    fun <A> argument(key: String): A = requireArguments()[key] as A

    override fun showError(data: String) {
        CustomSnack.make(requireView(), true, data)?.show()
    }

    override fun showSuccess(data: String) {
        CustomSnack.make(requireView(), false, data)?.show()
    }
}
