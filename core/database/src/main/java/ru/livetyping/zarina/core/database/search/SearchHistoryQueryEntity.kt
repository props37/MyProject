package ru.livetyping.zarina.core.database.search

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.livetyping.zarina.core.domain.model.search.SearchHistoryQuery

@Entity(
    tableName = SearchHistoryQueryEntity.TABLE_NAME,
    indices = [
        Index(
            value = [SearchHistoryQueryEntity.FIELD_TIMESTAMP_MILLIS],
            orders = [Index.Order.DESC],
        ),
    ],
)
public data class SearchHistoryQueryEntity(
    @PrimaryKey
    @ColumnInfo(name = FIELD_TEXT)
    val text: String,

    @ColumnInfo(name = FIELD_TIMESTAMP_MILLIS)
    val timestampMillis: Long
) {
    public fun toSearchHistoryQuery(): SearchHistoryQuery {
        return SearchHistoryQuery(
            text = text,
            timestampMillis = timestampMillis,
        )
    }

    public companion object {
        internal const val TABLE_NAME = "search_history_query"

        internal const val FIELD_TEXT = "search_history_query_text"
        internal const val FIELD_TIMESTAMP_MILLIS = "search_history_query_timestamp_millis"

        public fun from(query: SearchHistoryQuery): SearchHistoryQueryEntity {
            return SearchHistoryQueryEntity(
                text = query.text,
                timestampMillis = query.timestampMillis,
            )
        }
    }
}
