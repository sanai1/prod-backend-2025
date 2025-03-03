package ru.kotleteri.integration.company

import io.ktor.client.call.*
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.kotleteri.ApplicationTest

class CompanyProfileTest : ApplicationTest() {
    @Test
    fun `test get profile`() = testWithCompany { client, token ->
        val response = client.get("/api/companies/profile") {
            bearerAuth(token)
        }
        val body = response.body<Map<String, String>>()
        Assertions.assertEquals("Test Company", body["name"])
        Assertions.assertEquals("testcompany@test.com", body["email"])
    }
}