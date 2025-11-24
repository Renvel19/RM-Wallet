package org.acmevision.project.di

// Archivo: commonMain/kotlin/org/acmevision/project/di/AppModule.kt

import org.acmevision.project.data.AccountRepository
import org.acmevision.project.data.AccountRepositoryImpl
import org.koin.dsl.module
import org.acmevision.project.finanzas.db.AppDatabase
import org.acmevision.project.db.DatabaseDriverFactory
import org.acmevision.project.finanzas.db.CategoryEntity
import org.acmevision.project.finanzas.db.RecurringTransactionEntity
import org.acmevision.project.finanzas.db.TransactionEntity
import org.acmevision.project.util.localDateTimeAdapter
import org.acmevision.project.util.recurrenceIntervalAdapter
import org.acmevision.project.util.transactionTypeAdapter

// Asume que la clase AppDatabase fue generada por SQLDelight

val databaseModule = module {
    // 1. Proporcionar la Fábrica de Drivers (Inyectada por la plataforma)
    // El 'single' para DatabaseDriverFactory se implementa en Android/iOS/Desktop
    single { get<DatabaseDriverFactory>().createDriver() }

    // 2. Proporcionar la Base de Datos Completa (AppDatabase)
    single { AppDatabase(
        get(),
    ) }

    // 3. Proporcionar el DAO (Data Access Object) o Queries
    single { get<AppDatabase>().accountEntityQueries }
    single { get<AppDatabase>().transactionQueries }
    single { get<AppDatabase>().settingsQueries }
    single { get<AppDatabase>().budgetQueries }
    single { get<AppDatabase>().categoryQueries }
    single { get<AppDatabase>().recurringTransactionQueries }
    // Aquí, AppDatabase.accountEntityQueries es generado por SQLDelight

    single<AccountRepository> { AccountRepositoryImpl(queries = get()) }
}
