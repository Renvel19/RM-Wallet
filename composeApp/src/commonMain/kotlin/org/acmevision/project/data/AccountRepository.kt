package org.acmevision.project.data
// Archivo: commonMain/kotlin/.../data/AccountRepository.kt

import kotlinx.coroutines.flow.map
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import org.acmevision.project.finanzas.db.AccountEntityQueries
import org.acmevision.project.finanzas.db.AccountEntity // La data class generada por SQLDelight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import org.acmevision.project.domain.Account
import org.acmevision.project.domain.AccountType
import kotlin.collections.map

interface AccountRepository {
    fun getAllAccounts() : kotlinx.coroutines.flow.Flow<List<Account>>
    suspend fun insertAccount(account: Account)
    suspend fun updateBalance(accountId: String, newBalance: Double)

    suspend fun getAccountById(accountId: String): Account?
}

class AccountRepositoryImpl(
    private val queries: AccountEntityQueries // Inyectado desde Koin
) : AccountRepository {

    // Mapeador simple (Necesario para convertir de Entity a tu Modelo de Dominio)
    private fun AccountEntity.toDomain() = Account(
        id = this.id,
        name = this.name,
        // Aquí deberás hacer la conversión de String a AccountType
        type = AccountType.valueOf(this.type),
        currentBalance = this.currentBalance,
        creditLimit = this.creditLimit,
        cutoffDay = this.cutoffDay?.toInt()
    )

    override fun getAllAccounts() = queries.getAllAccounts()
        .asFlow()
        .mapToList(Dispatchers.Default) // Convierte la lista SQL a lista de Entity
        .map { list -> list.map { it.toDomain() } } // Convierte la lista de Entity a lista de Dominio

    override suspend fun insertAccount(account: Account) {
        queries.insertAccount(
            id = account.id,
            name = account.name,
            type = account.type.name, // Guarda el ENUM como String
            currentBalance = account.currentBalance,
            creditLimit = account.creditLimit,
            cutoffDay = account.cutoffDay?.toLong()
        )
    }

    override suspend fun updateBalance(accountId: String, newBalance: Double) {
        queries.updateBalance(newBalance, accountId)
    }

    override suspend fun getAccountById(accountId: String): Account? =
        queries.getAccountById(accountId)
            .executeAsOneOrNull()
            ?.toDomain()
}