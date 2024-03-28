package ru.zarina.zarina.ui.screens.pickup.details

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.old.Shop
import ru.zarina.zarina.ui.base.text.Text
import ru.zarina.zarina.ui.base.text.textString
import ru.zarina.zarina.ui.common.behavior.navigationbar.NavigationBarState
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.buttons.ZarinaTextButton
import ru.zarina.zarina.ui.common.components.form.Input
import ru.zarina.zarina.ui.common.components.form.SectionHeader
import ru.zarina.zarina.ui.common.components.toolbar.BackButton
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.providers.domain.ShopProvider
import ru.zarina.zarina.ui.common.utils.adaptPhoneValue
import ru.zarina.zarina.ui.screens.pickup.PickupViewModel
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.old.ZarinaTheme
import ru.zarina.zarina.utils.compose.PhoneVisualTransformation
import ru.zarina.zarina.utils.compose.autofill
import ru.zarina.zarina.utils.compose.navigationOrIme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DetailsScreenContent(
    surname: String,
    surnameError: Text?,
    onSurnameChange: (String) -> Unit,
    onSurnameFocusChange: (Boolean) -> Unit,
    name: String,
    nameError: Text?,
    onNameChange: (String) -> Unit,
    onNameFocusChange: (Boolean) -> Unit,
    phone: String,
    phoneError: Text?,
    onPhoneChange: (String) -> Unit,
    onPhoneFocusChange: (Boolean) -> Unit,
    email: String,
    emailError: Text?,
    onEmailChange: (String) -> Unit,
    onEmailFocusChange: (Boolean) -> Unit,
    shop: Shop?,
    onReserveClick: () -> Unit,
    onBackClick: () -> Unit,
    isReserveButtonEnabled: Boolean,
    isLoaderVisible: Boolean,
) {
    val scrollState = rememberScrollState()
    ZarinaScaffold(
        toolbar = {
            ScreenToolbar(
                title = stringResource(id = R.string.pickup_at_shop),
                startIcon = {
                    BackButton(onClick = onBackClick)
                },
                isElevated = scrollState.canScrollBackward,
            )
        },
        isModalLoaderVisible = isLoaderVisible,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            val coroutineScope = rememberCoroutineScope()
            val reserveRequester = remember { BringIntoViewRequester() }
            RecipientInformation(
                surname = surname,
                surnameError = surnameError,
                onSurnameChange = onSurnameChange,
                onSurnameFocusChange = onSurnameFocusChange,
                name = name,
                nameError = nameError,
                onNameChange = onNameChange,
                onNameFocusChange = onNameFocusChange,
                phone = phone,
                phoneError = phoneError,
                onPhoneChange = onPhoneChange,
                onPhoneFocusChange = onPhoneFocusChange,
                email = email,
                emailError = emailError,
                onEmailChange = onEmailChange,
                onEmailFocusChange = onEmailFocusChange,
                onContinue = {
                    coroutineScope.launch {
                        reserveRequester.bringIntoView()
                    }
                }
            )
            OrderInformation(
                shop = shop
            )
            Spacer(modifier = Modifier.height(8.dp))
            ZarinaTextButton(
                text = stringResource(id = R.string.place_order),
                onClick = onReserveClick,
                isEnabled = isReserveButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .bringIntoViewRequester(reserveRequester)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 24.dp)
                    .padding(WindowInsets.navigationOrIme.asPaddingValues()),
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ColumnScope.RecipientInformation(
    surname: String,
    surnameError: Text?,
    onSurnameChange: (String) -> Unit,
    onSurnameFocusChange: (Boolean) -> Unit,
    name: String,
    nameError: Text?,
    onNameChange: (String) -> Unit,
    onNameFocusChange: (Boolean) -> Unit,
    phone: String,
    phoneError: Text?,
    onPhoneChange: (String) -> Unit,
    onPhoneFocusChange: (Boolean) -> Unit,
    email: String,
    emailError: Text?,
    onEmailChange: (String) -> Unit,
    onEmailFocusChange: (Boolean) -> Unit,
    onContinue: () -> Unit,
) {
    SectionHeader(
        text = stringResource(id = R.string.recipient_information),
        modifier = Modifier.fillMaxWidth(),
    )
    Input(
        value = surname,
        onValueChange = onSurnameChange,
        hint = stringResource(id = R.string.surname),
        isError = surnameError != null,
        error = surnameError?.let { textString(it) },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next,
        ),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
            .onFocusChanged { onSurnameFocusChange(it.hasFocus) }
            .fillMaxWidth()
            .autofill(listOf(AutofillType.PersonLastName), onSurnameChange),
    )
    Input(
        value = name,
        onValueChange = onNameChange,
        hint = stringResource(id = R.string.first_name),
        isError = nameError != null,
        error = nameError?.let { textString(it) },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next,
        ),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
            .onFocusChanged { onNameFocusChange(it.hasFocus) }
            .fillMaxWidth()
            .autofill(listOf(AutofillType.PersonFirstName), onNameChange),
    )
    Input(
        value = phone,
        onValueChange = { onPhoneChange(adaptPhoneValue(it)) },
        hint = stringResource(id = R.string.phone),
        isError = phoneError != null,
        error = phoneError?.let { textString(it) },
        visualTransformation = PhoneVisualTransformation,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next,
        ),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
            .onFocusChanged { onPhoneFocusChange(it.hasFocus) }
            .fillMaxWidth()
            .autofill(listOf(AutofillType.PhoneNumber), onPhoneChange),
    )
    val focusManager = LocalFocusManager.current
    Input(
        value = email,
        onValueChange = onEmailChange,
        hint = stringResource(id = R.string.email),
        isError = emailError != null,
        error = emailError?.let { textString(it) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
        ),
        keyboardActions = KeyboardActions {
            focusManager.clearFocus()
            onContinue()
        },
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
            .onFocusChanged { onEmailFocusChange(it.hasFocus) }
            .fillMaxWidth()
            .autofill(listOf(AutofillType.EmailAddress), onEmailChange),
    )
}

@Composable
private fun ColumnScope.OrderInformation(
    shop: Shop?,
) {
    SectionHeader(
        text = stringResource(R.string.order_information),
        modifier = Modifier.fillMaxWidth(),
    )
    if (shop != null) {
        Text(
            text = stringResource(R.string.shop),
            style = UiKitTheme.typographyOld.circle1518bold,
            color = UiKitTheme.colorsOld.primaryContentColor,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${shop.name}, ${shop.address}",
            style = UiKitTheme.typographyOld.circle1718,
            color = UiKitTheme.colorsOld.primaryContentColor,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.working_schedule_template, shop.schedule),
            style = UiKitTheme.typographyOld.circle1718,
            color = UiKitTheme.colorsOld.primaryContentColor,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
    Text(
        text = stringResource(R.string.reservation_period),
        style = UiKitTheme.typographyOld.circle1518bold,
        color = UiKitTheme.colorsOld.primaryContentColor,
        textAlign = TextAlign.Start,
        modifier = Modifier.padding(horizontal = 16.dp),
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = stringResource(R.string.two_days),
        style = UiKitTheme.typographyOld.circle1718,
        color = UiKitTheme.colorsOld.primaryContentColor,
        textAlign = TextAlign.Start,
        modifier = Modifier.padding(horizontal = 16.dp),
    )
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = stringResource(R.string.payment_method),
        style = UiKitTheme.typographyOld.circle1518bold,
        color = UiKitTheme.colorsOld.primaryContentColor,
        textAlign = TextAlign.Start,
        modifier = Modifier.padding(horizontal = 16.dp),
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = stringResource(R.string.in_cash_or_by_card_upon_receiving),
        style = UiKitTheme.typographyOld.circle1718,
        color = UiKitTheme.colorsOld.primaryContentColor,
        textAlign = TextAlign.Start,
        modifier = Modifier.padding(horizontal = 16.dp),
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = stringResource(R.string.you_ll_be_able_to_pickup_after_sms),
        style = UiKitTheme.typographyOld.circle1720bold,
        color = UiKitTheme.colorsOld.primaryContentColor,
        textAlign = TextAlign.Start,
        modifier = Modifier.padding(horizontal = 16.dp),
    )
}

@Composable
fun DetailsScreen(
    parentEntry: NavBackStackEntry,
    showSuccess: () -> Unit,
    goBack: () -> Unit,
) {
    val parentViewModel = koinViewModel<PickupViewModel>(viewModelStoreOwner = parentEntry)
    val viewModel = koinViewModel<DetailsViewModel>()

    val surname by parentViewModel.surname.collectAsStateWithLifecycle()
    val surnameError by parentViewModel.surnameError.collectAsStateWithLifecycle()
    val name by parentViewModel.name.collectAsStateWithLifecycle()
    val nameError by parentViewModel.nameError.collectAsStateWithLifecycle()
    val phone by parentViewModel.phone.collectAsStateWithLifecycle()
    val phoneError by parentViewModel.phoneError.collectAsStateWithLifecycle()
    val email by parentViewModel.email.collectAsStateWithLifecycle()
    val emailError by parentViewModel.emailError.collectAsStateWithLifecycle()
    val shop by parentViewModel.selectedShop.collectAsStateWithLifecycle()
    val isReserveButtonEnabled by parentViewModel.isReserveButtonEnabled.collectAsStateWithLifecycle()
    val isLoaderVisible by parentViewModel.isReservationLoaderVisible.collectAsStateWithLifecycle()

    DetailsScreenBehavior(
        sideEffects = viewModel.sideEffects,
        parentSideEffects = parentViewModel.sideEffects,
        showSuccess = showSuccess,
        goBack = goBack,
    )

    DetailsScreenContent(
        surname = surname,
        surnameError = surnameError,
        onSurnameChange = parentViewModel::onSurnameChange,
        onSurnameFocusChange = parentViewModel::onSurnameFocusChange,
        name = name,
        nameError = nameError,
        onNameChange = parentViewModel::onNameChange,
        onNameFocusChange = parentViewModel::onNameFocusChange,
        phone = phone,
        phoneError = phoneError,
        onPhoneChange = parentViewModel::onPhoneChange,
        onPhoneFocusChange = parentViewModel::onPhoneFocusChange,
        email = email,
        emailError = emailError,
        onEmailChange = parentViewModel::onEmailChange,
        onEmailFocusChange = parentViewModel::onEmailFocusChange,
        shop = shop?.shop,
        onReserveClick = parentViewModel::onReserveClick,
        onBackClick = viewModel::onBackClick,
        isReserveButtonEnabled = isReserveButtonEnabled,
        isLoaderVisible = isLoaderVisible,
    )
}

@Composable
fun DetailsScreenBehavior(
    sideEffects: Flow<DetailsViewModel.SideEffect>,
    parentSideEffects: Flow<PickupViewModel.SideEffect>,
    showSuccess: () -> Unit,
    goBack: () -> Unit,
) {
    NavigationBarState(isVisible = false, isAnimated = false)
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                DetailsViewModel.SideEffect.GoBack -> goBack()
            }
        }
    }
    val context by rememberUpdatedState(LocalContext.current)
    LaunchedEffect(parentSideEffects) {
        parentSideEffects.collect { effect ->
            when (effect) {
                PickupViewModel.SideEffect.ShowSuccess -> showSuccess()
                is PickupViewModel.SideEffect.ShowError -> Toast
                    .makeText(context, effect.message.getString(context), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
fun DetailsScreenContentPreview(
    @PreviewParameter(ShopProvider::class)
    shop: Shop,
) {
    ZarinaTheme {
        DetailsScreenContent(
            surname = "Петров",
            surnameError = Text.Resource(R.string.allowed_symbols),
            onSurnameChange = {},
            onSurnameFocusChange = {},
            name = "Иван",
            nameError = null,
            onNameChange = {},
            onNameFocusChange = {},
            phone = "123-456-7890",
            phoneError = null,
            onPhoneChange = {},
            onPhoneFocusChange = {},
            email = "ivan@gmail.com",
            emailError = null,
            onEmailChange = {},
            onEmailFocusChange = {},
            shop = shop,
            onReserveClick = {},
            onBackClick = {},
            isReserveButtonEnabled = true,
            isLoaderVisible = false,
        )
    }
}
