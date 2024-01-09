package ru.zarina.zarina.ui.app

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.rework.graph.HomeGraph
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.utils.clean.invoke
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val interactor: AppInteractor,
) : ViewModel() {

    val startDestination: Destination<Unit> = runBlocking {
        val isOnboardingCompleted = interactor.getIsOnboardingCompleted()
            .firstOrNull()?.getOrNull() ?: false
        if (isOnboardingCompleted) {
            HomeGraph
        } else {
            UnscopedDestinations.Onboarding
        }
    }
}
