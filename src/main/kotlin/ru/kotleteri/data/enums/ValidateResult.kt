package ru.kotleteri.data.enums

import kotlinx.serialization.Serializable

@Serializable
enum class ValidateResult {
    Valid,
    InvalidLength,
    Null,
    NotMatchesPattern,
    OutOfBounds,
    Invalid
}