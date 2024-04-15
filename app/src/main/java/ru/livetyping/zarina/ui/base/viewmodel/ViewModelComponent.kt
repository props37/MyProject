package ru.livetyping.zarina.ui.base.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.io.Closeable

abstract class ViewModelComponent : Closeable {
    protected val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun close() {
        scope.cancel()
    }
}
