package ru.livetyping.zarina.presentation.screen.productsubscription

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.operationtracker.OperationKey
import ru.livetyping.zarina.base.operationtracker.OperationTracker
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.common.Email
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.common.exception.ValidationException
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductOffer
import ru.livetyping.zarina.domain.user.exception.EmailValidationException
import ru.livetyping.zarina.domain.user.exception.EmptyEmailException
import ru.livetyping.zarina.domain.user.exception.EmptyFirstNameException
import ru.livetyping.zarina.domain.user.exception.FirstNameValidationException
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.model.product.ProductItemParcelable
import ru.livetyping.zarina.presentation.model.product.ProductOfferParcelable
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.screen.productsubscription.ProductSubscriptionViewModel.SideEffect
import ru.livetyping.zarina.usecase.product.SubscribeToProductUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class ProductSubscriptionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: ProductSubscriptionInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var subscribeToProductJob: Job? = null

    val product: StateFlow<Product> = savedStateHandle
        .getStateFlow<ProductItemParcelable?>(
            key = UnscopedDestinations.ProductSubscription.ARG_KEY_PRODUCT,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { parcelable ->
            checkNotNull(parcelable) { "product is null" }
            parcelable.toProductItem()
        }

    val productOffer: StateFlow<ProductOffer> = savedStateHandle
        .getStateFlow<ProductOfferParcelable?>(
            key = UnscopedDestinations.ProductSubscription.ARG_KEY_OFFER,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { parcelable ->
            checkNotNull(parcelable) { "productOffer is null" }
            parcelable.toProductOffer()
        }

    val firstName: StateFlow<String> = savedStateHandle.getStateFlow(
        key = KEY_FIRST_NAME,
        initialValue = "",
    )

    val email: StateFlow<String> = savedStateHandle.getStateFlow(
        key = KEY_EMAIL,
        initialValue = "",
    )

    val arePoliciesAccepted: StateFlow<Boolean> = savedStateHandle.getStateFlow(
        key = KEY_ARE_POLICIES_ACCEPTED,
        initialValue = false,
    )

    val isPoliciesErrorVisible: StateFlow<Boolean> = savedStateHandle.getStateFlow(
        key = KEY_IS_POLICIES_ERROR_VISIBLE,
        initialValue = false,
    )

    val isSubscribeButtonLoading: StateFlow<Boolean> = operationTracker
        .isOperationOngoing(Operation.SUBSCRIBE_TO_PRODUCT)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = false,
        )

    private val _isFirstNameInvalid = MutableStateFlow(false)
    val isFirstNameInvalid: StateFlow<Boolean> = _isFirstNameInvalid.asStateFlow()

    private val _isEmailInvalid = MutableStateFlow(false)
    val isEmailInvalid: StateFlow<Boolean> = _isEmailInvalid.asStateFlow()

    init {
        fillFieldsWithUser()
    }

    fun onBackClicked() {
        navigationThrottler.throttle {
            val result = ProductSubscriptionScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(result))
        }
    }

    fun onFirstNameChanged(name: String) {
        savedStateHandle[KEY_FIRST_NAME] = name
        _isFirstNameInvalid.value = false
    }

    fun onEmailChanged(email: String) {
        savedStateHandle[KEY_EMAIL] = email
        _isEmailInvalid.value = false
    }

    fun onUrlClicked(url: Url) {
        navigationThrottler.throttle {
            emitSideEffect(SideEffect.OpenUrl(url))
        }
    }

    fun onPoliciesAcceptedChanged(areAccepted: Boolean) {
        savedStateHandle[KEY_ARE_POLICIES_ACCEPTED] = areAccepted
        if (areAccepted) {
            savedStateHandle[KEY_IS_POLICIES_ERROR_VISIBLE] = false
        }
    }

    fun onSubscribeClicked() {
        if (subscribeToProductJob?.isActive == true) return

        if (!arePoliciesAccepted.value) {
            savedStateHandle[KEY_IS_POLICIES_ERROR_VISIBLE] = true
            val text = Text.Resource(R.string.product_subscription_agreement_error)
            val message = ZarinaToastMessage.error(text)
            emitSideEffect(SideEffect.ShowZarinaToast(message))
            return
        }

        subscribeToProductJob = viewModelScope.launch {
            operationTracker.track(Operation.SUBSCRIBE_TO_PRODUCT) {
                val params = SubscribeToProductUseCase.Params(
                    barcode = productOffer.value.barcode,
                    firstName = firstName.value,
                    email = Email.create(email.value),
                )
                interactor.subscribeToProduct(params)
                    .onSuccess {
                        val text = Text.Resource(R.string.product_subscription_completed)
                        val message = ZarinaToastMessage(text)
                        emitSideEffect(SideEffect.ShowZarinaToast(message))

                        val action = ProductSubscriptionScreenAction.SubscriptionCompleted
                        emitSideEffect(SideEffect.Navigate(action))
                    }
                    .onFailure(::onSubscribeFailure)
            }
        }
    }

    private fun onSubscribeFailure(e: Throwable) {
        if (e is ValidationException) {
            val exceptions = listOf(e) + e.suppressedExceptions

            val isFirstNameEmpty = exceptions.any { it is EmptyFirstNameException }
            val isEmailEmpty = exceptions.any { it is EmptyEmailException }
            val messageText = when {
                isFirstNameEmpty || isEmailEmpty -> {
                    Text.Resource(R.string.product_subscription_empty_fields_error)
                }

                else -> Text.Resource(R.string.incorrect_data_entered)
            }
            val message = ZarinaToastMessage.error(messageText)
            emitSideEffect(SideEffect.ShowZarinaToast(message))

            if (exceptions.any { it is FirstNameValidationException }) {
                _isFirstNameInvalid.value = true
            }
            if (exceptions.any { it is EmailValidationException }) {
                _isEmailInvalid.value = true
            }
        } else {
            val text = Text.Resource(R.string.something_went_wrong)
            val message = ZarinaToastMessage.error(text)
            emitSideEffect(SideEffect.ShowZarinaToast(message))
        }
    }

    private fun fillFieldsWithUser() {
        viewModelScope.launch {
            val userResult = interactor.getUserFlow().firstOrNull()
            val user = userResult?.getOrNull()
            if (user != null) {
                savedStateHandle[KEY_FIRST_NAME] = user.firstName.orEmpty()
                savedStateHandle[KEY_EMAIL] = user.email.value
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: ProductSubscriptionScreenAction) : SideEffect

        data class OpenUrl(val url: Url) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }

    private enum class Operation : OperationKey { SUBSCRIBE_TO_PRODUCT }

    companion object {
        private const val KEY_FIRST_NAME = "first_name"
        private const val KEY_EMAIL = "email"
        private const val KEY_ARE_POLICIES_ACCEPTED = "are_policies_accepted"
        private const val KEY_IS_POLICIES_ERROR_VISIBLE = "is_policies_error_visible"
    }
}
