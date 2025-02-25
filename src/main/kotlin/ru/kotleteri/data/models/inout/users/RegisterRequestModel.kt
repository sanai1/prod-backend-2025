package ru.kotleteri.data.models.inout.users

import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt
import ru.kotleteri.data.models.base.UserModel
import ru.kotleteri.utils.EMAIL_REGEX
import ru.kotleteri.utils.PASSWORD_REGEX
import ru.kotleteri.utils.Validate
import ru.kotleteri.utils.Validateable

@Serializable
data class RegisterRequestModel(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
): Validateable {
    override fun performValidation() {
        Validate.string(this::firstName, 1..32)
        Validate.string(this::lastName, 1..32)
        Validate.string(this::email, 2..128, pattern = EMAIL_REGEX)
        Validate.string(this::password, 8..128, pattern = PASSWORD_REGEX)
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
