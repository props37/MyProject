package ru.zarina.zarina.ui.screen.productsubscription

import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.LocalToastController
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.zarina.zarina.ui.screen.productsubscription.ProductSubscriptionViewModel.SideEffect

@Composable
fun ProductSubscriptionScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigate: (ProductSubscriptionScreenAction) -> Unit,
) {
    val updatedContext by rememberUpdatedState(LocalContext.current)
    val updatedToastController by rememberUpdatedState(LocalToastController.current)
    val updatedNavigate by rememberUpdatedState(navigate)

    ForcedBottomNavBarBehavior(isVisible = false)

    LaunchedEffect(sideEffects) {
        sideEffects.collect { sideEffect ->
            when (sideEffect) {
                is SideEffect.Navigate -> updatedNavigate(sideEffect.action)
                is SideEffect.OpenUrl -> {
                    val intent = CustomTabsIntent.Builder()
                        .setShowTitle(true)
                        .build()
                    intent.launchUrl(updatedContext, sideEffect.url.value.toUri())
                }

                is SideEffect.ShowToast -> updatedToastController.show(sideEffect.message)
            }
        }
    }
}
