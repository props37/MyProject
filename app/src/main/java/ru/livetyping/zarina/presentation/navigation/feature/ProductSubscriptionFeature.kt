package ru.livetyping.zarina.presentation.navigation.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import ru.livetyping.zarina.feature.productsubscription.ui.api.ProductSubscriptionFeature
import ru.livetyping.zarina.feature.productsubscription.ui.api.ProductSubscriptionNavActions

fun NavGraphBuilder.productSubscriptionFeature(
    feature: ProductSubscriptionFeature,
    actions: ProductSubscriptionNavActions,
) {
    with(feature) {
        composable(
            actions = actions,
            resultRetrievers = Unit,
        )
    }
}

@Composable
fun rememberProductSubscriptionNavActions(
    navController: NavHostController
): ProductSubscriptionNavActions {
    return remember(navController) {
        val navigateUp: () -> Unit = { navController.navigateUp() }

        ProductSubscriptionNavActions(
            onBackClicked = navigateUp,
            onSubscriptionCompleted = navigateUp,
        )
    }
}
