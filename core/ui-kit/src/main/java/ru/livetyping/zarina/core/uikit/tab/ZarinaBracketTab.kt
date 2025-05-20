package ru.livetyping.zarina.core.uikit.tab

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.text.ZARINA_BRACKET_END
import ru.livetyping.zarina.core.uikit.text.ZARINA_BRACKET_START
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.theme.ZarinaTheme2

@Composable
public fun ZarinaBracketTab(
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    addBrackets: Boolean = isSelected,
    selectedTextStyle: TextStyle = ZarinaBracketTabDefaults.SelectedTextStyle,
    unselectedTextStyle: TextStyle = ZarinaBracketTabDefaults.UnselectedTextStyle,
    selectedColor: Color = ZarinaBracketTabDefaults.SelectedColor,
    unselectedColor: Color = ZarinaBracketTabDefaults.UnselectedColor,
    bracketPadding: Dp = ZarinaBracketTabDefaults.BracketPadding,
    contentPadding: PaddingValues = ZarinaBracketTabDefaults.ContentPadding,
    content: @Composable () -> Unit,
) {
    val textStyle = if (isSelected) selectedTextStyle else unselectedTextStyle
    val color by animateColorAsState(
        targetValue = if (isSelected) selectedColor else unselectedColor,
        label = "ZarinaBracketTab color",
    )

    CompositionLocalProvider(
        LocalTextStyle provides textStyle,
        LocalContentColor provides color,
    ) {
        val clickableModifier = if (onClick != null) {
            Modifier.clickable(role = Role.Tab, onClick = onClick)
        } else {
            Modifier
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .clip(RoundedCornerShape(1.dp))
                .then(clickableModifier)
                .minimumInteractiveComponentSize()
                .padding(contentPadding),
        ) {
            Bracket(
                bracket = Bracket.Start,
                isVisible = addBrackets,
                style = textStyle,
                color = color,
                padding = bracketPadding,
            )

            content()

            Bracket(
                bracket = Bracket.End,
                isVisible = addBrackets,
                style = textStyle,
                color = color,
                padding = bracketPadding,
            )
        }
    }
}

@Composable
public fun ZarinaBracketTab(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    addBrackets: Boolean = isSelected,
    selectedTextStyle: TextStyle = ZarinaBracketTabDefaults.SelectedTextStyle,
    unselectedTextStyle: TextStyle = ZarinaBracketTabDefaults.UnselectedTextStyle,
    selectedColor: Color = ZarinaBracketTabDefaults.SelectedColor,
    unselectedColor: Color = ZarinaBracketTabDefaults.UnselectedColor,
    bracketPadding: Dp = ZarinaBracketTabDefaults.BracketPadding,
    contentPadding: PaddingValues = ZarinaBracketTabDefaults.ContentPadding,
) {
    ZarinaBracketTab(
        isSelected = isSelected,
        onClick = onClick,
        addBrackets = addBrackets,
        selectedTextStyle = selectedTextStyle,
        unselectedTextStyle = unselectedTextStyle,
        selectedColor = selectedColor,
        unselectedColor = unselectedColor,
        bracketPadding = bracketPadding,
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        Text(
            text = text,
            style = LocalTextStyle.current,
            color = LocalContentColor.current,
        )
    }
}

@Composable
private fun RowScope.Bracket(
    bracket: Bracket,
    isVisible: Boolean,
    style: TextStyle,
    color: Color,
    padding: Dp,
    modifier: Modifier = Modifier,
) {
    val char = when (bracket) {
        Bracket.Start -> ZARINA_BRACKET_START
        Bracket.End -> ZARINA_BRACKET_END
    }
    val expandFrom = when (bracket) {
        Bracket.Start -> Alignment.Start
        Bracket.End -> Alignment.End
    }
    val shrinkTowards = when (bracket) {
        Bracket.Start -> Alignment.Start
        Bracket.End -> Alignment.End
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = expandHorizontally(
            expandFrom = expandFrom,
            animationSpec = ZarinaBracketTabDefaults.BracketPlacementAnimationSpec,
        ) + fadeIn(
            animationSpec = ZarinaBracketTabDefaults.BracketFadeAnimationSpec,
        ),
        exit = shrinkHorizontally(
            shrinkTowards = shrinkTowards,
            animationSpec = ZarinaBracketTabDefaults.BracketPlacementAnimationSpec,
        ) + fadeOut(
            animationSpec = ZarinaBracketTabDefaults.BracketFadeAnimationSpec,
        ),
        modifier = modifier,
    ) {
        val paddingModifier = when (bracket) {
            Bracket.Start -> Modifier.padding(end = padding)
            Bracket.End -> Modifier.padding(start = padding)
        }

        Text(
            text = char.toString(),
            style = style,
            color = color,
            modifier = paddingModifier,
        )
    }
}

@Composable
@Preview
private fun Preview() {
    var isSelected by remember { mutableStateOf(true) }

    ZarinaTheme2 {
        ZarinaBracketTab(
            text = "ЖЕНЩИНАМ",
            isSelected = isSelected,
            onClick = { isSelected = !isSelected },
            modifier = Modifier.background(Color.White),
        )
    }
}

public object ZarinaBracketTabDefaults {
    internal val SelectedTextStyle: TextStyle
        @Composable
        get() = UiKitTheme2.typography.body

    internal val UnselectedTextStyle: TextStyle
        @Composable
        get() = SelectedTextStyle

    public val SelectedColor: Color
        @Composable
        get() = UiKitTheme2.colors.mainBlack

    public val UnselectedColor: Color
        @Composable
        get() = SelectedColor.copy(alpha = 0.5f)

    internal val ContentPadding: PaddingValues
        get() = PaddingValues(horizontal = 8.dp, vertical = 4.dp)

    internal val BracketPadding: Dp get() = 7.dp

    internal val BracketPlacementAnimationSpec: FiniteAnimationSpec<IntSize>
        get() = spring(
            stiffness = Spring.StiffnessMediumLow,
            visibilityThreshold = IntSize.VisibilityThreshold
        )

    internal val BracketFadeAnimationSpec: FiniteAnimationSpec<Float>
        get() = spring(stiffness = Spring.StiffnessMedium)
}

private enum class Bracket { Start, End }
