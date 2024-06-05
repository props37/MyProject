package ru.livetyping.zarina.data.productsearch.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.livetyping.zarina.domain.productsearch.ProductSearchHistoryQuery

@Entity(
    tableName = ProductSearchHistoryQueryEntity.TABLE_NAME,
    indices = [
        Index(
            value = [ProductSearchHistoryQueryEntity.FIELD_TIMESTAMP_MILLIS],
            orders = [Index.Order.DESC],
        )
    ],
)
data class ProductSearchHistoryQueryEntity(
    @PrimaryKey
    @ColumnInfo(name = FIELD_TEXT)
    val text: String,

    @ColumnInfo(name = FIELD_TIMESTAMP_MILLIS)
    val timestampMillis: Long
) {
    fun toProductSearchHistoryQuery(): ProductSearchHistoryQuery {
        return ProductSearchHistoryQuery(
            text = text,
            timestampMillis = timestampMillis,
        )
    }

    companion object {
        const val TABLE_NAME = "product_search_history_query"

        const val FIELD_TEXT = "product_search_history_query_text"
        const val FIELD_TIMESTAMP_MILLIS = "product_search_history_query_timestamp_millis"

        fun from(entry: ProductSearchHistoryQuery): ProductSearchHistoryQueryEntity {
            return ProductSearchHistoryQueryEntity(
                text = entry.text.lowercase().trim(),
                timestampMillis = entry.timestampMillis,
            )
        }
    }
}
