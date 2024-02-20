package ru.zarina.zarina.util.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberEndlessPagerState(itemCount: Int): PagerState {
    val pageCount = (itemCount * PAGE_COUNT_MULTIPLIER).coerceAtMost(PAGE_COUNT_MAX_VALUE)
    return rememberPagerState(
        initialPage = (pageCount / 2).roundToProductOf(itemCount),
        pageCount = { pageCount },
    )
}

private fun Int.roundToProductOf(other: Int): Int {
    if (other == 0) return this
    val divisionReminder = this % other
    return when {
        divisionReminder == 0 -> this
        divisionReminder > other / 2 -> this - divisionReminder + other
        else -> this - divisionReminder
    }
}

private const val PAGE_COUNT_MULTIPLIER = 200
private const val PAGE_COUNT_MAX_VALUE = 5_000
