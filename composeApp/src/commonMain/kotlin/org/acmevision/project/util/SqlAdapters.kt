package org.acmevision.project.util

// Archivo: commonMain/kotlin/org/acmevision/project/util/SqlAdapters.kt

import app.cash.sqldelight.ColumnAdapter
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.acmevision.project.domain.AccountType
import org.acmevision.project.domain.TransactionType
import org.acmevision.project.domain.RecurrenceInterval

// Adaptador para AccountType
val accountTypeAdapter = object : ColumnAdapter<AccountType, String> {
    override fun decode(databaseValue: String): AccountType = AccountType.valueOf(databaseValue)
    override fun encode(value: AccountType): String = value.name
}

// Adaptador para TransactionType
val transactionTypeAdapter = object : ColumnAdapter<TransactionType, String> {
    override fun decode(databaseValue: String): TransactionType = TransactionType.valueOf(databaseValue)
    override fun encode(value: TransactionType): String = value.name
}

// Adaptador para RecurrenceInterval
val recurrenceIntervalAdapter = object : ColumnAdapter<RecurrenceInterval, String> {
    override fun decode(databaseValue: String): RecurrenceInterval = RecurrenceInterval.valueOf(databaseValue)
    override fun encode(value: RecurrenceInterval): String = value.name
}

val localDateTimeAdapter = object : ColumnAdapter<LocalDateTime, String> {

    // 1. Decodificación (DB String -> Kotlin LocalDateTime)
    override fun decode(databaseValue: String): LocalDateTime {
        // Asumimos el formato estándar ISO 8601 (ej: 2025-11-24T17:09:08.000)
        return databaseValue.toLocalDateTime()
    }

    // 2. Codificación (Kotlin LocalDateTime -> DB String)
    override fun encode(value: LocalDateTime): String {
        // Convierte el objeto LocalDateTime de Kotlin a su representación String
        return value.toString()
    }
}
// Nota: Para kotlinx.datetime.LocalDateTime, se recomienda usar una librería de terceros
// o un adaptador similar si lo usaste en tu .sq. Por ahora, asumiremos que solo necesitas los ENUMs.