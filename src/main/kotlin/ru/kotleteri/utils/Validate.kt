package ru.kotleteri.utils

import ru.kotleteri.data.enums.ValidateResult

object Validate {
    fun field(field: String?,
              length: IntRange? = null,
              pattern: String? = null,
              ignoreNull: Boolean = false): ValidateResult {
        if (field == null) {
            if (ignoreNull) return ValidateResult.Valid
            return ValidateResult.Null
        }

        if (length != null && field.length !in length) {
            return ValidateResult.InvalidLength
        }

        if (pattern != null && !Regex(pattern).matches(field)) {
            return ValidateResult.NotMatchesPattern
        }

        return ValidateResult.Valid
    }

    fun number(number: Int?,
               bounds: IntRange? = null,
               ignoreNull: Boolean = false): ValidateResult {
        if (number == null) {
            if (ignoreNull) return ValidateResult.Valid
            return ValidateResult.Null
        }

        if (bounds != null && number !in bounds) {
            return ValidateResult.OutOfBounds
        }

        return ValidateResult.Valid
    }
}