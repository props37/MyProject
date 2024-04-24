package ru.livetyping.zarina.ui.screen.productsubscription

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.common.MediaType
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductOffer
import ru.livetyping.zarina.ui.common.component.ProductOrderCard
import ru.livetyping.zarina.ui.common.component.button.ZarinaButton
import ru.livetyping.zarina.ui.common.component.textfield.ZarinaTextField
import ru.livetyping.zarina.ui.common.component.textfield.ZarinaTextFieldDefaults
import ru.livetyping.zarina.ui.common.component.textfield.ZarinaTextFieldSize
import ru.livetyping.zarina.ui.common.tooling.FakeDataGenerator
import ru.livetyping.zarina.ui.common.tooling.preview.DensityPreviews
import ru.livetyping.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.livetyping.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.ui.screen.productsubscription.ProductSubscriptionScreenComponents.Policies
import ru.livetyping.zarina.ui.screen.productsubscription.ProductSubscriptionScreenComponents.TopBar
import ru.livetyping.zarina.ui.screen.productsubscription.ProductSubscriptionViewModel.SideEffect
import ru.livetyping.zarina.ui.theme.UiKitTheme

@Composable
fun ProductSubscriptionScreen(
    navigate: (ProductSubscriptionScreenAction) -> Unit,
    viewModel: ProductSubscriptionViewModel = hiltViewModel(),
) {
    val product by viewModel.product.collectAsStateWithLifecycle()
    val productOffer by viewModel.productOffer.collectAsStateWithLifecycle()
    val firstName by viewModel.firstName.collectAsStateWithLifecycle(
        context = Dispatchers.Main.immediate, // TODO: [Low] remove after migration to BasicTextField2
    )
    val isFirstNameInvalid by viewModel.isFirstNameInvalid.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle(
        context = Dispatchers.Main.immediate, // TODO: [Low] remove after migration to BasicTextField2
    )
    val isEmailInvalid by viewModel.isEmailInvalid.collectAsStateWithLifecycle()
    val arePoliciesAccepted by viewModel.arePoliciesAccepted.collectAsStateWithLifecycle()
    val isPoliciesErrorVisible by viewModel.isPoliciesErrorVisible.collectAsStateWithLifecycle()
    val isSubscribeButtonLoading by viewModel.isSubscribeButtonLoading.collectAsStateWithLifecycle()

    ScreenContent(
        onBackClicked = viewModel::onBackClicked,
        product = product,
        productOffer = productOffer,
        firstName = firstName,
        onFirstNameChanged = viewModel::onFirstNameChanged,
        isFirstNameInvalid = isFirstNameInvalid,
        email = email,
        onEmailChanged = viewModel::onEmailChanged,
        isEmailInvalid = isEmailInvalid,
        arePoliciesAccepted = arePoliciesAccepted,
        isPoliciesErrorVisible = isPoliciesErrorVisible,
        isSubscribeButtonLoading = isSubscribeButtonLoading,
        onUrlClicked = viewModel::onUrlClicked,
        onPoliciesAcceptedChanged = viewModel::onPoliciesAcceptedChanged,
        onSubscribeClicked = viewModel::onSubscribeClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    onBackClicked: () -> Unit,
    product: Product,
    productOffer: ProductOffer,
    firstName: String,
    onFirstNameChanged: (String) -> Unit,
    isFirstNameInvalid: Boolean,
    email: String,
    onEmailChanged: (String) -> Unit,
    isEmailInvalid: Boolean,
    arePoliciesAccepted: Boolean,
    isPoliciesErrorVisible: Boolean,
    isSubscribeButtonLoading: Boolean,
    onUrlClicked: (Url) -> Unit,
    onPoliciesAcceptedChanged: (Boolean) -> Unit,
    onSubscribeClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (ProductSubscriptionScreenAction) -> Unit,
) {
    ProductSubscriptionScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            ),
    ) {
        TopBar(
            onBackClicked = onBackClicked,
            modifier = Modifier.fillMaxWidth(),
        )

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.height(12.dp))
            ProductOrderCard(
                name = product.name,
                imageUrl = remember(product.media) {
                    product.media.firstOrNull { it.type == MediaType.IMAGE }?.url ?: Url.EMPTY
                },
                size = productOffer.size,
                sizeRu = productOffer.sizeRu,
                height = productOffer.height,
                color = remember(product) { product.colors.find { it.productId == product.id } },
                price = product.price,
                showOriginalPrice = false,
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = stringResource(R.string.product_subscription_description),
                style = UiKitTheme.typography.secondary.light,
                color = UiKitTheme.colors.text.general.regular.default,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(20.dp))

            ZarinaTextField(
                value = firstName,
                onValueChanged = onFirstNameChanged,
                isError = isFirstNameInvalid,
                size = ZarinaTextFieldSize.Small,
                label = { Text(text = stringResource(R.string.first_name)) },
                placeholder = {
                    Text(text = stringResource(R.string.first_name_text_field_placeholder))
                },
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = firstName.isNotEmpty(),
                        onClick = { onFirstNameChanged("") },
                        iconSize = 16.dp,
                        indication = rememberRipple(bounded = false, radius = 6.dp),
                    )
                },
                singleLine = true,
                keyboardOptions = remember {
                    KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next,
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(20.dp))

            ZarinaTextField(
                value = email,
                onValueChanged = onEmailChanged,
                isError = isEmailInvalid,
                size = ZarinaTextFieldSize.Small,
                label = { Text(text = stringResource(R.string.email)) },
                placeholder = { Text(text = stringResource(R.string.email_text_field_placeholder)) },
                innerTrailingContent = {
                    ZarinaTextFieldDefaults.ClearButton(
                        isVisible = email.isNotEmpty(),
                        onClick = { onEmailChanged("") },
                        iconSize = 16.dp,
                        indication = rememberRipple(bounded = false, radius = 6.dp),
                    )
                },
                singleLine = true,
                keyboardOptions = remember {
                    KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done,
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(28.dp))

            Policies(
                areAccepted = arePoliciesAccepted,
                onAcceptedChanged = onPoliciesAcceptedChanged,
                isError = isPoliciesErrorVisible,
                onUrlClicked = onUrlClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(20.dp))

            ZarinaButton(
                onClick = onSubscribeClicked,
                isLoading = isSubscribeButtonLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Text(text = stringResource(R.string.subscribe).uppercase())
            }

            Spacer(modifier = Modifier.height(20.dp))
            val navigationBarsOrImeBottomPadding =
                WindowInsets.ime.asPaddingValues().calculateBottomPadding()
            Spacer(modifier = Modifier.height(navigationBarsOrImeBottomPadding))
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun Preview() {
    ZarinaPreview {
        ScreenContent(
            onBackClicked = {},
            product = remember { FakeDataGenerator.getProduct() },
            productOffer = remember { FakeDataGenerator.getProductOffer() },
            firstName = "",
            onFirstNameChanged = {},
            isFirstNameInvalid = false,
            email = "",
            onEmailChanged = {},
            isEmailInvalid = false,
            arePoliciesAccepted = false,
            isPoliciesErrorVisible = false,
            isSubscribeButtonLoading = false,
            onUrlClicked = {},
            onPoliciesAcceptedChanged = {},
            onSubscribeClicked = {},
            sideEffects = remember { emptyFlow() },
            navigate = {},
        )
    }
}
