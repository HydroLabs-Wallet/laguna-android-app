package io.novafoundation.nova.app.root.presentation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import io.novafoundation.nova.app.R
import io.novafoundation.nova.app.root.di.RootApi
import io.novafoundation.nova.app.root.di.RootComponent
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.common.utils.setVisible
import io.novafoundation.nova.common.utils.showToast
import io.novafoundation.nova.splash.presentation.SplashBackgroundHolder
import kotlinx.android.synthetic.main.activity_root.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.lang.ref.WeakReference
import javax.inject.Inject

class RootActivity : MvpAppCompatActivity(), SplashBackgroundHolder, ChainHolder, RootView {

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    @InjectPresenter
    lateinit var presenter: RootPresenter

    @ProvidePresenter
    fun createPresenter() = presenter

    private val navigator: Navigator = AppNavigator(this, R.id.main_container)
    override val chain = ArrayList<WeakReference<Fragment>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        subscribe()
    }

    fun inject() {
        FeatureUtils.getFeature<RootComponent>(this, RootApi::class.java)
            .mainActivityComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        removeSplashBackground()

        presenter.restoredAfterConfigChange()
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)

    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun onStop() {
        super.onStop()

        presenter.noticeInBackground()
    }

    override fun onStart() {
        super.onStart()

        presenter.noticeInForeground()
    }

    fun subscribe() {
        presenter.showConnectingBarLiveData.observe(this) { show ->
            rootNetworkBar.setVisible(show)
        }


    }

    override fun showMessage(text: String) {
        showToast(text)
    }

    override fun removeSplashBackground() {
        mainView.setBackgroundResource(R.color.black)
    }

    fun changeLanguage() {
        presenter.noticeLanguageLanguage()

        recreate()
    }

    private fun processIntent(intent: Intent) {
        val uri = intent.data?.toString()

        uri?.let { presenter.externalUrlOpened(uri) }
    }

}
