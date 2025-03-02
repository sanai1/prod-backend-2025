package ru.kotleteri.data.models.inout.clients

import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt
import ru.kotleteri.data.models.base.CompanyModel
import ru.kotleteri.utils.EMAIL_REGEX
import ru.kotleteri.utils.PASSWORD_REGEX
import ru.kotleteri.utils.Validate
import ru.kotleteri.utils.Validateable
import java.util.*

@Serializable
data class RegisterCompanyRequestModel(
    val name: String,
    val email: String,
    val password: String
) : Validateable {
    override fun performValidation() {
        Validate.string(this::name, 1..50)
        Validate.string(this::email, 2..128, pattern = EMAIL_REGEX)
        Validate.string(this::password, 8..128, pattern = PASSWORD_REGEX)
    }

    fun toCompanyModel() =
        CompanyModel(
            UUID.randomUUID(),
            name,
            email.lowercase(),
            BCrypt.hashpw(password, BCrypt.gensalt())
        )
}
