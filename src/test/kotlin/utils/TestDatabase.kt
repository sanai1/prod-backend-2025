package ru.kotleteri.utils

import org.jetbrains.exposed.sql.Database

fun connectTestDatabase() {
    Database.connect(
        "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
        "org.h2.Driver"
    )
}