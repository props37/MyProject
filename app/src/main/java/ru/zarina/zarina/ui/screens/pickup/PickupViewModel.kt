package ru.zarina.zarina.ui.screens.pickup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.Offer
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.Stock
import ru.zarina.zarina.domain.exception.NotFoundException
import ru.zarina.zarina.domain.exception.validation.EmptyException
import ru.zarina.zarina.domain.exception.validation.FormatException
import ru.zarina.zarina.domain.exception.validation.IllegalContentsException
import ru.zarina.zarina.domain.exception.validation.TooLongException
import ru.zarina.zarina.ui.common.base.FocusState
import ru.zarina.zarina.ui.common.base.Text
import ru.zarina.zarina.ui.common.base.operation.OperationKey
import ru.zarina.zarina.ui.common.base.operation.OperationTracker
import ru.zarina.zarina.ui.navigation.destinations.Pickup
import ru.zarina.zarina.utils.coroutine.mapState
import ru.zarina.zarina.utils.isNetworkException
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PickupViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: PickupInteractor,
) : ViewModel() {

    private val operationTracker = OperationTracker()

    private val productId = savedStateHandle.getStateFlow(
        key = Pickup.ARGUMENT_PRODUCT_ID,
        initialValue = ""
    ).mapState(viewModelScope) { Product.Id(it) }

    val city = savedStateHandle.getStateFlow<City?>(KEY_SELECTED_CITY, null)

    private val _product = MutableStateFlow<Result<Product?>?>(null)
    val product = _product
        .mapState(viewModelScope) { it?.getOrNull() }

    private val offersReloadTrigger = MutableSharedFlow<Unit>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
        .apply { tryEmit(Unit) }
    private val _offers = MutableStateFlow<Result<List<Offer>>?>(null)
    val offers = _offers
        .mapState(viewModelScope) {
            it?.getOrNull()?.toPersistentList() ?: persistentListOf()
        }

    private val stockReloadTrigger = MutableSharedFlow<Unit>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
        .apply { tryEmit(Unit) }
    private val _stocks = MutableStateFlow<Result<List<Stock>>?>(null)
    val stocks = _stocks
        .map { it?.getOrNull()?.toPersistentList() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val isStocksLoaderVisible = operationTracker
        .isOperationOngoing(
            Operation.LOADING_CITY,
            Operation.LOADING_PRODUCT,
            Operation.LOADING_SIZES,
            Operation.LOADING_STOCKS,
        )
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val errorType = combine(
        _product,
        _offers,
        _stocks,
    ) { product, offers, stocks ->

        val exception = listOf(product, offers, stocks)
            .firstNotNullOfOrNull { it?.exceptionOrNull() }

        when {
            exception == null -> null
            exception.isNetworkException() -> ErrorType.NETWORK
            else -> ErrorType.GENERIC
        }
    }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val selectedOffer = savedStateHandle.getStateFlow<Offer?>(KEY_SELECTED_OFFER, null)
    val selectedShop = savedStateHandle.getStateFlow<Stock?>(KEY_SELECTED_SHOP, null)

    val surname = savedStateHandle.getStateFlow(KEY_SURNAME, "")
    private val surnameFocusState = MutableStateFlow(FocusState())
    private val surnameValidation = surname
        .map { interactor.validateName(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val surnameError: StateFlow<Text?> =
        combine(surnameValidation, surnameFocusState) { surnameValidation, focusState ->
            val exception = surnameValidation?.exceptionOrNull()
            when {
                exception is TooLongException ->
                    Text.Resource(R.string.max_length_symbols, exception.maxLength)

                exception is EmptyException && focusState.everLostFocus -> Text.Resource(R.string.field_should_be_filled)
                exception is IllegalContentsException -> Text.Resource(R.string.allowed_symbols)
                else -> null
            }
        }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val name = savedStateHandle.getStateFlow(KEY_NAME, "")
    private val nameFocusState = MutableStateFlow(FocusState())
    private val nameValidation = name
        .map { interactor.validateName(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val nameError: StateFlow<Text?> =
        combine(nameValidation, nameFocusState) { nameValidation, focusState ->
            val exception = nameValidation?.exceptionOrNull()
            when {
                exception is TooLongException -> Text.Resource(
                    R.string.max_length_symbols,
                    exception.maxLength
                )

                exception is EmptyException && focusState.everLostFocus -> Text.Resource(R.string.field_should_be_filled)
                exception is IllegalContentsException -> Text.Resource(R.string.allowed_symbols)
                else -> null
            }
        }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val phone = savedStateHandle.getStateFlow(KEY_PHONE, "+7")
    private val phoneFocusState = MutableStateFlow(FocusState())
    private val phoneValidation = phone
        .map { interactor.validatePhone(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val phoneError: StateFlow<Text?> =
        combine(phoneValidation, phoneFocusState) { phoneValidation, focusState ->
            val exception = phoneValidation?.exceptionOrNull()
            when {
                exception is EmptyException && focusState.everLostFocus -> Text.Resource(R.string.field_should_be_filled)
                exception is FormatException && focusState.everLostFocus -> Text.Resource(R.string.illegal_phone_format)
                else -> null
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val email = savedStateHandle.getStateFlow(KEY_EMAIL, "")
    private val emailFocusState = MutableStateFlow(FocusState())
    private val emailValidation = email
        .map { interactor.validateEmail(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val emailError: StateFlow<Text?> =
        combine(emailValidation, emailFocusState) { emailValidation, focusState ->
            val exception = emailValidation?.exceptionOrNull()
            when {
                exception is EmptyException && focusState.everLostFocus -> Text.Resource(R.string.field_should_be_filled)
                exception is FormatException && focusState.everLostFocus -> Text.Resource(R.string.illegal_email_format)
                else -> null
            }
        }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val isReserveButtonEnabled = combine(
        nameValidation,
        surnameValidation,
        emailValidation,
        phoneValidation
    ) { validations -> validations.all { it?.isSuccess == true } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        loadUserCity()
        setupStockLoading()
        setupSizeUpdates()
        setupProductLoading()
    }

    fun onOfferClick(offer: Offer) {
        savedStateHandle[KEY_SELECTED_OFFER] = offer
    }

    fun onStockPickupClick(stock: Stock) {
        savedStateHandle[KEY_SELECTED_SHOP] = stock
    }

    fun onRefreshClick() {
        when {
            _product.value?.isFailure == true -> loadProduct(productId.value)
            _offers.value?.isFailure == true -> offersReloadTrigger.tryEmit(Unit)
            _stocks.value?.isFailure == true -> stockReloadTrigger.tryEmit(Unit)
        }
    }

    fun onCityClick(city: City) {
        savedStateHandle[KEY_SELECTED_CITY] = city
    }

    fun onSurnameChange(surname: String) {
        savedStateHandle[KEY_SURNAME] = surname
    }

    fun onSurnameFocusChange(isFocused: Boolean) {
        surnameFocusState.update { it.updated(isFocused) }
    }

    fun onNameChange(name: String) {
        savedStateHandle[KEY_NAME] = name
    }

    fun onNameFocusChange(isFocused: Boolean) {
        nameFocusState.update { it.updated(isFocused) }
    }

    fun onPhoneChange(phone: String) {
        savedStateHandle[KEY_PHONE] = phone
    }

    fun onPhoneFocusChange(isFocused: Boolean) {
        phoneFocusState.update { it.updated(isFocused) }
    }

    fun onEmailChange(email: String) {
        savedStateHandle[KEY_EMAIL] = email
    }

    fun onEmailFocusChange(isFocused: Boolean) {
        emailFocusState.update { it.updated(isFocused) }
    }

    fun onReserveClick() {
        viewModelScope.launch {
            operationTracker.track(Operation.RESERVING) {
                // TODO loader
                // TODO error
                // TODO navigation
                interactor.reserve(
                    offer = selectedOffer.value ?: return@track,
                    shop = selectedShop.value?.shop ?: return@track,
                    surname = surname.value,
                    name = name.value,
                    email = email.value,
                    phone = phone.value,
                )
            }
        }
    }

    private fun loadProduct(id: Product.Id) {
        viewModelScope.launch {
            operationTracker.track(Operation.LOADING_PRODUCT) {
                _product.value = interactor.getProduct(id)
            }
        }
    }

    private fun loadUserCity() {
        if (city.value == null)
            viewModelScope.launch {
                operationTracker.track(Operation.LOADING_CITY) {
                    savedStateHandle[KEY_SELECTED_CITY] = interactor.getCity().getOrNull()
                }
            }
    }

    private fun setupStockLoading() {
        combine(selectedOffer, city, stockReloadTrigger) { offer, city, _ ->
            if (offer == null || city == null) return@combine
            operationTracker.track(Operation.LOADING_STOCKS) {
                _stocks.value = interactor.getStocks(offer, city)
                    .recoverCatching { throwable ->
                        when (throwable) {
                            is NotFoundException -> persistentListOf()
                            else -> throw throwable
                        }
                    }
            }
        }
            .launchIn(viewModelScope)
    }

    private fun setupSizeUpdates() {
        combine(_product, city, offersReloadTrigger) { productResult, city, _ ->
            val product = productResult?.getOrNull()
            if (product == null || city == null) return@combine
            operationTracker.track(Operation.LOADING_SIZES) {
                _offers.value = interactor.getOffers(product, city)
            }
        }
            .launchIn(viewModelScope)

        _offers
            .onEach { offersResult ->
                val offers = offersResult?.getOrNull() ?: return@onEach
                val selectedOffer = selectedOffer.value
                if (selectedOffer !in offers || selectedOffer == null) {
                    val availableOffer = offers.find { it.isAvailable }
                    val firstOffer = offers.firstOrNull()
                    savedStateHandle[KEY_SELECTED_OFFER] = availableOffer ?: firstOffer
                }
            }
            .launchIn(viewModelScope)
    }

    private fun setupProductLoading() {
        productId
            .mapLatest { id -> loadProduct(id) }
            .launchIn(viewModelScope)
    }

    enum class ErrorType { NETWORK, GENERIC }

    enum class Operation : OperationKey {
        LOADING_CITY, LOADING_PRODUCT, LOADING_SIZES, LOADING_STOCKS, RESERVING
    }

    companion object {
        private const val KEY_SELECTED_CITY = "selected_city"
        private const val KEY_SELECTED_OFFER = "selected_offer"
        private const val KEY_SELECTED_SHOP = "selected_shop"
        private const val KEY_SURNAME = "surname"
        private const val KEY_NAME = "name"
        private const val KEY_PHONE = "phone"
        private const val KEY_EMAIL = "email"
    }

}
