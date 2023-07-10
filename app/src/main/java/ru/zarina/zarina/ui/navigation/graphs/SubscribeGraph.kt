package ru.zarina.zarina.ui.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import ru.zarina.zarina.ui.navigation.base.composableDestination
import ru.zarina.zarina.ui.navigation.base.navigationGraph
import ru.zarina.zarina.ui.navigation.destinations.Destinations
import ru.zarina.zarina.ui.navigation.destinations.Subscribe
import ru.zarina.zarina.ui.screens.subscribe.SubscribeScreen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun NavGraphBuilder.subscribeGraph(navController: NavController) {
    navigationGraph(Subscribe) {
        composableDestination(Subscribe.Root) {
            SubscribeScreen(
                showSuccess = { email ->
                    navController.navigate(
                        Subscribe.Success.createRoute(Subscribe.Success.Arguments(email))
                    ) {
                        popUpTo(Destinations.Product.routeSchema)
                    }
                },
                showWebpage = { url ->
                    val encodedUrl =
                        URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                    navController.navigate(
                        Destinations.Webpage.createRoute(
                            Destinations.Webpage.Arguments(
                                encodedUrl
                            )
                        )
                    )
                },
                goBack = {
                    navController.popBackStack(Subscribe.routeSchema, true)
                }
            )
        }
        composableDestination(Subscribe.Success) {
            ru.zarina.zarina.ui.screens.subscribe.success.SuccessScreen(
                goBack = {
                    navController.popBackStack(Subscribe.routeSchema, true)
                }
            )
        }
    }
}
