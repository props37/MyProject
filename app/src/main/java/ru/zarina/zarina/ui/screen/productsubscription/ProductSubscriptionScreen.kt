package ru.zarina.zarina.ui.screen.productsubscription

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
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
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.rework.common.Url
import ru.zarina.zarina.domain.rework.product.Product
import ru.zarina.zarina.domain.rework.product.ProductOffer
import ru.zarina.zarina.ui.common.component.textfield.ZarinaTextField
import ru.zarina.zarina.ui.common.component.textfield.ZarinaTextFieldDefaults
import ru.zarina.zarina.ui.common.component.textfield.ZarinaTextFieldSize
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.productsubscription.ProductSubscriptionScreenComponents.Policies
import ru.zarina.zarina.ui.screen.productsubscription.ProductSubscriptionScreenComponents.ProductCard
import ru.zarina.zarina.ui.screen.productsubscription.ProductSubscriptionScreenComponents.TopBar
import ru.zarina.zarina.ui.screen.productsubscription.ProductSubscriptionViewModel.SideEffect
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.AnimatedContentDefaultEnterTransition
import ru.zarina.zarina.util.compose.AnimatedContentDefaultExitTransition

@Composable
fun ProductSubscriptionScreen(
    navigateBackward: (ProductSubscriptionScreenResult) -> Unit,
    viewModel: ProductSubscriptionViewModel = hiltViewModel(),
) {
    val product by viewModel.product.collectAsStateWithLifecycle()
    val productOffer by viewModel.productOffer.collectAsStateWithLifecycle()
    val name by viewModel.name.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val arePoliciesAccepted by viewModel.arePoliciesAccepted.collectAsStateWithLifecycle()

    ScreenContent(
        product = product,
        productOffer = productOffer,
        name = name,
        onNameChanged = viewModel::onNameChanged,
        email = email,
        onEmailChanged = viewModel::onEmailChanged,
        arePoliciesAccepted = arePoliciesAccepted,
        onPoliciesAcceptedChanged = viewModel::onPoliciesAcceptedChanged,
        onBackClicked = viewModel::onBackClicked,
        onUrlClicked = viewModel::onUrlClicked,
        sideEffects = viewModel.sideEffects,
        navigateBackward = navigateBackward,
    )
}

@Composable
private fun ScreenContent(
    product: Product,
    productOffer: ProductOffer,
    name: String,
    onNameChanged: (String) -> Unit,
    email: String,
    onEmailChanged: (String) -> Unit,
    arePoliciesAccepted: Boolean,
    onPoliciesAcceptedChanged: (Boolean) -> Unit,
    onBackClicked: () -> Unit,
    onUrlClicked: (Url) -> Unit,
    sideEffects: Flow<SideEffect>,
    navigateBackward: (ProductSubscriptionScreenResult) -> Unit,
) {
    ProductSubscriptionScreenBehavior(
        sideEffects = sideEffects,
        navigateBackward = navigateBackward,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colorsReworked.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.systemBars
                    .union(WindowInsets.displayCutout)
                    .union(WindowInsets.ime),
            ),
    ) {
        TopBar(
            onBackClicked = onBackClicked,
            modifier = Modifier.fillMaxWidth(),
        )

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.height(12.dp))
            ProductCard(
                product = product,
                productOffer = productOffer,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = stringResource(R.string.product_subscription_description),
                style = UiKitTheme.typographyReworked.secondary.light,
                color = UiKitTheme.colorsReworked.text.general.regular.default,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(20.dp))

            ZarinaTextField(
                value = name,
                onValueChanged = onNameChanged,
                size = ZarinaTextFieldSize.Small,
                label = { Text(text = stringResource(R.string.how_should_i_contact_you)) },
                placeholder = { Text(text = stringResource(R.string.first_name)) },
                innerTrailingContent = {
                    AnimatedVisibility(
                        visible = name.isNotBlank(),
                        enter = remember { AnimatedContentDefaultEnterTransition },
                        exit = remember { AnimatedContentDefaultExitTransition },
                    ) {
                        ZarinaTextFieldDefaults.ClearButton(
                            onClick = { onNameChanged("") },
                            iconSize = 16.dp,
                        )
                    }
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
                size = ZarinaTextFieldSize.Small,
                label = { Text(text = stringResource(R.string.email)) },
                placeholder = { Text(text = stringResource(R.string.email_address)) },
                innerTrailingContent = {
                    AnimatedVisibility(
                        visible = email.isNotBlank(),
                        enter = remember { AnimatedContentDefaultEnterTransition },
                        exit = remember { AnimatedContentDefaultExitTransition },
                    ) {
                        ZarinaTextFieldDefaults.ClearButton(
                            onClick = { onEmailChanged("") },
                            iconSize = 16.dp,
                        )
                    }
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
                onUrlClicked = onUrlClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}
