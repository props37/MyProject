package ru.livetyping.zarina.core.uikit.feedback

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.R
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.theme.ZarinaTheme2

@Composable
public fun FeedbackWidget(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(UiKitTheme2.colors.backgroundLightBeige)
            .clickable(
                role = Role.Button,
                onClick = onClick,
            )
            .padding(16.dp),
    ) {
        TitleAndBody(modifier = Modifier.weight(1f))

        Spacer(modifier = Modifier.width(16.dp))

        Image(
            imageVector = ImageVector.vectorResource(R.drawable.feedback_widget_image),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.width(72.dp),
        )
    }
}

@Composable
private fun TitleAndBody(
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.uikit_feedback_widget_title).uppercase(),
            style = UiKitTheme2.typography.bodyBold,
            color = UiKitTheme2.colors.mainBlack,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(R.string.uikit_feedback_widget_body).uppercase(),
            style = UiKitTheme2.typography.body,
            color = UiKitTheme2.colors.mainBlack,
        )
    }
}

@Composable
@Preview
private fun Preview() {
    ZarinaTheme2 {
        FeedbackWidget(onClick = {})
    }
}
