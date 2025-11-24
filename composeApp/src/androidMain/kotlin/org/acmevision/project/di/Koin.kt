package org.acmevision.project.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModule)
    }
}

// Aquí definiremos tus ViewModels y Repositorios más adelante
val appModule = module {
    databaseModule
}