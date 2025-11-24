package org.acmevision.project.domain

import kotlinx.datetime.LocalDateTime

enum class TransactionType {
    INCOME,   // Ingreso (Money entering the system/account)
    EXPENSE,  // Gasto (Money leaving the system/account for goods/services)
    TRANSFER,  // Transferencia (Money moving between two internal accounts)
    NONE
}

data class Transaction(
    val id: String,
    val amount: Double,
    val date: LocalDateTime,
    val note: String?,
    val type: TransactionType,

    // Relaciones
    val categoryId: String?, // Null en transferencias
    val accountId: String,   // Cuenta origen (o la cuenta donde se gasta/ingresa)
    val targetAccountId: String? = null, // Solo para TRANSFER: Cuenta destino

    // Para rastrear si vino de una recurrencia
    val recurringTransactionId: String? = null
)