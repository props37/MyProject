package ru.livetyping.zarina.data.old.category.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.livetyping.zarina.domain.old.Category
import ru.livetyping.zarina.domain.old.Url

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "image")
    val image: String?,
    @ColumnInfo(name = "children_ids")
    val childrenIds: List<Int>? = null,
    @ColumnInfo(name = "is_root")
    val isRoot: Boolean,
    @ColumnInfo(name = "display_order")
    val displayOrder: Int,
) {

    companion object {
        fun from(category: Category, displayOrder: Int, isRoot: Boolean = false): CategoryEntity =
            CategoryEntity(
                id = category.id.value,
                name = category.name,
                image = category.image?.value,
                childrenIds = category.subcategories.map { it.id.value }.takeIf { it.isNotEmpty() },
                isRoot = isRoot,
                displayOrder = displayOrder,
            )
    }

    fun toDomain(children: List<Category>): Category = Category(
        id = Category.Id(id),
        name = name,
        image = image?.let { Url(it) },
        subcategories = children
    )

}
