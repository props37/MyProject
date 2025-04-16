package ru.livetyping.zarina.feature.cart.ui.impl.impl.giftcert

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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.model.common.exception.CombinedException
import ru.livetyping.zarina.core.domain.model.giftcert.GiftCertificate
import ru.livetyping.zarina.core.domain.model.giftcert.exception.EmptyGiftCertificateNumberException
import ru.livetyping.zarina.core.domain.model.giftcert.exception.EmptyGiftCertificateVerificationCodeException
import ru.livetyping.zarina.core.domain.model.giftcert.exception.GiftCertificateException
import ru.livetyping.zarina.core.domain.model.giftcert.exception.GiftCertificateReservedException
import ru.livetyping.zarina.core.domain.usecase.checkout.ApplyGiftCertificateUseCase
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage
import ru.livetyping.zarina.core.uicompose.textAsFlow
import ru.livetyping.zarina.feature.cart.ui.impl.R
import ru.livetyping.zarina.feature.cart.ui.impl.impl.giftcert.model.GiftCertificateState
import java.io.IOException
import javax.inject.Inject
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel
internal class GiftCertificateViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deps: GiftCertificateDependencies,
) : ViewModel(), SideEffectSource<GiftCertificateSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private val navEntry = savedStateHandle.toRoute<GiftCertificateNavEntry>(
        typeMap = GiftCertificateNavEntry.typeMap(),
    )

    private var applyGiftCertificateJob: Job? = null

    @OptIn(SavedStateHandleSaveableApi::class)
    private val giftCertificateNumberTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    @OptIn(SavedStateHandleSaveableApi::class)
    private val giftCertificateVerificationCodeTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val isGiftCertificateNumberInvalid = MutableStateFlow(false)

    private val isGiftCertificateVerificationCodeInvalid = MutableStateFlow(false)

    private val giftCertificateInitialState = GiftCertificateState(
        giftCertificateNumberTextFieldState = giftCertificateNumberTextFieldState,
        giftCertificateVerificationCodeTextFieldState = giftCertificateVerificationCodeTextFieldState,
        isGiftCertificateNumberInvalid = false,
        isGiftCertificateVerificationCodeInvalid = false,
        isApplyButtonLoading = false,
    )
    val giftCertificateState: StateFlow<GiftCertificateState> = combine(
        isGiftCertificateNumberInvalid,
        isGiftCertificateVerificationCodeInvalid,
        operationTracker.ongoingOperationKeys,
    ) { isNumberInvalid, isVerificationCodeInvalid, ongoingOperations ->
        GiftCertificateState(
            giftCertificateNumberTextFieldState = giftCertificateNumberTextFieldState,
            giftCertificateVerificationCodeTextFieldState = giftCertificateVerificationCodeTextFieldState,
            isGiftCertificateNumberInvalid = isNumberInvalid,
            isGiftCertificateVerificationCodeInvalid = isVerificationCodeInvalid,
            isApplyButtonLoading = ApplyGiftCertificateOperation in ongoingOperations,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = giftCertificateInitialState,
    )

    init {
        makeFieldsValidOnChange()
    }

    fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = GiftCertificateScreenAction.BackClicked
            emitSideEffect(GiftCertificateSideEffect.Navigate(action))
        }
    }

    fun onApplyClicked() {
        if (applyGiftCertificateJob?.isActive == true) return
        applyGiftCertificateJob = viewModelScope.launch {
            operationTracker.track(ApplyGiftCertificateOperation) {
                val giftCertificateNumber = GiftCertificate.Number(
                    value = giftCertificateNumberTextFieldState.text.toString().trim(),
                )
                val giftCertificate = GiftCertificate(
                    number = giftCertificateNumber,
                    verificationCode = giftCertificateVerificationCodeTextFieldState.text.toString(),
                )
                val params = ApplyGiftCertificateUseCase.Params(
                    giftCertificate = giftCertificate,
                    cartFinalPrice = navEntry.cartFinalPrice,
                    cartType = navEntry.cartType.toCartType(),
                )
                deps.applyGiftCertificate(params)
                    .onSuccess {
                        val action = GiftCertificateScreenAction.GiftCertificateApplied
                        emitSideEffect(GiftCertificateSideEffect.Navigate(action))
                    }
                    .onFailure(::onApplyFailure)
            }
        }
    }

    private fun onApplyFailure(t: Throwable) {
        when (t) {
            is GiftCertificateException -> handleGiftCertificateException(t)
            is CombinedException -> handleCombinedException(t)
            !is IOException -> {
                isGiftCertificateNumberInvalid.value = true
                isGiftCertificateVerificationCodeInvalid.value = true
                val text = Text.Resource(R.string.cart_unknown_gift_certificate_error)
                showZarinaErrorToast(text)
            }

            else -> {
                val messageText = Text.Resource(RCommon.string.res_something_went_wrong)
                val message = ZarinaToastMessage.error(messageText)
                emitSideEffect(GiftCertificateSideEffect.ShowZarinaToast(message))
            }
        }
    }

    private fun handleGiftCertificateException(e: GiftCertificateException) {
        when (e) {
            is EmptyGiftCertificateNumberException -> {
                isGiftCertificateNumberInvalid.value = true
                val text = Text.Resource(R.string.cart_gift_certificate_number_empty_error)
                showZarinaErrorToast(text)
            }

            is EmptyGiftCertificateVerificationCodeException -> {
                isGiftCertificateVerificationCodeInvalid.value = true
                val text = Text.Resource(R.string.cart_gift_certificate_verification_code_empty_error)
                showZarinaErrorToast(text)
            }

            is GiftCertificateReservedException -> {
                val text = Text.Resource(R.string.cart_cart_gift_certificate_reserved_error)
                showZarinaErrorToast(text)
            }

            else -> {
                val text = Text.Resource(R.string.cart_unknown_gift_certificate_error)
                showZarinaErrorToast(text)
            }
        }
    }

    private fun handleCombinedException(e: CombinedException) {
        val causes = e.causes
        val isNumberEmpty = causes.any { it is EmptyGiftCertificateNumberException }
        val isVerificationCodeEmpty =
            causes.any { it is EmptyGiftCertificateVerificationCodeException }
        if (isNumberEmpty) {
            isGiftCertificateNumberInvalid.value = true
        }
        if (isVerificationCodeEmpty) {
            isGiftCertificateVerificationCodeInvalid.value = true
        }

        val isGiftCertificateReserved = causes.any { it is GiftCertificateReservedException }
        val textResId = when {
            isGiftCertificateReserved -> R.string.cart_cart_gift_certificate_reserved_error
            isNumberEmpty && isVerificationCodeEmpty -> R.string.cart_gift_certificate_fields_empty_error
            isNumberEmpty -> R.string.cart_gift_certificate_number_empty_error
            isVerificationCodeEmpty -> R.string.cart_gift_certificate_verification_code_empty_error
            else -> R.string.cart_unknown_gift_certificate_error
        }
        showZarinaErrorToast(Text.Resource(textResId))
    }

    private fun showZarinaErrorToast(text: Text) {
        val message = ZarinaToastMessage.error(text)
        emitSideEffect(GiftCertificateSideEffect.ShowZarinaToast(message))
    }

    private fun makeFieldsValidOnChange() {
        giftCertificateNumberTextFieldState.textAsFlow()
            .onEach { isGiftCertificateNumberInvalid.value = false }
            .launchIn(viewModelScope)

        giftCertificateVerificationCodeTextFieldState.textAsFlow()
            .onEach { isGiftCertificateVerificationCodeInvalid.value = false }
            .launchIn(viewModelScope)
    }

    private object ApplyGiftCertificateOperation : OperationKey
}
