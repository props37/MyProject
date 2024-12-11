package ru.livetyping.zarina.feature.productsubscription.ui.impl.impl

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.common.Email
import ru.livetyping.zarina.core.domain.model.common.exception.CombinedValidationException
import ru.livetyping.zarina.core.domain.model.user.exception.EmailException
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyEmailException
import ru.livetyping.zarina.core.domain.model.user.exception.EmptyFirstNameException
import ru.livetyping.zarina.core.domain.model.user.exception.FirstNameException
import ru.livetyping.zarina.core.domain.usecase.product.SubscribeToProductUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserFlowUseCase
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage
import ru.livetyping.zarina.core.uicompose.textAsFlow
import ru.livetyping.zarina.feature.productsubscription.ui.api.ProductSubscriptionNavEntry
import ru.livetyping.zarina.feature.productsubscription.ui.impl.R
import ru.livetyping.zarina.feature.productsubscription.ui.impl.impl.model.ProductSubscriptionEvent
import ru.livetyping.zarina.feature.productsubscription.ui.impl.impl.model.ProductSubscriptionState
import javax.inject.Inject
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel
internal class ProductSubscriptionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserFlow: GetUserFlowUseCase,
    private val subscribeToProduct: SubscribeToProductUseCase,
) : ViewModel(), SideEffectSource<ProductSubscriptionSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

    private var subscribeJob: Job? = null

    private val navEntry = savedStateHandle.toRoute<ProductSubscriptionNavEntry>(
        typeMap = ProductSubscriptionNavEntry.typeMap(),
    )
    private val product = navEntry.product.toProductShort()
    private val productOffer = navEntry.offer.toProductOffer()

    @OptIn(SavedStateHandleSaveableApi::class)
    private val nameTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    @OptIn(SavedStateHandleSaveableApi::class)
    private val emailTextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val isNameInvalid = MutableStateFlow(false)

    private val isEmailInvalid = MutableStateFlow(false)

    val state: StateFlow<ProductSubscriptionState> = combine(
        isNameInvalid,
        isEmailInvalid,
        operationTracker.ongoingOperationKeys,
    ) { isNameInvalid, isEmailInvalid, ongoingOperations ->
        ProductSubscriptionState(
            product = product,
            productOffer = productOffer,
            nameTextFieldState = nameTextFieldState,
            isNameInvalid = isNameInvalid,
            emailTextFieldState = emailTextFieldState,
            isEmailInvalid = isEmailInvalid,
            isSubscribeButtonLoading = Operation.SUBSCRIBE in ongoingOperations,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = ProductSubscriptionState(
            product = product,
            productOffer = productOffer,
            nameTextFieldState = nameTextFieldState,
            isNameInvalid = isNameInvalid.value,
            emailTextFieldState = emailTextFieldState,
            isEmailInvalid = isEmailInvalid.value,
            isSubscribeButtonLoading = false,
        ),
    )

    init {
        updateWithUser()
        makeFieldsValidOnChange()
    }

    fun onEvent(event: ProductSubscriptionEvent) {
        when (event) {
            ProductSubscriptionEvent.BackClicked -> onBackClicked()
            ProductSubscriptionEvent.SubscribeClicked -> onSubscribeClicked()
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ProductSubscriptionScreenAction.BackClicked
            emitSideEffect(ProductSubscriptionSideEffect.Navigate(action))
        }
    }

    private fun onSubscribeClicked() {
        if (subscribeJob?.isActive == true) return

        // TODO: [Top] Check are policies accepted

        subscribeJob = viewModelScope.launch {
            operationTracker.track(Operation.SUBSCRIBE) {
                val params = SubscribeToProductUseCase.Params(
                    barcode = productOffer.barcode,
                    name = nameTextFieldState.text.toString(),
                    email = Email.create(emailTextFieldState.text.toString()),
                )
                subscribeToProduct(params)
                    .onSuccess {
                        val text = Text.Resource(R.string.product_subscription_completed)
                        val message = ZarinaToastMessage(text)
                        emitSideEffect(ProductSubscriptionSideEffect.ShowZarinaToast(message))

                        val action = ProductSubscriptionScreenAction.SubscriptionCompleted
                        emitSideEffect(ProductSubscriptionSideEffect.Navigate(action))
                    }
                    .onFailure(::handleProductSubscriptionException)
            }
        }
    }

    private fun handleProductSubscriptionException(t: Throwable) {
        when (t) {
            is CombinedValidationException -> {
                handleProductSubscriptionCombinedValidationException(t)
            }

            is FirstNameException -> {
                isNameInvalid.value = true
                val textResId = when (t) {
                    is EmptyFirstNameException -> R.string.product_subscription_empty_fields_error
                    else -> RCommon.string.res_incorrect_data_entered
                }
                showZarinaErrorToast(Text.Resource(textResId))
            }

            is EmailException -> {
                isEmailInvalid.value = true
                val textResId = when (t) {
                    is EmptyEmailException -> R.string.product_subscription_empty_fields_error
                    else -> RCommon.string.res_incorrect_data_entered
                }
                showZarinaErrorToast(Text.Resource(textResId))
            }

            else -> {
                val text = Text.Resource(RCommon.string.res_something_went_wrong)
                showZarinaErrorToast(text)
            }
        }
    }

    private fun handleProductSubscriptionCombinedValidationException(
        e: CombinedValidationException,
    ) {
        val causes = e.causes
        causes.forEach { cause ->
            when (cause) {
                is FirstNameException -> isNameInvalid.value = true
                is EmailException -> isEmailInvalid.value = true
            }
        }

        val isNameEmpty = causes.any { it is EmptyFirstNameException }
        val isEmailEmpty = causes.any { it is EmptyEmailException }

        val textResId = if (isNameEmpty || isEmailEmpty) {
            R.string.product_subscription_empty_fields_error
        } else {
            RCommon.string.res_incorrect_data_entered
        }
        val text = Text.Resource(textResId)
        showZarinaErrorToast(text)
    }

    private fun updateWithUser() {
        viewModelScope.launch {
            val params = GetUserFlowUseCase.Params(CachePolicy.LocalOnly)
            val userResult = getUserFlow(params).firstOrNull()
            val user = userResult?.getOrNull()
            if (user != null) {
                nameTextFieldState.setTextAndPlaceCursorAtEnd(user.firstName.orEmpty())
                emailTextFieldState.setTextAndPlaceCursorAtEnd(user.email.value)
            }
        }
    }

    private fun makeFieldsValidOnChange() {
        nameTextFieldState.textAsFlow()
            .onEach { isNameInvalid.value = false }
            .launchIn(viewModelScope)
        emailTextFieldState.textAsFlow()
            .onEach { isEmailInvalid.value = false }
            .launchIn(viewModelScope)
    }

    private fun showZarinaErrorToast(text: Text) {
        val message = ZarinaToastMessage.error(text)
        emitSideEffect(ProductSubscriptionSideEffect.ShowZarinaToast(message))
    }

    private enum class Operation : OperationKey { SUBSCRIBE }
}
