package ru.livetyping.zarina.presentation.screen.loyaltyprogram.bonushistory

import ru.livetyping.zarina.presentation.screen.loyaltyprogram.bonushistory.paging.LoyaltyProgramBonusActionPager
import javax.inject.Inject

class BonusHistoryInteractor @Inject constructor(
    val bonusActionPager: LoyaltyProgramBonusActionPager,
)
