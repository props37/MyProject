package ru.livetyping.zarina.util.compose.collapsingtopbar

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

// Source: androidx.compose.material3.EnterAlwaysScrollBehavior

class EnterAlwaysScrollBehavior(
    override val state: CollapsingTopBarState,
    override val snapAnimationSpec: AnimationSpec<Float>?,
    override val flingAnimationSpec: DecayAnimationSpec<Float>?,
    private val canScroll: () -> Boolean = { true },
    private val scrollBeforeContent: () -> Boolean = { true },
) : CollapsingTopBarScrollBehavior {
    private var settleTopBarJob: Job? = null

    override val isPinned: Boolean = false

    override var nestedScrollConnection =
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (!canScroll()) return Offset.Zero
                settleTopBarJob?.cancel()
                val prevHeightOffset = state.heightOffset
                state.heightOffset += available.y
                return if (scrollBeforeContent() && prevHeightOffset != state.heightOffset) {
                    // We're in the middle of top app bar collapse or expand.
                    // Consume only the scroll on the Y axis.
                    available.copy(x = 0f)
                } else {
                    Offset.Zero
                }
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (!canScroll()) return Offset.Zero
                if (scrollBeforeContent()) {
                    state.contentOffset += consumed.y
                    if (state.heightOffset == 0f || state.heightOffset == state.heightOffsetLimit) {
                        if (consumed.y == 0f && available.y > 0f) {
                            // Reset the total content offset to zero when scrolling all the way down.
                            // This will eliminate some float precision inaccuracies.
                            state.contentOffset = 0f
                        }
                    }
                    state.heightOffset += consumed.y
                }
                return Offset.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                val superConsumed = super.onPostFling(consumed, available)
                val thisConsumed = coroutineScope {
                    async {
                        settleTopBar(
                            state,
                            available.y,
                            flingAnimationSpec,
                            snapAnimationSpec
                        )
                    }.also { settleTopBarJob = it }.await()
                }
                return superConsumed + thisConsumed
            }
        }
}
