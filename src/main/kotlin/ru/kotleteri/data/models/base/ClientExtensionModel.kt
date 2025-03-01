package ru.kotleteri.data.models.base

import ru.kotleteri.data.enums.Gender
import java.util.*

data class ClientExtensionModel(
    val clientId: UUID,
    val age: Int,
    val gender: Gender
)
