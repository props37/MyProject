package ru.livetyping.zarina.data.user.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.pagination.Page
import ru.livetyping.zarina.core.domain.model.user.LoyaltyProgramBonusAction
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull
import ru.livetyping.zarina.core.network.zarina.dto.PaginationInfoDto
import timber.log.Timber
import java.time.LocalDate

@Serializable
internal data class LoyaltyProgramBonusHistoryDto(
    @SerialName("items")
    val items: List<Item>? = null,

    @SerialName("items_count")
    val itemCount: Int? = null,

    @SerialName("pagination")
    val pagination: PaginationInfoDto? = null,
) {
    fun toLoyaltyProgramBonusActionPage(): Page<List<LoyaltyProgramBonusAction>> {
        checkPropertyNotNull(items) { "items" }
        checkPropertyNotNull(itemCount) { "items_count" }
        checkPropertyNotNull(pagination) { "pagination" }
        val data = items.mapNotNull { it.toLoyaltyProgramBonusAction() }
        return Page(
            data = data,
            paginationInfo = pagination.toPaginationInfo(itemCount),
        )
    }

    @Serializable
    data class Item(
        @SerialName("bonus_count")
        val bonusCount: Int? = null,

        @SerialName("date")
        val date: String? = null,

        @SerialName("expirate")
        val expirate: String? = null,

        @SerialName("title")
        val title: String? = null,

        @SerialName("type")
        val type: Type? = null,
    ) {
        fun toLoyaltyProgramBonusAction(): LoyaltyProgramBonusAction? {
            val type = type?.toLoyaltyProgramBonusActionType()
            return if (bonusCount != null && title != null && type != null) {
                val date = date
                    ?.takeIf { it.isNotBlank() }
                    ?.let { LocalDate.parse(it) }
                val expirationDate = expirate
                    ?.takeIf { it.isNotBlank() }
                    ?.let { LocalDate.parse(it) }
                return LoyaltyProgramBonusAction(
                    bonusCount = bonusCount,
                    title = title,
                    type = type,
                    date = date,
                    expirationDate = expirationDate,
                )
            } else {
                Timber.tag(TAG).e("Ignore $this because it can't be mapped to LoyaltyProgramBonusAction")
                null
            }
        }

        @Serializable
        @JvmInline
        value class Type(private val value: String) {
            fun toLoyaltyProgramBonusActionType(): LoyaltyProgramBonusAction.Type? {
                return when (value) {
                    EARNED -> LoyaltyProgramBonusAction.Type.EARNED
                    SPENT -> LoyaltyProgramBonusAction.Type.SPENT
                    else -> {
                        Timber.tag(TAG).e("Unknown LoyaltyProgramBonusActionType $value")
                        null
                    }
                }
            }

            companion object {
                private const val EARNED = "charge"
                private const val SPENT = "charge-off"
            }
        }
    }

    private companion object {
        private const val TAG = "LoyaltyProgramBonusHistoryDto"
    }
}
