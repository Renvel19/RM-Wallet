package org.acmevision.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.tudominio.finanzas.di.initKoin
import org.koin.dsl.module

fun main() = application {
    // Inicializamos Koin antes de lanzar la ventana
    initKoin {
        modules(module {
            // Inyectamos la implementación específica de Desktop
            single<DatabaseDriverFactory> { DesktopDatabaseDriverFactory() }
        })
    }

    Window(onCloseRequest = ::exitApplication, title = "RM Wallet") {
        App() // Tu composable principal (definido en commonMain)
    }
}