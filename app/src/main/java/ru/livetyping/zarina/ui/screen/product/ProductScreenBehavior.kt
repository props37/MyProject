package ru.livetyping.zarina.ui.screen.product

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.livetyping.zarina.ui.screen.product.ProductViewModel.SideEffect

@Composable
fun ProductScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigate: (ProductScreenAction) -> Unit,
) {
    val updatedContext by rememberUpdatedState(LocalContext.current)
    val updatedNavigate by rememberUpdatedState(navigate)

    ForcedBottomNavBarBehavior(isVisible = true)

    LifecycleStartEffect(sideEffects) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sideEffects.collect { sideEffect ->
                    when (sideEffect) {
                        is SideEffect.Navigate -> updatedNavigate(sideEffect.action)
                        is SideEffect.Share -> {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                putExtra(Intent.EXTRA_TEXT, sideEffect.text)
                                type = MIME_TYPE_TEXT_PLAIN
                            }
                            val shareIntent = Intent.createChooser(intent, null)
                            updatedContext.startActivity(shareIntent)
                        }
                    }
                }
            }
        }

        onStopOrDispose {}
    }
}

private const val MIME_TYPE_TEXT_PLAIN = "text/plain"
