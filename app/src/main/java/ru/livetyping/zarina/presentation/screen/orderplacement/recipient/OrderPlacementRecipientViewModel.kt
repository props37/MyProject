package ru.livetyping.zarina.presentation.screen.orderplacement.recipient

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.presentation.common.savedstatehandle.createValueHolder
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.model.cart.CartTypeParcelable
import ru.livetyping.zarina.presentation.navigation.destination.graph.OrderPlacementGraph
import ru.livetyping.zarina.presentation.screen.orderplacement.recipient.OrderPlacementRecipientViewModel.SideEffect
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class OrderPlacementRecipientViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: OrderPlacementRecipientInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val cartType = savedStateHandle
        .getStateFlow<CartTypeParcelable?>(
            key = OrderPlacementGraph.Recipient.ARG_KEY_CART_TYPE,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { parcelable ->
            checkNotNull(parcelable) { "cartType is null" }
            parcelable.toCartType()
        }

    @OptIn(SavedStateHandleSaveableApi::class)
    val firstNameTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    @OptIn(SavedStateHandleSaveableApi::class)
    val lastNameTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    private val phoneValueHolder = savedStateHandle.createValueHolder(
        key = KEY_PHONE,
        initialValue = "",
    )
    val phone: StateFlow<String> = phoneValueHolder.stateFlow

    @OptIn(SavedStateHandleSaveableApi::class)
    val emailTextFieldState: TextFieldState by savedStateHandle.saveable(
        saver = TextFieldState.Saver,
        init = { TextFieldState() },
    )

    init {
        fetchUser()
    }

    fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = OrderPlacementRecipientScreenAction.OrderPlacementClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onPhoneChanged(phone: String) {
        phoneValueHolder.set(phone)
    }

    fun onContinueClicked() {
        // TODO: [High] Implement
    }

    private fun fetchUser() {
        viewModelScope.launch {
            val user = interactor.getUserFlow().firstOrNull()?.getOrNull()
            if (user != null) {
                firstNameTextFieldState.setTextAndPlaceCursorAtEnd(user.firstName.orEmpty())
                lastNameTextFieldState.setTextAndPlaceCursorAtEnd(user.lastName.orEmpty())
                phoneValueHolder.set(user.phone?.value.orEmpty())
                emailTextFieldState.setTextAndPlaceCursorAtEnd(user.email.value)
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: OrderPlacementRecipientScreenAction) : SideEffect
    }

    companion object {
        private const val KEY_PHONE = "phone"
    }
}
