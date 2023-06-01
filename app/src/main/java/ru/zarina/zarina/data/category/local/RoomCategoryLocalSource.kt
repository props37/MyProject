package ru.zarina.zarina.data.category.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.zarina.zarina.data.category.local.database.CategoryDao
import ru.zarina.zarina.data.category.local.database.entity.CategoryEntity
import ru.zarina.zarina.domain.Category
import javax.inject.Inject

class RoomCategoryLocalSource @Inject constructor(
    private val dao: CategoryDao,
) : ICategoryLocalSource {

    override suspend fun addCategories(categories: List<Category>) {
        val entities = buildList {
            addAll(categories.mapIndexed { index, item -> CategoryEntity.from(item, index) })
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
        .map { categories -> categories.firstOrNull { it.id == id } }

}
