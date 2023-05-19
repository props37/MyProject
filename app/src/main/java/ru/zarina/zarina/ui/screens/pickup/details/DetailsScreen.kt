package ru.zarina.zarina.ui.screens.pickup.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.Shop
import ru.zarina.zarina.ui.common.base.Text
import ru.zarina.zarina.ui.common.base.textString
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.buttons.ZarinaTextButton
import ru.zarina.zarina.ui.common.components.form.Input
import ru.zarina.zarina.ui.common.components.form.SectionHeader
import ru.zarina.zarina.ui.common.components.toolbar.BackButton
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.providers.domain.ShopProvider
import ru.zarina.zarina.ui.screens.pickup.PickupViewModel
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme
import ru.zarina.zarina.utils.compose.autofill
import ru.zarina.zarina.utils.compose.navigationOrIme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreenContent(
    surname: String,
    surnameError: Text?,
    onSurnameChange: (String) -> Unit,
    name: String,
    nameError: Text?,
    onNameChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    email: String,
    emailError: Text?,
    onEmailChange: (String) -> Unit,
    shop: Shop?,
    onPlaceOrderClick: () -> Unit,
    onBackClick: () -> Unit,
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
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            RecipientInformation(
                surname = surname,
                surnameError = surnameError,
                onSurnameChange = onSurnameChange,
                name = name,
                nameError = nameError,
                onNameChange = onNameChange,
                phone = phone,
                onPhoneChange = onPhoneChange,
                email = email,
                emailError = emailError,
                onEmailChange = onEmailChange,
            )
            OrderInformation(
                shop = shop
            )
            Spacer(modifier = Modifier.height(8.dp))
            ZarinaTextButton(
                text = stringResource(id = R.string.place_order),
                onClick = onPlaceOrderClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
            Spacer(
                modifier = Modifier
                    .padding(WindowInsets.navigationOrIme.asPaddingValues())
                    .height(24.dp)
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
    name: String,
    nameError: Text?,
    onNameChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    email: String,
    emailError: Text?,
    onEmailChange: (String) -> Unit,
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
            .padding(top = 4.dp, bottom = 12.dp)
            .fillMaxWidth()
            .autofill(listOf(AutofillType.PersonLastName), onSurnameChange),
    )
    Input(
        value = name,
        onValueChange = onNameChange,
        hint = stringResource(id = R.string.name),
        isError = nameError != null,
        error = nameError?.let { textString(it) },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Next,
        ),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 4.dp, bottom = 12.dp)
            .fillMaxWidth()
            .autofill(listOf(AutofillType.PersonFirstName), onNameChange),
    )
    Input(
        value = phone,
        onValueChange = onPhoneChange,
        hint = stringResource(id = R.string.phone),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next,
        ),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 4.dp, bottom = 12.dp)
            .fillMaxWidth()
            .autofill(listOf(AutofillType.PhoneNumber), onPhoneChange),
    )
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
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 4.dp, bottom = 12.dp)
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
            style = UiKitTheme.typography.circle1518bold,
            color = UiKitTheme.colors.primaryContentColor,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${shop.name}, ${shop.address}",
            style = UiKitTheme.typography.circle1718,
            color = UiKitTheme.colors.primaryContentColor,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.working_schedule_template, shop.schedule),
            style = UiKitTheme.typography.circle1718,
            color = UiKitTheme.colors.primaryContentColor,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
    Text(
        text = stringResource(R.string.reservation_period),
        style = UiKitTheme.typography.circle1518bold,
        color = UiKitTheme.colors.primaryContentColor,
        textAlign = TextAlign.Start,
        modifier = Modifier.padding(horizontal = 16.dp),
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = stringResource(R.string.two_days),
        style = UiKitTheme.typography.circle1718,
        color = UiKitTheme.colors.primaryContentColor,
        textAlign = TextAlign.Start,
        modifier = Modifier.padding(horizontal = 16.dp),
    )
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = stringResource(R.string.payment_method),
        style = UiKitTheme.typography.circle1518bold,
        color = UiKitTheme.colors.primaryContentColor,
        textAlign = TextAlign.Start,
        modifier = Modifier.padding(horizontal = 16.dp),
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = stringResource(R.string.in_cash_or_by_card_upon_receiving),
        style = UiKitTheme.typography.circle1718,
        color = UiKitTheme.colors.primaryContentColor,
        textAlign = TextAlign.Start,
        modifier = Modifier.padding(horizontal = 16.dp),
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = stringResource(R.string.you_ll_be_able_to_pickup_after_sms),
        style = UiKitTheme.typography.circle1720bold,
        color = UiKitTheme.colors.primaryContentColor,
        textAlign = TextAlign.Start,
        modifier = Modifier.padding(horizontal = 16.dp),
    )
}

@Composable
fun DetailsScreen(
    parentEntry: NavBackStackEntry,
    goBack: () -> Unit,
) {
    val parentViewModel = hiltViewModel<PickupViewModel>(parentEntry)
    val viewModel = hiltViewModel<DetailsViewModel>()

    val surname by parentViewModel.surname.collectAsStateWithLifecycle()
    val surnameError by parentViewModel.surnameError.collectAsStateWithLifecycle()
    val name by parentViewModel.name.collectAsStateWithLifecycle()
    val nameError by parentViewModel.nameError.collectAsStateWithLifecycle()
    val phone by parentViewModel.phone.collectAsStateWithLifecycle()
    val email by parentViewModel.email.collectAsStateWithLifecycle()
    val emailError by parentViewModel.emailError.collectAsStateWithLifecycle()
    val shop by parentViewModel.selectedShop.collectAsStateWithLifecycle()

    DetailsScreenBehavior(
        sideEffects = viewModel.sideEffects,
        goBack = goBack,
    )

    DetailsScreenContent(
        surname = surname,
        surnameError = surnameError,
        onSurnameChange = parentViewModel::onSurnameChange,
        name = name,
        nameError = nameError,
        onNameChange = parentViewModel::onNameChange,
        phone = phone,
        onPhoneChange = parentViewModel::onPhoneChange,
        email = email,
        emailError = emailError,
        onEmailChange = parentViewModel::onEmailChange,
        shop = shop?.shop,
        onPlaceOrderClick = parentViewModel::onPlaceOrderClick,
        onBackClick = viewModel::onBackClick,
    )
}

@Composable
fun DetailsScreenBehavior(
    sideEffects: Flow<DetailsViewModel.SideEffect>,
    goBack: () -> Unit,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                DetailsViewModel.SideEffect.GoBack -> goBack()
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
            name = "Иван",
            nameError = null,
            onNameChange = {},
            phone = "123-456-7890",
            onPhoneChange = {},
            email = "ivan@gmail.com",
            emailError = null,
            onEmailChange = {},
            shop = shop,
            onPlaceOrderClick = {},
            onBackClick = {},
        )
    }
}
