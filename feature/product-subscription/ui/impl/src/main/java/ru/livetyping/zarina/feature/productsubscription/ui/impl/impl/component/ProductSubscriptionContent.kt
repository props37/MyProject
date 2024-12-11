package ru.livetyping.zarina.feature.productsubscription.ui.impl.impl.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.media.MediaType
import ru.livetyping.zarina.core.uicompose.tryRequestFocus
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.product.ProductOrderCard
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.text.ZarinaTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaTextFieldDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.productsubscription.ui.impl.R
import ru.livetyping.zarina.feature.productsubscription.ui.impl.impl.model.ProductSubscriptionEvent
import ru.livetyping.zarina.feature.productsubscription.ui.impl.impl.model.ProductSubscriptionState
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun ProductSubscriptionContent(
    state: ProductSubscriptionState,
    onEvent: (ProductSubscriptionEvent) -> Unit,
    modifier: Modifier = Modifier,
    windowInsetsProvider: @Composable () -> WindowInsets = { WindowInsets.safeDrawing },
) {
    val product = state.product
    val productOffer = state.productOffer

    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(12.dp))

        ProductOrderCard(
            name = product.name,
            imageUrl = remember(product.media) {
                product.media.firstOrNull { it.type == MediaType.IMAGE }?.originalUrl?.value.orEmpty()
            },
            size = productOffer.size,
            sizeRu = productOffer.sizeRu,
            height = productOffer.height,
            color = remember(product) {
                product.colors.find { it.productId == product.id }
            },
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

        val nameTextFieldState = state.nameTextFieldState
        val nameFocusRequester = remember { FocusRequester() }
        ZarinaTextField(
            state = nameTextFieldState,
            isError = state.isNameInvalid,
            label = {
                val label = if (nameTextFieldState.text.isNotEmpty()) {
                    stringResource(RCommon.string.res_first_name)
                } else ""

                Text(text = label)
            },
            placeholder = {
                Text(text = stringResource(RCommon.string.res_first_name))
            },
            innerTrailingContent = {
                ZarinaTextFieldDefaults.ClearButton(
                    isVisible = nameTextFieldState.text.isNotEmpty(),
                    onClick = {
                        nameTextFieldState.clearText()
                        nameFocusRequester.tryRequestFocus()
                    },
                )
            },
            keyboardOptions = remember {
                KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next,
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .focusRequester(nameFocusRequester),
        )

        Spacer(modifier = Modifier.height(20.dp))

        val emailTextFieldState = state.emailTextFieldState
        val emailFocusRequester = remember { FocusRequester() }
        ZarinaTextField(
            state = emailTextFieldState,
            isError = state.isEmailInvalid,
            label = {
                val label = if (emailTextFieldState.text.isNotEmpty()) {
                    stringResource(RCommon.string.res_email)
                } else ""

                Text(text = label)
            },
            placeholder = {
                Text(text = stringResource(RCommon.string.res_email))
            },
            innerTrailingContent = {
                ZarinaTextFieldDefaults.ClearButton(
                    isVisible = emailTextFieldState.text.isNotEmpty(),
                    onClick = {
                        emailTextFieldState.clearText()
                        emailFocusRequester.tryRequestFocus()
                    },
                )
            },
            keyboardOptions = remember {
                KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done,
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .focusRequester(emailFocusRequester),
        )

        Spacer(modifier = Modifier.height(28.dp))

        // TODO: [Top] Add policies

        Spacer(modifier = Modifier.height(20.dp))

        ZarinaButton(
            onClick = { onEvent(ProductSubscriptionEvent.SubscribeClicked) },
            isLoading = state.isSubscribeButtonLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            Text(text = stringResource(RCommon.string.res_subscribe).uppercase())
        }

        Spacer(modifier = Modifier.height(ZarinaScrollableDefaults.ScrollableBottomPadding))
        Spacer(modifier = Modifier.windowInsetsBottomHeight(windowInsetsProvider()))
    }
}
