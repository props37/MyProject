package ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.user.LoyaltyCard
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.feature.profile.ui.impl.impl.profile.model.ProfileUserState

@Composable
internal fun AuthorizationOrLoyaltyCard(
    userState: ProfileUserState,
    loyaltyCard: LoyaltyCard?,
    onSignInClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
    onLoyaltyCardInfoClicked: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = ContentPadding,
) {
    AnimatedContent(
        targetState = userState,
        transitionSpec = {
            AnimatedContentDefaultTransitionSpec.using(SizeTransform(clip = false))
        },
        contentAlignment = Alignment.Center,
        label = "Authorization or LoyaltyCard",
        modifier = modifier,
    ) { userState ->
        val paddingModifier = Modifier.padding(contentPadding)

        when (userState) {
            is ProfileUserState.Success -> {
                if (userState.user != null) {
                    LoyaltyCard(
                        loyaltyCard = loyaltyCard,
                        onLoyaltyCardInfoClicked = onLoyaltyCardInfoClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clipToBounds()
                            .then(paddingModifier),
                    )
                } else {
                    AuthorizationBlock(
                        onSignInClicked = onSignInClicked,
                        onSignUpClicked = onSignUpClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(paddingModifier),
                    )
                }
            }

            ProfileUserState.Loading -> Unit
        }
    }
}

private val ContentPadding: PaddingValues
    get() = PaddingValues(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 32.dp)
