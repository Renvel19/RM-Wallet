package org.acmevision.project

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform