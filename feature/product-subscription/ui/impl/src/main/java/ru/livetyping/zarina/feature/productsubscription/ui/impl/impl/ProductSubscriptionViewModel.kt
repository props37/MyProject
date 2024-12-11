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
import ru.livetyping.zarina.core.domain.usecase.user.GetUserFlowUseCase
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicompose.textAsFlow
import ru.livetyping.zarina.feature.productsubscription.ui.api.ProductSubscriptionNavEntry
import ru.livetyping.zarina.feature.productsubscription.ui.impl.impl.model.ProductSubscriptionEvent
import ru.livetyping.zarina.feature.productsubscription.ui.impl.impl.model.ProductSubscriptionState
import javax.inject.Inject

@HiltViewModel
internal class ProductSubscriptionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserFlow: GetUserFlowUseCase,
) : ViewModel(), SideEffectSource<ProductSubscriptionSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val operationTracker = OperationTracker()

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
            ProductSubscriptionEvent.SubscribeClicked -> TODO()
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ProductSubscriptionScreenAction.BackClicked
            emitSideEffect(ProductSubscriptionSideEffect.Navigate(action))
        }
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

    private enum class Operation : OperationKey { SUBSCRIBE }
}
