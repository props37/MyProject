package ru.livetyping.zarina.feature.cart.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.cart.ui.impl.impl.recipient.RecipientNavActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.recipient.RecipientNavEntry
import ru.livetyping.zarina.feature.cart.ui.impl.impl.recipient.RecipientScreen

internal fun NavGraphBuilder.recipientScreen(actions: RecipientNavActions) {
    composable<RecipientNavEntry>(
        typeMap = RecipientNavEntry.typeMap(),
    ) {
        RecipientScreen(actions)
    }
}
