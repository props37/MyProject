package ru.zarina.zarina.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.destinations.Destinations

@KoinViewModel
class AppViewModel(
    private val interactor: AppInteractor,
) : ViewModel() {

    val startDestination: Destination<*> = runBlocking {
        val isOnboardingCompleted = interactor.isOnboardingCompleted()
            .first()
        if (isOnboardingCompleted) Destinations.Home else Destinations.Onboarding
    }

}
