package io.novafoundation.nova.app.root.presentation

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Cicerone.Companion.create
import com.github.terrakok.cicerone.Router
import java.util.*
class LocalCiceroneHolder {
    private val containers = HashMap<String, Cicerone<Router>>()

    fun getCicerone(containerTag: String): Cicerone<Router> =
        containers.getOrPut(containerTag) {
            create()
        }
}
