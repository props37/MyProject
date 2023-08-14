package ru.zarina.zarina.data.search.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history_table")
data class SearchHistoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "query")
    val query: String,
) {

    fun toDomain(): String = query

    companion object {
        fun from(domain: String): SearchHistoryEntity = SearchHistoryEntity(domain)
    }

}