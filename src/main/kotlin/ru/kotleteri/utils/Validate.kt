package ru.kotleteri.utils

import ru.kotleteri.data.enums.ValidateResult
import kotlin.reflect.KProperty0

object Validate {
    fun string(
        field: KProperty0<String?>,
        length: IntRange? = null,
        pattern: String? = null,
        ignoreNull: Boolean = false
    ) {
        val fieldValue = field.get()
        if (fieldValue == null) {
            if (ignoreNull) return
            throw ValidationException(field.name, ValidateResult.Null)
        }

        if (length != null && fieldValue.length !in length) {
            throw ValidationException(field.name, ValidateResult.InvalidLength)
        }

        if (pattern != null && !Regex(pattern).matches(fieldValue)) {
            throw ValidationException(field.name, ValidateResult.NotMatchesPattern)
        }
    }

    fun number(
        number: KProperty0<Int?>,
        bounds: IntRange? = null,
        ignoreNull: Boolean = false
    ) {
        val fieldValue = number.get()
        if (fieldValue == null) {
            if (ignoreNull) return
            throw ValidationException(number.name, ValidateResult.Null)
        }

        if (bounds != null && fieldValue !in bounds) {
            throw ValidationException(number.name, ValidateResult.OutOfBounds)
        }

    }

    fun field(field: KProperty0<Validateable?>, ignoreNull: Boolean = false) {
        val fieldValue = field.get()
        if (fieldValue == null) {
            if (ignoreNull) return
            throw ValidationException(field.name, ValidateResult.Null)
        }
        kotlin.runCatching {
            fieldValue.performValidation()
        }.onFailure {
            if (it is ValidationException) {
                throw ValidationException("${field.name}.${it.field}", it.result)
            }
            throw it
        }
    }

    inline fun <T> custom(
        field: KProperty0<T>,
        crossinline validation: (T) -> Boolean
    ) {
        val result = validation(field.get())
        if (!result) {
            throw ValidationException(field.name, ValidateResult.Invalid)
        }
    }
}


interface Validateable {
    fun performValidation()
    fun validate(): Pair<String?, ValidateResult> = try {
        performValidation()
        Pair(null, ValidateResult.Valid)
    } catch (it: ValidationException) {
        Pair(it.field, it.result)
    }
}

class ValidationException(val field: String, val result: ValidateResult) : Exception("Field $field is $result")