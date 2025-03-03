package ru.kotleteri.utils

import ru.kotleteri.data.models.base.ClientModel
import ru.kotleteri.data.models.base.CompanyModel
import ru.kotleteri.database.crud.ClientCRUD
import ru.kotleteri.database.crud.CompanyCRUD
import ru.kotleteri.plugins.generateNewToken
import java.util.*

suspend fun generateSampleTokenForCompany(): String {
    val company = CompanyCRUD.readByEmail("test@test.test") ?: CompanyModel(
        UUID.randomUUID(),
        "Test Company",
        "test@test.test",
        "test"
    ).also { CompanyCRUD.create(it) }
    val token = generateNewToken(company.id, company.email, false)
    return token
}

suspend fun generateSampleTokenForClient(): String {
    val company = ClientCRUD.readByEmail("test@test.test") ?: ClientModel(
        UUID.randomUUID(),
        "Test",
        "Client",
        "test@test.test",
        "test"
    ).also { ClientCRUD.create(it) }
    val token = generateNewToken(company.id, company.email, true)
    return token
}