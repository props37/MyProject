package ru.zarina.zarina.util.library.timber

import timber.log.Timber

val Timber.Forest.isEnabled: Boolean
    get() = this.treeCount > 0
