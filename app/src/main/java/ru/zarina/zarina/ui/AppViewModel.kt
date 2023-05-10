package ru.zarina.zarina.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import ru.zarina.zarina.ui.navigation.base.Destination
import ru.zarina.zarina.ui.navigation.destinations.Destinations
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val interactor: AppInteractor,
) : ViewModel() {

    val startDestination: Destination<*> = runBlocking {
        val isOnboardingCompleted = interactor.isOnboardingCompleted()
            .first()
        if (isOnboardingCompleted) Destinations.Home else Destinations.Onboarding
    }

}
