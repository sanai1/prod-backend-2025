package ru.kotleteri.utils

import java.util.*

fun String.toUUIDOrNull(): UUID? = runCatching { UUID.fromString(this) }.getOrNull()