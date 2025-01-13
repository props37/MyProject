package ru.livetyping.zarina.core.platform

import android.content.Context

public sealed class PackageName {
    public abstract fun getString(context: Context): String

    public data object Own : PackageName() {
        override fun getString(context: Context): String = context.packageName
    }

    public data class External(val packageName: String) : PackageName() {
        override fun getString(context: Context): String = packageName
    }
}
