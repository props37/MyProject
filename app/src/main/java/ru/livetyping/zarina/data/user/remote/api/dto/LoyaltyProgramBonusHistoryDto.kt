package ru.livetyping.zarina.data.user.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.data.common.remote.api.zarina.dto.PaginationInfoDto
import ru.livetyping.zarina.domain.common.Page
import ru.livetyping.zarina.domain.user.LoyaltyProgramBonusAction
import ru.livetyping.zarina.util.kotlin.date.LocalDateUtil

@Serializable
data class LoyaltyProgramBonusHistoryDto(
    @SerialName("items")
    val items: List<Item>? = null,

    @SerialName("pagination")
    val paginationInfo: PaginationInfoDto? = null,
) {
    fun toLoyaltyProgramBonusActionPage(): Page<List<LoyaltyProgramBonusAction>> {
        checkNotNull(items) { "items is null" }
        checkNotNull(paginationInfo) { "paginationInfo is null" }
        val data = items.map {
            it.toLoyaltyProgramBonusAction()
        }
        return Page(
            data = data,
            // TODO: [High] Implement when itemTotalCount is added to response
            paginationInfo = paginationInfo.toPaginationInfo(Int.MAX_VALUE),
        )
    }

    @Serializable
    data class Item(
        @SerialName("bonus_count")
        val bonusCount: Int? = null,

        @SerialName("date")
        val date: String? = null,

        @SerialName("expirate")
        val expirationDate: String? = null,

        @SerialName("title")
        val title: String? = null,

        @SerialName("type")
        val type: Type? = null,
    ) {
        fun toLoyaltyProgramBonusAction(): LoyaltyProgramBonusAction {
            checkNotNull(bonusCount) { "bonusCount is null" }
            checkNotNull(title) { "title is null" }
            checkNotNull(type) { "type is null" }
            checkNotNull(date) { "date is null" }
            val date = date
                .takeIf { it.isNotBlank() }
                ?.let { LocalDateUtil.parseRussianDate(it) }
            val expirationDate = expirationDate
                ?.takeIf { it.isNotBlank() }
                ?.let { LocalDateUtil.parseRussianDate(it) }
            return LoyaltyProgramBonusAction(
                bonusCount = bonusCount,
                title = title,
                type = type.toLoyaltyProgramBonusActionType(),
                date = date,
                expirationDate = expirationDate,
            )
        }

        @Serializable
        @JvmInline
        value class Type(private val value: String) {
            fun toLoyaltyProgramBonusActionType(): LoyaltyProgramBonusAction.Type = when (value) {
                EARNED -> LoyaltyProgramBonusAction.Type.EARNED
                SPENT -> LoyaltyProgramBonusAction.Type.SPENT
                else -> error("Unknown LoyaltyProgramBonusActionType $value")
            }

            companion object {
                private const val EARNED = "charge"
                private const val SPENT = "charge-off"
            }
        }
    }
}
