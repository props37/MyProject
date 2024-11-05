package ru.livetyping.zarina.presentation.screen.checkout.giftcert

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.base.operationtracker.OperationKey
import ru.livetyping.zarina.base.operationtracker.OperationTracker
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.giftcert.CheckoutGiftCertificateViewModel.SideEffect
import ru.livetyping.zarina.usecase.cart.ApplyGiftCertificateUseCase
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import javax.inject.Inject

@HiltViewModel
class CheckoutGiftCertificateViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: CheckoutGiftCertificateInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private val giftCertificateParams = savedStateHandle.toRoute<CheckoutGraph.GiftCertificate>(
        typeMap = CheckoutGraph.GiftCertificate.typeMap(),
    )

    private var applyGiftCertificateJob: Job? = null

    @OptIn(SavedStateHandleSaveableApi::class)
    val giftCertificateNumberTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    @OptIn(SavedStateHandleSaveableApi::class)
    val giftCertificateVerificationCodeTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    val isApplyButtonLoading: StateFlow<Boolean> = operationTracker
        .isOperationOngoing(ApplyGiftCertificateOperation)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = false,
        )

    fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = CheckoutGiftCertificateScreenAction.ScreenClosed(
                isGiftCertificateApplied = false,
            )
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onApplyClicked() {
        if (applyGiftCertificateJob?.isActive == true) return
        applyGiftCertificateJob = viewModelScope.launch {
            operationTracker.track(ApplyGiftCertificateOperation) {
                val params = ApplyGiftCertificateUseCase.Params(
                    certificateNumber = giftCertificateNumberTextFieldState.text.toString(),
                    certificateVerificationCode = giftCertificateVerificationCodeTextFieldState.text.toString(),
                    cartTotalPrice = giftCertificateParams.cartTotalPrice,
                    cartType = giftCertificateParams.cartType.toCartType(),
                )
                interactor.applyGiftCertificate(params)
                    .onSuccess {
                        val action = CheckoutGiftCertificateScreenAction.ScreenClosed(
                            isGiftCertificateApplied = true,
                        )
                        emitSideEffect(SideEffect.Navigate(action))
                    }
                    .onFailure {
                        // TODO: [High] Implement
                    }
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: CheckoutGiftCertificateScreenAction) : SideEffect
    }

    private object ApplyGiftCertificateOperation : OperationKey
}
