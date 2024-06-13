package ru.livetyping.zarina.presentation.screen.loyaltyprogram.bonushistory.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import ru.livetyping.zarina.data.user.UserRepository
import ru.livetyping.zarina.data.user.pagination.LoyaltyProgramBonusActionPagingSource
import ru.livetyping.zarina.domain.user.LoyaltyProgramBonusAction
import javax.inject.Inject

class LoyaltyProgramBonusActionPager @Inject constructor(
    private val userRepository: UserRepository,
) {
    fun getBonusHistoryPagingDataFlow(): Flow<PagingData<LoyaltyProgramBonusAction>> {
        return Pager(
            config = getPagingConfig(),
            pagingSourceFactory = {
                LoyaltyProgramBonusActionPagingSource(
                    fetchBonusActionPage = { page ->
                        userRepository.getLoyaltyCardBonusHistoryPageFlow(page).first()
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
                    fetchBonusActionPage = { page ->
                        userRepository.getLoyaltyCardExpectedBonusesPageFlow(page).first()
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
