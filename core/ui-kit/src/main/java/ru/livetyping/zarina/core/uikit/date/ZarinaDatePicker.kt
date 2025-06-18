package ru.livetyping.zarina.core.uikit.date

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.platform.getLocale
import ru.livetyping.zarina.core.uikit.R
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonColors
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import java.util.Locale
import ru.livetyping.zarina.core.resource.R as RCommon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun ZarinaDatePicker(
    modifier: Modifier = Modifier,
    state: DatePickerState = rememberDatePickerState(),
    dateFormatter: DatePickerFormatter = remember { DatePickerDefaults.dateFormatter() },
    title: (@Composable () -> Unit)? = {
        ZarinaDatePickerDefaults.Title(
            modifier = Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp),
        )
    },
    headline: (@Composable () -> Unit)? = {
        ZarinaDatePickerDefaults.Headline(
            selectedDateMillis = state.selectedDateMillis,
            dateFormatter = dateFormatter,
            modifier = Modifier.padding(start = 24.dp, end = 12.dp, bottom = 12.dp),
        )
    },
    isModeToggleVisible: Boolean = true,
    colors: DatePickerColors = ZarinaDatePickerDefaults.colors(),
) {
    DatePicker(
        state = state,
        dateFormatter = dateFormatter,
        title = title,
        headline = headline,
        showModeToggle = isModeToggleVisible,
        colors = colors,
        modifier = modifier,
    )
}

public object ZarinaDatePickerDefaults {

    @Composable
    public fun Title(modifier: Modifier = Modifier) {
        Text(
            text = stringResource(R.string.uikit_select_date),
            style = UiKitTheme.typography.primary.regular,
            color = UiKitTheme.colors.text.general.regular.default,
            modifier = modifier,
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    public fun Headline(
        selectedDateMillis: Long?,
        dateFormatter: DatePickerFormatter,
        modifier: Modifier = Modifier,
        locale: Locale = LocalContext.current.getLocale(),
    ) {
        val formattedDate = dateFormatter.formatDate(
            dateMillis = selectedDateMillis,
            locale = locale,
        )

        Text(
            text = formattedDate ?: stringResource(R.string.uikit_selected_date),
            style = UiKitTheme.typography.heading1.regular,
            color = UiKitTheme.colors.text.general.regular.default,
            maxLines = 1,
            modifier = modifier,
        )
    }

    @Composable
    public fun ConfirmButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        text: String = stringResource(RCommon.string.res_select).uppercase(),
        colors: ZarinaButtonColors = ZarinaButtonDefaults.backlessColors(),
    ) {
        ZarinaButton(
            onClick = onClick,
            colors = colors,
            modifier = modifier,
        ) {
            Text(text = text)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    public fun colors(
        containerColor: Color = UiKitTheme.colors.background.general.regular.default,
        titleContentColor: Color = UiKitTheme.colors.text.general.regular.default,
        headlineContentColor: Color = UiKitTheme.colors.text.general.regular.default,
        weekdayContentColor: Color = UiKitTheme.colors.text.general.regular.muted,
        subheadContentColor: Color = UiKitTheme.colors.text.general.regular.default,
        navigationContentColor: Color = UiKitTheme.colors.text.general.regular.default,
        yearContentColor: Color = UiKitTheme.colors.text.general.regular.default,
        disabledYearContentColor: Color = Color.Unspecified,
        currentYearContentColor: Color = UiKitTheme.colors.text.general.regular.default,
        selectedYearContentColor: Color = UiKitTheme.colors.text.general.inversed.default,
        disabledSelectedYearContentColor: Color = Color.Unspecified,
        selectedYearContainerColor: Color = UiKitTheme.colors.background.general.inversed.default,
        disabledSelectedYearContainerColor: Color = Color.Unspecified,
        dayContentColor: Color = UiKitTheme.colors.text.general.regular.default,
        disabledDayContentColor: Color = Color.Unspecified,
        selectedDayContentColor: Color = UiKitTheme.colors.text.general.inversed.default,
        disabledSelectedDayContentColor: Color = Color.Unspecified,
        selectedDayContainerColor: Color = UiKitTheme.colors.background.general.inversed.default,
        disabledSelectedDayContainerColor: Color = Color.Unspecified,
        todayContentColor: Color = UiKitTheme.colors.text.general.regular.default,
        todayDateBorderColor: Color = UiKitTheme.colors.border.general.active,
        dayInSelectionRangeContentColor: Color = Color.Unspecified,
        dayInSelectionRangeContainerColor: Color = Color.Unspecified,
        dividerColor: Color = UiKitTheme.colors.background.skeleton,
        dateTextFieldColors: TextFieldColors? = TextFieldDefaults.colors(
            focusedTextColor = UiKitTheme.colors.text.general.regular.default,
            focusedContainerColor = UiKitTheme.colors.background.general.regular.default,
            focusedIndicatorColor = UiKitTheme.colors.border.general.active,
            focusedLabelColor = UiKitTheme.colors.text.general.regular.default,
            unfocusedTextColor = UiKitTheme.colors.text.general.regular.default,
            unfocusedContainerColor = UiKitTheme.colors.background.general.regular.default,
            unfocusedIndicatorColor = UiKitTheme.colors.border.general.default,
            unfocusedLabelColor = UiKitTheme.colors.text.general.regular.muted,
            errorTextColor = UiKitTheme.colors.text.general.accent.red,
            errorContainerColor = UiKitTheme.colors.background.general.regular.default,
            errorIndicatorColor = UiKitTheme.colors.border.general.error,
            errorLabelColor = UiKitTheme.colors.text.general.accent.red,
            errorSupportingTextColor = UiKitTheme.colors.text.general.accent.red,
            cursorColor = UiKitTheme.colors.text.general.regular.default,
            errorCursorColor = UiKitTheme.colors.text.general.regular.default,
        ),
    ): DatePickerColors = DatePickerDefaults.colors(
        containerColor = containerColor,
        titleContentColor = titleContentColor,
        headlineContentColor = headlineContentColor,
        weekdayContentColor = weekdayContentColor,
        subheadContentColor = subheadContentColor,
        navigationContentColor = navigationContentColor,
        yearContentColor = yearContentColor,
        disabledYearContentColor = disabledYearContentColor,
        currentYearContentColor = currentYearContentColor,
        selectedYearContentColor = selectedYearContentColor,
        disabledSelectedYearContentColor = disabledSelectedYearContentColor,
        selectedYearContainerColor = selectedYearContainerColor,
        disabledSelectedYearContainerColor = disabledSelectedYearContainerColor,
        dayContentColor = dayContentColor,
        disabledDayContentColor = disabledDayContentColor,
        selectedDayContentColor = selectedDayContentColor,
        disabledSelectedDayContentColor = disabledSelectedDayContentColor,
        selectedDayContainerColor = selectedDayContainerColor,
        disabledSelectedDayContainerColor = disabledSelectedDayContainerColor,
        todayContentColor = todayContentColor,
        todayDateBorderColor = todayDateBorderColor,
        dayInSelectionRangeContentColor = dayInSelectionRangeContentColor,
        dayInSelectionRangeContainerColor = dayInSelectionRangeContainerColor,
        dividerColor = dividerColor,
        dateTextFieldColors = dateTextFieldColors,
    )
}
