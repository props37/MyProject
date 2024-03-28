package ru.zarina.zarina.ui.screen.productsubscription

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.zarina.zarina.R
import ru.zarina.zarina.base.operationtracker.OperationKey
import ru.zarina.zarina.base.operationtracker.OperationTracker
import ru.zarina.zarina.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.base.throttler.Throttler
import ru.zarina.zarina.domain.common.Url
import ru.zarina.zarina.domain.common.exception.ValidationException
import ru.zarina.zarina.domain.exception.InvalidEmailException
import ru.zarina.zarina.domain.exception.InvalidFirstNameException
import ru.zarina.zarina.domain.product.Product
import ru.zarina.zarina.domain.product.ProductOffer
import ru.zarina.zarina.ui.base.text.Text
import ru.zarina.zarina.ui.common.util.getNavigationThrottler
import ru.zarina.zarina.ui.common.zarinatoast.ZarinaToastMessage
import ru.zarina.zarina.ui.common.zarinatoast.ZarinaToastMessageStyle
import ru.zarina.zarina.ui.model.product.ProductOfferParcelable
import ru.zarina.zarina.ui.model.product.ProductParcelable
import ru.zarina.zarina.ui.navigation.destination.UnscopedDestinations
import ru.zarina.zarina.ui.screen.productsubscription.ProductSubscriptionViewModel.SideEffect
import ru.zarina.zarina.usecase.product.SubscribeToProductUseCase
import ru.zarina.zarina.util.library.coroutines.WhileUiSubscribed
import ru.zarina.zarina.util.library.coroutines.mapState
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
        .getStateFlow<ProductParcelable?>(
            key = UnscopedDestinations.ProductSubscription.ARG_KEY_PRODUCT,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { parcelable ->
            checkNotNull(parcelable) { "product is null" }
            parcelable.toProduct()
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
    }

    fun onSubscribeClicked() {
        if (subscribeToProductJob?.isActive == true) return

        if (!arePoliciesAccepted.value) {
            savedStateHandle[KEY_IS_POLICIES_ERROR_VISIBLE] = true
            val messageText = Text.Resource(R.string.product_subscription_agreement_error)
            val message = ZarinaToastMessage(
                text = messageText,
                style = ZarinaToastMessageStyle.ERROR,
            )
            emitSideEffect(SideEffect.ShowZarinaToast(message))
            return
        }

        subscribeToProductJob = viewModelScope.launch {
            operationTracker.track(Operation.SUBSCRIBE_TO_PRODUCT) {
                val params = SubscribeToProductUseCase.Params(
                    barcode = productOffer.value.barcode,
                    name = firstName.value,
                    email = email.value,
                )
                interactor.subscribeToProduct(params)
                    .onSuccess {
                        val messageText = Text.Resource(R.string.product_subscription_completed)
                        val message = ZarinaToastMessage(messageText)
                        emitSideEffect(SideEffect.ShowZarinaToast(message))

                        val action = ProductSubscriptionScreenAction.SubscriptionCompleted
                        emitSideEffect(SideEffect.Navigate(action))
                    }
                    .onFailure { e ->
                        if (e is ValidationException) {
                            val messageText = Text.Resource(R.string.incorrect_data)
                            val message = ZarinaToastMessage(
                                text = messageText,
                                style = ZarinaToastMessageStyle.ERROR,
                            )
                            emitSideEffect(SideEffect.ShowZarinaToast(message))

                            val exceptions = listOf(e) + e.suppressedExceptions
                            if (exceptions.any { it is InvalidFirstNameException }) {
                                _isFirstNameInvalid.value = true
                            }
                            if (exceptions.any { it is InvalidEmailException }) {
                                _isEmailInvalid.value = true
                            }
                        } else {
                            val message = Text.Resource(R.string.something_went_wrong)
                            emitSideEffect(SideEffect.ShowToast(message))
                        }
                    }
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: ProductSubscriptionScreenAction) : SideEffect

        data class OpenUrl(val url: Url) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect

        data class ShowToast(val message: Text) : SideEffect
    }

    private enum class Operation : OperationKey { SUBSCRIBE_TO_PRODUCT }

    companion object {
        private const val KEY_FIRST_NAME = "first_name"
        private const val KEY_EMAIL = "email"
        private const val KEY_ARE_POLICIES_ACCEPTED = "are_policies_accepted"
        private const val KEY_IS_POLICIES_ERROR_VISIBLE = "is_policies_error_visible"
    }
}
