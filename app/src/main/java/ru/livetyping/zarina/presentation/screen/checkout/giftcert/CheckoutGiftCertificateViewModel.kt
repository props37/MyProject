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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.operationtracker.OperationKey
import ru.livetyping.zarina.base.operationtracker.OperationTracker
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.giftcert.exception.EmptyGiftCertificateNumberException
import ru.livetyping.zarina.domain.giftcert.exception.EmptyGiftCertificateVerificationCodeException
import ru.livetyping.zarina.domain.giftcert.exception.GiftCertificateException
import ru.livetyping.zarina.domain.giftcert.exception.GiftCertificateReservedException
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.giftcert.CheckoutGiftCertificateViewModel.SideEffect
import ru.livetyping.zarina.usecase.giftcert.ApplyGiftCertificateUseCase
import ru.livetyping.zarina.util.compose.text.textAsFlow
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import java.io.IOException
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

    private val _isGiftCertificateNumberInvalid = MutableStateFlow(false)
    val isGiftCertificateNumberInvalid: StateFlow<Boolean> =
        _isGiftCertificateNumberInvalid.asStateFlow()

    private val _isGiftCertificateVerificationCodeInvalid = MutableStateFlow(false)
    val isGiftCertificateVerificationCodeInvalid: StateFlow<Boolean> =
        _isGiftCertificateVerificationCodeInvalid.asStateFlow()

    val isApplyButtonLoading: StateFlow<Boolean> = operationTracker
        .isOperationOngoing(ApplyGiftCertificateOperation)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = false,
        )

    init {
        giftCertificateNumberTextFieldState.textAsFlow()
            .onEach { _isGiftCertificateNumberInvalid.value = false }
            .launchIn(viewModelScope)

        giftCertificateVerificationCodeTextFieldState.textAsFlow()
            .onEach { _isGiftCertificateVerificationCodeInvalid.value = false }
            .launchIn(viewModelScope)
    }

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
                    .onFailure(::onApplyFailure)
            }
        }
    }

    private fun onApplyFailure(t: Throwable) {
        when (t) {
            is GiftCertificateException -> handleGiftCertificateException(t)
            is GiftCertificateReservedException -> {
                val messageText = Text.Resource(R.string.gift_certificate_reserved_error)
                val message = ZarinaToastMessage.error(messageText)
                emitSideEffect(SideEffect.ShowZarinaToast(message))
            }

            !is IOException -> {
                _isGiftCertificateNumberInvalid.value = true
                _isGiftCertificateVerificationCodeInvalid.value = true
                val messageText = Text.Resource(R.string.unknown_gift_certificate_error)
                val message = ZarinaToastMessage.error(messageText)
                emitSideEffect(SideEffect.ShowZarinaToast(message))
            }

            else -> {
                val messageText = Text.Resource(R.string.something_went_wrong_try_again)
                val message = ZarinaToastMessage.error(messageText)
                emitSideEffect(SideEffect.ShowZarinaToast(message))
            }
        }
    }

    private fun handleGiftCertificateException(t: GiftCertificateException) {
        val exceptions = listOf(t) + t.suppressedExceptions
        val isNumberEmpty = exceptions.any { it is EmptyGiftCertificateNumberException }
        val isVerificationCodeEmpty =
            exceptions.any { it is EmptyGiftCertificateVerificationCodeException }
        if (isNumberEmpty) {
            _isGiftCertificateNumberInvalid.value = true
        }
        if (isVerificationCodeEmpty) {
            _isGiftCertificateVerificationCodeInvalid.value = true
        }

        val messageTextResId = when {
            isNumberEmpty && isVerificationCodeEmpty -> R.string.gift_certificate_fields_empty_error
            isNumberEmpty -> R.string.gift_certificate_number_empty_error
            isVerificationCodeEmpty -> R.string.gift_certificate_verification_code_empty_error
            else -> R.string.unknown_gift_certificate_error
        }
        val messageText = Text.Resource(messageTextResId)
        val message = ZarinaToastMessage.error(messageText)
        emitSideEffect(SideEffect.ShowZarinaToast(message))
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: CheckoutGiftCertificateScreenAction) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }

    private object ApplyGiftCertificateOperation : OperationKey
}
