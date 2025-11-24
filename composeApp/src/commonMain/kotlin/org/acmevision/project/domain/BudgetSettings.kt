package org.acmevision.project.domain

data class BudgetSettings(
    val startDayOfMonth: Int, // El día que le pagan (ej: 15 o 30)
    val globalLimit: Double? = null // Opcional: Límite global mensual
)