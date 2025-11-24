package org.acmevision.project.data

// Archivo: commonMain/kotlin/.../data/TransactionRepository.kt

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime
import org.acmevision.project.domain.Transaction
import org.acmevision.project.domain.TransactionType
import org.acmevision.project.finanzas.db.AppDatabase
import org.acmevision.project.finanzas.db.TransactionEntity
import org.acmevision.project.finanzas.db.TransactionQueries

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<Transaction>>
    suspend fun recordNewTransaction(transaction: Transaction)
    fun getExpensesForBudget(startDate: String, endDate: String): Flow<List<Transaction>>
}

// Archivo: commonMain/kotlin/.../data/TransactionRepositoryImpl.kt

class TransactionRepositoryImpl(
    private val transactionQueries: TransactionQueries,
    private val accountRepository: AccountRepository,
    private val db: AppDatabase
) : TransactionRepository {

    // --- Mapeo de Entidad a Dominio ---
    private fun TransactionEntity.toDomain() = Transaction(
        id = this.id,
        amount = this.amount,
        // La fecha ya viene como LocalDateTime gracias al Type Adapter en Koin
        date = LocalDateTime.parse(this.date),
        note = this.note,
        type = TransactionType.valueOf(this.type as String),
        categoryId = this.categoryId,
        accountId = this.accountId,
        targetAccountId = this.targetAccountId
    )


    // ************************************************
    // OPERACIONES DE LECTURA (READ)
    // ************************************************

    override fun getAllTransactions(): Flow<List<Transaction>> =
        transactionQueries.getAllTransactions() // Query que ordena por fecha DESC
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { list -> list.map { it.toDomain() } }

    override fun getExpensesForBudget(startDate: String, endDate: String): Flow<List<Transaction>> =
        transactionQueries.getTransactionsForBudget(startDate , endDate )
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { list -> list.map { it.toDomain() } }

    // ************************************************
    // OPERACIONES DE ESCRITURA (WRITE)
    // ************************************************

    override suspend fun recordNewTransaction(transaction: Transaction) {
        withContext(Dispatchers.Default) {
            val amount = transaction.amount

            // 1. OBTENER LAS CUENTAS Y CALCULAR NUEVOS SALDOS (REQUIERE 'suspend')

            // Declaramos variables para los nuevos saldos fuera del bloque síncrono
            var newSourceBalance: Double = 0.0
            var newTargetBalance: Double? = null // Solo para TRANSFER

            // Usamos las funciones 'suspend' aquí, donde son válidas:
            val sourceAccount = accountRepository.getAccountById(transaction.accountId)
                ?: throw IllegalStateException("Cuenta de origen no encontrada: ${transaction.accountId}")

            when (transaction.type) {
                TransactionType.INCOME -> {
                    newSourceBalance = sourceAccount.currentBalance + amount
                }

                TransactionType.EXPENSE -> {
                    newSourceBalance = sourceAccount.currentBalance - amount
                }

                TransactionType.TRANSFER -> {
                    val targetAccountId = transaction.targetAccountId
                        ?: throw IllegalArgumentException("Transferencia requiere cuenta destino.")

                    val targetAccount = accountRepository.getAccountById(targetAccountId)
                        ?: throw IllegalStateException("Cuenta de destino no encontrada: $targetAccountId")

                    newSourceBalance = sourceAccount.currentBalance - amount
                    newTargetBalance = targetAccount.currentBalance + amount
                }
                TransactionType.NONE -> {}
            }


            // 2. EJECUTAR TRANSACCIÓN SÍNCRONA (NO REQUIERE 'suspend')

            // Ahora, db.transaction solo ejecuta las queries finales
            db.transaction {

                // A. Aplicar el nuevo saldo a la cuenta de origen (síncrona)
                // Llama a la query directa de SQLDelight, NO a la función 'suspend' del repositorio.
                db.accountEntityQueries.updateBalance(
                    currentBalance = newSourceBalance,
                    id = transaction.accountId
                )

                // B. Aplicar el nuevo saldo a la cuenta destino (si es TRANSFER)
                if (transaction.type == TransactionType.TRANSFER && newTargetBalance != null) {
                    db.accountEntityQueries.updateBalance(
                        currentBalance = newTargetBalance,
                        id = transaction.targetAccountId!!
                    )
                }

                // C. Registrar la transacción
                transactionQueries.insertTransaction(
                    id = transaction.id,
                    amount = transaction.amount,
                    date = transaction.date.toString() ,
                    note = transaction.note,
                    type = transaction.type.name,
                    categoryId = transaction.categoryId,
                    accountId = transaction.accountId,
                    targetAccountId = transaction.targetAccountId
                )
            }
        }

    }
}