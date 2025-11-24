package org.acmevision.project.domain

import kotlinx.datetime.LocalDateTime

enum class RecurrenceInterval {
    NONE,    // Transacción única (no recurrente)
    DAILY,   // Diariamente
    WEEKLY,  // Semanalmente
    MONTHLY, // Mensualmente (lo más común para salarios y alquileres)
    YEARLY   // Anualmente
}

data class RecurringTransaction(
    val id: String,
    val amount: Double,
    val note: String,
    val type: TransactionType,
    val categoryId: String?,
    val accountId: String,
    val targetAccountId: String? = null,

    val interval: RecurrenceInterval,
    val nextExecutionDate: LocalDateTime,
    val isActive: Boolean = true
)