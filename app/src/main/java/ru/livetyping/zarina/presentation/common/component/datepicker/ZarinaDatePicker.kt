package ru.livetyping.zarina.presentation.common.component.datepicker

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
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.platform.locale
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZarinaDatePicker(
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
    showModeToggle: Boolean = true,
    colors: DatePickerColors = ZarinaDatePickerDefaults.colors(),
) {
    DatePicker(
        state = state,
        dateFormatter = dateFormatter,
        title = title,
        headline = headline,
        showModeToggle = showModeToggle,
        colors = colors,
        modifier = modifier,
    )
}

object ZarinaDatePickerDefaults {

    @Composable
    fun Title(modifier: Modifier = Modifier) {
        Text(
            text = stringResource(R.string.select_date),
            style = UiKitTheme.typography.primary.regular,
            color = UiKitTheme.colors.text.general.regular.default,
            modifier = modifier,
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Headline(
        selectedDateMillis: Long?,
        dateFormatter: DatePickerFormatter,
        modifier: Modifier = Modifier,
        locale: Locale = LocalContext.current.locale,
    ) {
        val formattedDate = dateFormatter.formatDate(
            dateMillis = selectedDateMillis,
            locale = locale,
        )

        Text(
            text = formattedDate ?: stringResource(R.string.selected_date),
            style = UiKitTheme.typography.heading1.regular,
            color = UiKitTheme.colors.text.general.regular.default,
            maxLines = 1,
            modifier = modifier,
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun colors(
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
