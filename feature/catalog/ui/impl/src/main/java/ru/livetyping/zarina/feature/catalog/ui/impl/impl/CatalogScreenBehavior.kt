package ru.livetyping.zarina.feature.catalog.ui.impl.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicompose.LifecycleEventEffect
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.feature.catalog.ui.CatalogFeature

@Composable
internal fun CatalogScreenBehavior(
    onLifecycleEvent: (LifecycleEvent) -> Unit,
    sideEffects: Flow<CatalogSideEffect>,
    navActions: CatalogFeature.NavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)

    BottomNavBarBehavior(isVisible = true)

    LifecycleEventEffect(onLifecycleEvent = onLifecycleEvent)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is CatalogSideEffect.Navigate -> {
                        navigate(currentNavActions, sideEffect.action)
                    }
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}

private fun navigate(navActions: CatalogFeature.NavActions, action: CatalogScreenAction) {
    when (action) {
        CatalogScreenAction.BackClicked -> navActions.onBackClicked()
        CatalogScreenAction.SearchClicked -> navActions.onSearchClicked()
        is CatalogScreenAction.CategoryClicked -> navActions.onCategoryClicked(action.categoryId)
    }
}
