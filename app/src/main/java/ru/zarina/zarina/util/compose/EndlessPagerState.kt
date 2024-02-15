package ru.zarina.zarina.util.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun rememberEndlessPagerState(itemCount: Int): PagerState {
    val pageCount = PAGE_COUNT
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

private const val PAGE_COUNT = 10_000
