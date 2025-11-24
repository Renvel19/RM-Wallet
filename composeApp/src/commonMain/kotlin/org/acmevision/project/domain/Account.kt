package org.acmevision.project.domain

enum class AccountType { SAVINGS, CREDIT_CARD, INVESTMENT }


data class Account(
    val id: String,
    val name: String,
    val type: AccountType,
    val currentBalance: Double, // Para Crédito, esto será negativo usualmente
    // Campos específicos para Tarjeta de Crédito (nullables para otros tipos)
    val creditLimit: Double? = null,
    val cutoffDay: Int? = null // Día del mes (ej: 15)
)


