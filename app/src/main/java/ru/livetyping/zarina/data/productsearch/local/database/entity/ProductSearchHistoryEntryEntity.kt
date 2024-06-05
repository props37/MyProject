package ru.livetyping.zarina.data.productsearch.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ru.livetyping.zarina.domain.productsearch.ProductSearchHistoryEntry

@Entity(
    tableName = ProductSearchHistoryEntryEntity.TABLE_NAME,
    indices = [
        Index(
            value = [ProductSearchHistoryEntryEntity.FIELD_TIMESTAMP_MILLIS],
            orders = [Index.Order.DESC],
        )
    ],
)
data class ProductSearchHistoryEntryEntity(
    @PrimaryKey
    @ColumnInfo(name = FIELD_TEXT)
    val text: String,

    @ColumnInfo(name = FIELD_TIMESTAMP_MILLIS)
    val timestampMillis: Long
) {
    fun toProductSearchHistoryEntry(): ProductSearchHistoryEntry {
        return ProductSearchHistoryEntry(
            text = text,
            timestampMillis = timestampMillis,
        )
    }

    companion object {
        const val TABLE_NAME = "product_search_history_entry"

        const val FIELD_TEXT = "product_search_history_entry_text"
        const val FIELD_TIMESTAMP_MILLIS = "product_search_history_entry_"

        fun from(entry: ProductSearchHistoryEntry): ProductSearchHistoryEntryEntity {
            return ProductSearchHistoryEntryEntity(
                text = entry.text.lowercase().trim(),
                timestampMillis = entry.timestampMillis,
            )
        }
    }
}
