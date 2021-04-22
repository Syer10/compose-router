package com.github.zsoltk.compose.savedinstancestate

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf

private val rootSavedInstanceState = Bundle()

val LocalSavedInstanceState: ProvidableCompositionLocal<Bundle> = compositionLocalOf { rootSavedInstanceState }

internal const val BUNDLE_KEY = "LocalSavedInstanceState"

fun Bundle.saveLocal() {
    putBundle(BUNDLE_KEY, rootSavedInstanceState)
}


