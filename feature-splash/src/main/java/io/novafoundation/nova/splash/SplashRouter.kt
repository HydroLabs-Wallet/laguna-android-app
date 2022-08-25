package io.novafoundation.nova.splash

import io.novafoundation.nova.common.navigation.SecureRouter
import io.novafoundation.nova.feature_account_api.presenatation.account.add.AddAccountPayload

interface SplashRouter : SecureRouter {
    fun setResult(key:String,data:Any)

    fun toSplashScreen()
    fun toOnboardingScreen()

    fun toCreatePassword(payload: AddAccountPayload)
    fun toLoginScreen()

    fun openInitialCheckPincode()
}
