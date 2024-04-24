package ru.livetyping.zarina.ui.screen.myorders

import ru.livetyping.zarina.ui.screen.myorders.paging.OrderPager
import javax.inject.Inject

class MyOrdersInteractor @Inject constructor(
    val orderPager: OrderPager,
)
