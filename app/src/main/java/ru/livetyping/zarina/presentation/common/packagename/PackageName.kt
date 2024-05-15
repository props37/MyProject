package ru.livetyping.zarina.presentation.common.packagename

import android.content.Context

sealed class PackageName {
    abstract fun getString(context: Context): String

    data object Own : PackageName() {
        override fun getString(context: Context): String = context.packageName
    }

    data class External(val packageName: String) : PackageName() {
        override fun getString(context: Context): String = packageName
    }
}
