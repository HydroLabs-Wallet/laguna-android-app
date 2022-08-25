package io.novafoundation.nova.app.root.presentation

import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

interface ChainHolder {
    val chain: MutableList<WeakReference<Fragment>>
}

interface ChainScreen {
    val name: String
    val creationTime: Long
}
