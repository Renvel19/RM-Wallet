package org.acmevision.project.domain

data class Category(
    val id: String,
    val name: String,
    val iconName: String, // Referencia al icono de UI
    val colorHex: String,
    val type: TransactionType, // ¿Es categoría de gasto o ingreso?
    val parentId: String? = null // Si tiene valor, es una subcategoría
)