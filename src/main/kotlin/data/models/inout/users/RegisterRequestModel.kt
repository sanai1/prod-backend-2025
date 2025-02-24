package ru.kotleteri.data.models.inout.users

import org.mindrot.jbcrypt.BCrypt
import ru.kotleteri.data.enums.ValidateResult
import ru.kotleteri.data.models.base.UserModel
import ru.kotleteri.utils.EMAIL_REGEX
import ru.kotleteri.utils.PASSWORD_REGEX
import ru.kotleteri.utils.Validate

data class RegisterRequestModel(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
) {
    fun validate(): Pair<String?, ValidateResult> { // field name and validation result for this field
        val validations = mapOf(
            "firstName" to Validate.field(firstName, 1..32),
            "lastName" to Validate.field(lastName, 1..32),
            "email" to Validate.field(email, 2..128, pattern = EMAIL_REGEX),
            "password" to Validate.field(password, 8..128, pattern = PASSWORD_REGEX)
        )

        val validSet = validations.values.toSet()

        if (validSet.size == 1 && validSet.contains(ValidateResult.Valid)) { // making sure that only Valid in set
            return Pair(null, ValidateResult.Valid)
        }

        val invalids = validations.entries.filter {
            it.value != ValidateResult.Valid
        }

        return invalids[0].toPair()
    }

    fun toUserModel() =
        UserModel(
            0,
            firstName,
            lastName,
            email,
            BCrypt.hashpw(password, BCrypt.gensalt())
        )
}
