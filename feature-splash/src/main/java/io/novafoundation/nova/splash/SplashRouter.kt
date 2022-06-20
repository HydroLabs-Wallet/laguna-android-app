package io.novafoundation.nova.splash

import io.novafoundation.nova.common.navigation.SecureRouter

interface SplashRouter : SecureRouter {
    fun setResult(key:String,data:Any)

    fun toSplashScreen()
    fun toOnboardingScreen()

    fun toCreatePassword()
    fun toLoginScreen()

    fun openInitialCheckPincode()
}
