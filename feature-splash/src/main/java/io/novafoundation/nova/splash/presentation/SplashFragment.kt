package io.novafoundation.nova.splash.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.novafoundation.nova.common.base.BaseFragmentOld
import io.novafoundation.nova.common.di.FeatureUtils
import io.novafoundation.nova.splash.R
import io.novafoundation.nova.splash.di.SplashFeatureApi
import io.novafoundation.nova.splash.di.SplashFeatureComponent
import javax.inject.Inject

class SplashFragment : BaseFragmentOld<SplashViewModel>() {

    companion object {
        fun getNewInstance(): SplashFragment = SplashFragment()
    }

    @Inject
    lateinit var splashViewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun initViews() {
    }

    override fun inject() {
        FeatureUtils.getFeature<SplashFeatureComponent>(this, SplashFeatureApi::class.java)
            .splashComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        (activity as? SplashBackgroundHolder)?.removeSplashBackground()
    }

    override fun subscribe(viewModel: SplashViewModel) {}
}
