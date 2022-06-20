package io.novafoundation.nova.app.di.app

import android.content.Context
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import io.novafoundation.nova.app.App
import io.novafoundation.nova.app.root.presentation.LocalCiceroneHolder
import io.novafoundation.nova.common.di.scope.ApplicationScope
import javax.inject.Singleton

@Module
class AppModule {

    @ApplicationScope
    @Provides
    fun provideContext(application: App): Context {
        return application
    }


}
