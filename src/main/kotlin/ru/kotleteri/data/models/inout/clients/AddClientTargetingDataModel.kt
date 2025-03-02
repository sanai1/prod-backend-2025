package ru.kotleteri.data.models.inout.clients

import kotlinx.serialization.Serializable
import ru.kotleteri.utils.Validate
import ru.kotleteri.utils.Validateable

@Serializable
data class AddClientTargetingDataModel(
    val age: Int,
    val gender: String
) : Validateable {
    override fun performValidation() {
        Validate.number(this::age, 1..100)
    }
}
