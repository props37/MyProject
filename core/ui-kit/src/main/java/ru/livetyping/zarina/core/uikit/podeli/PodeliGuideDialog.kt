package ru.livetyping.zarina.core.uikit.podeli

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import ru.livetyping.zarina.core.uicommon.openUrlInCustomTabs
import ru.livetyping.zarina.core.uicompose.text.rememberAnnotatedStringWithLinks
import ru.livetyping.zarina.core.uikit.R
import ru.livetyping.zarina.core.uikit.pager.ZarinaHorizontalPagerIndicator
import ru.livetyping.zarina.core.uikit.pager.ZarinaHorizontalPagerIndicatorDefaults
import ru.livetyping.zarina.core.uikit.theme.ZarinaTheme2
import ru.livetyping.zarina.core.resource.R as RCommon

@SuppressLint("ComposeModifierMissing")
@Composable
public fun PodeliGuideDialog(
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        CompositionLocalProvider(LocalContentColor provides PodeliTheme.ContentColor) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(Color.White, BackgroundShape)
                    .verticalScroll(rememberScrollState()),
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                TopBar(onCloseClicked = onDismissRequest)
                
                Spacer(modifier = Modifier.height(12.dp))

                MainBlock()

                Spacer(modifier = Modifier.height(56.dp))

                StepByStepBlock()

                Spacer(modifier = Modifier.height(24.dp))

                MainFootnote(modifier = Modifier.padding(horizontal = HorizontalPadding))

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun TopBar(
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        AsyncImage(
            model = R.drawable.podeli_logo,
            contentDescription = stringResource(R.string.uikit_podeli_logo_description),
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .height(32.dp)
                .padding(start = HorizontalPadding),
        )

        Spacer(modifier = Modifier.weight(1f))

        CloseButton(
            onClick = onCloseClicked,
            modifier = Modifier.padding(end = 4.dp),
        )
    }
}

@Composable
private fun CloseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_podeli_cross_24),
            contentDescription = stringResource(RCommon.string.res_close),
            modifier = Modifier.size(16.dp),
        )
    }
}

@Composable
private fun MainBlock(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.uikit_podeli_guide_main_block_title),
            fontFamily = PodeliTheme.ManropeFontFamily,
            fontSize = 36.sp,
            fontWeight = FontWeight.W700,
            lineHeight = 41.sp,
            modifier = Modifier.padding(horizontal = HorizontalPadding),
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.uikit_podeli_guide_main_block_description),
            fontFamily = PodeliTheme.ManropeFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = HorizontalPadding),
        )

        Spacer(modifier = Modifier.height(24.dp))

        AsyncImage(
            model = R.drawable.podeli_main_block_image,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HorizontalPadding),
        )

        Spacer(modifier = Modifier.height(24.dp))

        MainBlockFootnote(modifier = Modifier.padding(horizontal = HorizontalPadding))
    }
}

@Composable
private fun MainBlockFootnote(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val textStyle = TextStyle(
        fontFamily = PodeliTheme.ManropeFontFamily,
        fontSize = 13.sp,
        fontWeight = FontWeight.W500,
        lineHeight = 18.sp,
    )
    val linkText = stringResource(R.string.uikit_podeli_guide_main_block_footnote_link_text)
    val linkUrl = stringResource(R.string.uikit_podeli_guide_main_block_footnote_link_url)
    val substringToUrl = remember(linkText, linkUrl) { mapOf(linkText to linkUrl) }
    val linkStyle = textStyle.copy(
        color = PodeliTheme.AccentColor,
        textDecoration = TextDecoration.Underline,
    )
    val text = rememberAnnotatedStringWithLinks(
        baseString = stringResource(R.string.uikit_podeli_guide_main_block_footnote),
        substringToUrl = substringToUrl,
        linkStyle = linkStyle.toSpanStyle(),
        onUrlClicked = context::openUrlInCustomTabs,
    )

    Text(
        text = text,
        style = textStyle,
        modifier = modifier
            .background(
                color = PodeliTheme.FootnoteBackgroundColor,
                shape = RoundedCornerShape(8.dp),
            )
            .padding(horizontal = 24.dp, vertical = 12.dp),
    )
}

@Composable
private fun StepByStepBlock(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.uikit_podeli_step_by_step_block_title),
            fontFamily = PodeliTheme.ManropeFontFamily,
            fontSize = 28.sp,
            fontWeight = FontWeight.W700,
            lineHeight = 32.sp,
            modifier = Modifier.padding(horizontal = HorizontalPadding),
        )

        Spacer(modifier = Modifier.height(24.dp))

        StepByStepPager()
    }
}

@Composable
private fun StepByStepPager(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        val pagerState = rememberPagerState { StepByStepPageCount }

        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Top,
        ) { page ->
            when (page) {
                0 -> {
                    StepByStepPage(
                        page = page,
                        title = stringResource(R.string.uikit_podeli_step_by_step_block_step_1_title),
                        imageResId = R.drawable.podeli_step_by_step_1,
                    )
                }

                1 -> {
                    StepByStepPage(
                        page = page,
                        title = stringResource(R.string.uikit_podeli_step_by_step_block_step_2_title),
                        imageResId = R.drawable.podeli_step_by_step_2_3,
                    )
                }

                2 -> {
                    StepByStepPage(
                        page = page,
                        title = stringResource(R.string.uikit_podeli_step_by_step_block_step_3_title),
                        description = {
                            Step3Description()
                        },
                        imageResId = R.drawable.podeli_step_by_step_2_3,
                    )
                }
            }
        }

        ZarinaHorizontalPagerIndicator(
            pagerState = pagerState,
            itemCount = pagerState.pageCount,
            style = ZarinaHorizontalPagerIndicatorDefaults.dots(
                dotSize = 12.dp,
                spacedBy = 8.dp,
                activeColor = PodeliTheme.AccentColor,
                inactiveColor = PodeliTheme.PagerIndicatorInactiveColor,
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
    }
}

@Composable
private fun StepByStepPage(
    page: Int,
    title: String,
    @DrawableRes
    imageResId: Int,
    modifier: Modifier = Modifier,
    description: (@Composable () -> Unit)? = null,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.uikit_podeli_step_by_step_block_step_counter, page + 1),
            color = PodeliTheme.CounterColor,
            fontFamily = PodeliTheme.ManropeFontFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.W700,
            lineHeight = 16.sp,
            modifier = Modifier.padding(horizontal = HorizontalPadding),
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = title,
            fontFamily = PodeliTheme.ManropeFontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.W700,
            lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = HorizontalPadding),
        )

        if (description != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.padding(horizontal = HorizontalPadding)) {
                description()
            }
        } else {
            Spacer(modifier = Modifier.height(32.dp))
        }

        AsyncImage(
            model = imageResId,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HorizontalPadding),
        )
    }
}

@Composable
private fun Step3Description(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val textStyle = TextStyle(
        color = PodeliTheme.HelperTextColor,
        fontFamily = PodeliTheme.ManropeFontFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight.W500,
        lineHeight = 15.sp,
    )
    val linkText = stringResource(R.string.uikit_podeli_step_by_step_block_step_3_description_link_text)
    val linkUrl = stringResource(R.string.uikit_podeli_step_by_step_block_step_3_description_link_url)
    val substringToUrl = remember(linkText, linkUrl) { mapOf(linkText to linkUrl) }
    val linkStyle = textStyle.copy(
        color = PodeliTheme.AccentColor,
        textDecoration = TextDecoration.Underline,
    )
    val text = rememberAnnotatedStringWithLinks(
        baseString = stringResource(R.string.uikit_podeli_step_by_step_block_step_3_description),
        substringToUrl = substringToUrl,
        linkStyle = linkStyle.toSpanStyle(),
        onUrlClicked = context::openUrlInCustomTabs,
    )

    Text(
        text = text,
        style = textStyle,
        modifier = modifier,
    )
}

@Composable
private fun MainFootnote(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(modifier = modifier) {
        Divider(
            color = PodeliTheme.DividerColor,
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(10.dp))

        val textStyle = TextStyle(
            color = LocalContentColor.current.copy(alpha = 0.3f),
            fontFamily = PodeliTheme.ManropeFontFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.W500,
            lineHeight = 16.sp,
        )
        val linkText = stringResource(R.string.uikit_podeli_main_footnote_link_text)
        val linkUrl = stringResource(R.string.uikit_podeli_main_footnote_link_url)
        val substringToUrl = remember(linkText, linkUrl) { mapOf(linkText to linkUrl) }
        val linkStyle = textStyle.copy(textDecoration = TextDecoration.Underline)
        val text = rememberAnnotatedStringWithLinks(
            baseString = stringResource(R.string.uikit_podeli_main_footnote),
            substringToUrl = substringToUrl,
            linkStyle = linkStyle.toSpanStyle(),
            onUrlClicked = context::openUrlInCustomTabs,
        )

        Text(
            text = text,
            style = textStyle,
        )
    }
}

@Preview
@Composable
private fun Preview() {
    ZarinaTheme2 {
        PodeliGuideDialog(onDismissRequest = {})
    }
}

private val BackgroundShape: Shape get() = RoundedCornerShape(16.dp)

private val HorizontalPadding: Dp get() = 20.dp

private const val StepByStepPageCount = 3
