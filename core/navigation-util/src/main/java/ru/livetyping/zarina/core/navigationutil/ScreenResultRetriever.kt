package ru.livetyping.zarina.core.navigationutil

import androidx.navigation.NavBackStackEntry
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.navigation.ScreenResult

public fun interface ScreenResultRetriever<R : ScreenResult> {
    public fun get(navBackStackEntry: NavBackStackEntry): Flow<R?>
}
