package ru.livetyping.zarina.presentation.navigation.destination.graph

import ru.livetyping.zarina.presentation.navigation.BaseRoute
import ru.livetyping.zarina.presentation.navigation.base.parameterless.SimpleDestination
import ru.livetyping.zarina.presentation.navigation.base.parameterless.SimpleGraph

object LoyaltyProgramGraph : SimpleGraph(
    baseRoute = BaseRoute.LOYALTY_PROGRAM_GRAPH,
    startDestination = LoyaltyProgram,
) {
    data object LoyaltyProgram : SimpleDestination(BaseRoute.LOYALTY_PROGRAM)
}
