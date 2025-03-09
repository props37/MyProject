package ru.livetyping.zarina.feature.search.ui.impl.impl.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uicommon.LifecycleEvent
import ru.livetyping.zarina.core.uicompose.LifecycleEventEffect
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.core.uikit.toast.LocalZarinaToastController

@Composable
internal fun SearchScreenBehavior(
    onLifecycleEvent: (LifecycleEvent) -> Unit,
    sideEffects: Flow<SearchSideEffect>,
    navActions: SearchNavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)
    val currentZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)
    val currentFocusManager by rememberUpdatedState(LocalFocusManager.current)
    val currentKeyboardController by rememberUpdatedState(LocalSoftwareKeyboardController.current)

    BottomNavBarBehavior(isVisible = true)

    LifecycleEventEffect(onLifecycleEvent = onLifecycleEvent)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is SearchSideEffect.Navigate -> {
                        currentKeyboardController?.hide()
                        navigate(currentNavActions, sideEffect.action)
                    }

                    SearchSideEffect.ClearSearchBarTextFieldFocus -> currentFocusManager.clearFocus()
                    is SearchSideEffect.ShowZarinaToast -> {
                        currentZarinaToastController.show(sideEffect.message)
                    }
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}

private fun navigate(navActions: SearchNavActions, action: SearchScreenAction) {
    when (action) {
        SearchScreenAction.BackClicked -> navActions.onBackClicked()
        is SearchScreenAction.CategoryClicked -> navActions.onCategoryClicked(action.categoryId)
        is SearchScreenAction.ProductClicked -> navActions.onProductClicked(action.product)
        is SearchScreenAction.SubscribeToProductClicked -> {
            navActions.onSubscribeToProductClicked(action.product, action.offer)
        }
    }
}
