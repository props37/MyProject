package ru.livetyping.zarina.core.uikit.list

import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset

public object ZarinaListDefaults {
    public val LazyListFadeInSpec: SpringSpec<Float> = spring()
    public val LazyListPlacementSpec: SpringSpec<IntOffset> = spring()
    public val LazyListFadeOutSpec: SpringSpec<Float> = LazyListFadeInSpec

    public fun Modifier.animateZarinaItem(lazyItemScope: LazyItemScope): Modifier {
        val thisModifier = this
        return with(lazyItemScope) {
            thisModifier.animateItem(
                fadeInSpec = LazyListFadeInSpec,
                placementSpec = LazyListPlacementSpec,
                fadeOutSpec = LazyListFadeOutSpec,
            )
        }
    }
}
