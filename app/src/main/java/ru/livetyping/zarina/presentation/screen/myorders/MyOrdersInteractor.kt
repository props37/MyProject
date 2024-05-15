package ru.livetyping.zarina.presentation.screen.myorders

import ru.livetyping.zarina.presentation.screen.myorders.paging.OrderPager
import javax.inject.Inject

class MyOrdersInteractor @Inject constructor(
    val orderPager: OrderPager,
)
