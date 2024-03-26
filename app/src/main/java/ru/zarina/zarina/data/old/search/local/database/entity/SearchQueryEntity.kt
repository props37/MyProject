package ru.zarina.zarina.data.old.search.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "search_history_table",
    indices = [Index(value = ["query"], unique = true)]
)
data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo(name = "query")
    val query: String,
) {

    fun toDomain(): String = query

    companion object {
        fun from(query: String): SearchHistoryEntity = SearchHistoryEntity(query = query)
    }

}
