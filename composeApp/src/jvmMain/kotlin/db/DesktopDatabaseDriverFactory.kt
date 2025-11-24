package db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.acmevision.project.finanzas.db.AppDatabase
import java.io.File

class DesktopDatabaseDriverFactory : DatabaseDriverFactory {
    override fun createDriver(): SqlDriver {
        // Definimos la ruta del archivo. En un caso real, usa AppData o UserHome.
        val dbFilePath = "finance.db"
        val driver = JdbcSqliteDriver("jdbc:sqlite:$dbFilePath")

        // IMPORTANTE: En Desktop (JDBC), debemos crear las tablas manualmente
        // si el archivo no existe. Android e iOS lo hacen automático.
        if (!File(dbFilePath).exists()) {
            AppDatabase.Schema.create(driver)
        }

        return driver
    }
}