// Archivo: commonMain/kotlin/org/acmevision/project/db/DatabaseExtensions.kt

import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import org.acmevision.project.finanzas.db.AppDatabase

// Necesitas la función de extensión 'transaction' del SqlDriver
// que está disponible en commonMain.

suspend fun <R> AppDatabase.runInTransaction(
    block: AppDatabase.() -> R // Usa la firma de AppDatabase para acceder a las queries
): R = withContext(Dispatchers.Default) {

    // ✅ LLAMA A LA FUNCIÓN DE TRANSACCIÓN DIRECTAMENTE SOBRE EL OBJETO 'this' (AppDatabase)
    // Usamos el método de transacción síncrono proporcionado por SQLDelight.
    this@runInTransaction.transaction { // NOTA: 'transaction' sin el driver explícito
        // El bloque lambda de transaction se ejecuta en el contexto de AppDatabase.
        block(this@runInTransaction)
    } as R
}