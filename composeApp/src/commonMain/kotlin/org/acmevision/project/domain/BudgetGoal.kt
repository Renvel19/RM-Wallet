package org.acmevision.project.domain

data class BudgetGoal(
    val id: String,
    val categoryId: String,
    val limitAmount: Double,
    val periodYear: Int,  // Ej: 2025
    val periodMonth: Int  // Ej: 11 (Noviembre) - Ajustado a tu "mes financiero"
)