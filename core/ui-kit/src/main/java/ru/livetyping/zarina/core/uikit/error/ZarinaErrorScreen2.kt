package ru.livetyping.zarina.core.uikit.error

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.textString
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.theme.ZarinaTheme2

// TODO: [Top] Add video
// TODO: [Top] Rename after full migration

@Composable
public fun ZarinaErrorScreen2(
    state: ZarinaErrorScreenState2,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        val topPadding = contentPadding.calculateTopPadding()
        if (topPadding != 0.dp) {
            Spacer(modifier = Modifier.height(topPadding))
        }

        // TODO: [Top] Add video player
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color.Red),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = textString(state.title).uppercase(),
                style = UiKitTheme2.typography.h2Regular,
                color = UiKitTheme2.colors.mainBlack,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = textString(state.body).uppercase(),
                style = UiKitTheme2.typography.body,
                color = UiKitTheme2.colors.mainBlack,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            if (state.isButtonVisible) {
                Spacer(modifier = Modifier.height(32.dp))

                ZarinaButton(
                    onClick = onButtonClick,
                    contentPadding = PaddingValues(horizontal = 60.dp, vertical = 8.dp),
                    modifier = Modifier.padding(horizontal = 16.dp),
                ) {
                    Text(text = textString(state.buttonText).uppercase())
                }
            }

            val bottomPadding = contentPadding.calculateBottomPadding()
            Spacer(
                modifier = Modifier
                    .heightIn(min = 20.dp + bottomPadding)
                    .fillMaxHeight(0.2f)
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    ZarinaTheme2 {
        ZarinaErrorScreen2(
            state = ZarinaErrorScreenState2.NETWORK,
            onButtonClick = {},
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
        )
    }
}
