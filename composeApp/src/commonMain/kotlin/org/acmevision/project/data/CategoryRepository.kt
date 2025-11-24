package org.acmevision.project.data

// commonMain/kotlin/.../data/CategoryRepository.kt

import kotlinx.coroutines.flow.Flow
import org.acmevision.project.domain.Category
import org.acmevision.project.domain.TransactionType
import org.acmevision.project.finanzas.db.CategoryEntity

interface CategoryRepository {
    fun getAllCategories(): Flow<List<Category>>
    suspend fun insertCategory(category: Category)
    suspend fun getCategoryById(id: String): Category?
}
