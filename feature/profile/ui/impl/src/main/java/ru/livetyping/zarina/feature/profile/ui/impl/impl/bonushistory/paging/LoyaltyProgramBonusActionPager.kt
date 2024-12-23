package ru.livetyping.zarina.feature.profile.ui.impl.impl.bonushistory.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import ru.livetyping.zarina.core.domain.model.user.LoyaltyProgramBonusAction
import ru.livetyping.zarina.core.domain.usecase.user.GetLoyaltyProgramBonusHistoryPageFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetLoyaltyProgramExpectedBonusesPageFlowUseCase
import javax.inject.Inject

internal class LoyaltyProgramBonusActionPager @Inject constructor(
    private val getLoyaltyProgramBonusHistoryPageFlow: GetLoyaltyProgramBonusHistoryPageFlowUseCase,
    private val getLoyaltyProgramExpectedBonusesPageFlow: GetLoyaltyProgramExpectedBonusesPageFlowUseCase,
) {
    fun getBonusHistoryPagingDataFlow(): Flow<PagingData<LoyaltyProgramBonusAction>> {
        return Pager(
            config = getPagingConfig(),
            pagingSourceFactory = {
                LoyaltyProgramBonusActionPagingSource(
                    bonusActionPageProvider = { page ->
                        val params = GetLoyaltyProgramBonusHistoryPageFlowUseCase.Params(page)
                        val bonusActionPageResult =
                            getLoyaltyProgramBonusHistoryPageFlow(params).firstOrNull()
                        val bonusActionPage = bonusActionPageResult?.getOrNull()
                        checkNotNull(bonusActionPage) { "Failed to get loyalty program bonus history" }
                        bonusActionPage
                    },
                )
            }
        ).flow
    }

    fun getExpectedBonusesPagingDataFlow(): Flow<PagingData<LoyaltyProgramBonusAction>> {
        return Pager(
            config = getPagingConfig(),
            pagingSourceFactory = {
                LoyaltyProgramBonusActionPagingSource(
                    bonusActionPageProvider = { page ->
                        val params = GetLoyaltyProgramExpectedBonusesPageFlowUseCase.Params(page)
                        val bonusActionPageResult =
                            getLoyaltyProgramExpectedBonusesPageFlow(params).firstOrNull()
                        val bonusActionPage = bonusActionPageResult?.getOrNull()
                        checkNotNull(bonusActionPage) { "Failed to get loyalty program expected bonuses" }
                        bonusActionPage
                    },
                )
            }
        ).flow
    }

    private fun getPagingConfig(): PagingConfig {
        return PagingConfig(
            pageSize = PAGE_SIZE,
            prefetchDistance = PREFETCH_DISTANCE,
            enablePlaceholders = false,
            initialLoadSize = INITIAL_LOAD_SIZE,
            maxSize = MAX_SIZE,
        )
    }

    companion object {
        private const val PAGE_SIZE = 20
        private const val PREFETCH_DISTANCE = PAGE_SIZE
        private const val INITIAL_LOAD_SIZE = PAGE_SIZE * 2
        private const val MAX_SIZE = 300
    }
}
