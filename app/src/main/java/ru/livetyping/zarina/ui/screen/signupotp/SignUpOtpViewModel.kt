package ru.livetyping.zarina.ui.screen.signupotp

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.common.PhoneNumber
import ru.livetyping.zarina.ui.common.util.getNavigationThrottler
import ru.livetyping.zarina.ui.navigation.destination.graph.SignUpGraph
import ru.livetyping.zarina.ui.screen.signupotp.SignUpOtpViewModel.SideEffect
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class SignUpOtpViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: SignUpOtpInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    val phone: StateFlow<PhoneNumber> = savedStateHandle
        .getStateFlow<String?>(
            key = SignUpGraph.Otp.ARG_KEY_PHONE,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
        ) { string ->
            checkNotNull(string) { "phone is null" }
            PhoneNumber.create(string)
        }

    val otp: StateFlow<String> = savedStateHandle.getStateFlow(
        key = KEY_OTP,
        initialValue = "",
    )

    fun onBackClicked() {
        navigationThrottler.throttle {
            // TODO: [High] Implement
        }
    }

    fun onOtpChanged(otp: String) {
        savedStateHandle[KEY_OTP] = otp
    }

    sealed interface SideEffect : SideEffectSource.SideEffect

    companion object {
        private const val KEY_OTP = "otp"
    }
}
