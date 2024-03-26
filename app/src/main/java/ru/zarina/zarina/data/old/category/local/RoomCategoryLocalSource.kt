package ru.zarina.zarina.data.old.category.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.old.category.local.database.CategoryDao
import ru.zarina.zarina.data.old.category.local.database.entity.CategoryEntity
import ru.zarina.zarina.domain.old.Category

@Factory
class RoomCategoryLocalSource(
    private val dao: CategoryDao,
) : ICategoryLocalSource {

    override suspend fun addCategories(categories: List<Category>) {
        val entities = buildList {
            addAll(categories.mapIndexed { index, item ->
                CategoryEntity.from(
                    category = item,
                    displayOrder = index,
                    isRoot = true
                )
            })
            addAll(
                categories.flatMap { category ->
                    category.getFlattenedSubcategories()
                        .mapIndexed { index, item -> CategoryEntity.from(item, index) }
                }
            )
        }
        dao.insert(entities)
    }

    override fun getCategories(): Flow<List<Category>> = dao.select().map { entities ->
        val entitiesById = entities.associateBy { it.id }

        fun CategoryEntity.toCategory(): Category {
            val children = this.childrenIds?.mapNotNull { entitiesById[it]?.toCategory() }.orEmpty()
            return this.toDomain(children)
        }

        entities.filter { it.isRoot }.sortedBy { it.displayOrder }.map { it.toCategory() }
    }

    override fun getCategory(id: Category.Id) = getCategories()
        .map { categories ->
            categories.firstOrNull { it.id == id }
                ?: categories.flatMap { it.getFlattenedSubcategories() }.firstOrNull { it.id == id }
                ?: Category(
                    id = id,
                    image = null,
                    name = "",
                    subcategories = emptyList(),
                )
        }

}
