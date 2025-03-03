package ru.kotleteri.integration.company

import JsonSerializable
import io.ktor.client.request.*
import io.ktor.http.*
import jsonString
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.kotleteri.ApplicationTest

class CompanyAuthTest : ApplicationTest() {
    @Test
    fun `test company registration`() = testWithApplication {
        val response = client.post("/api/companies/register") {
            contentType(ContentType.Application.Json)
            setBody(
                jsonString(
                    object : JsonSerializable {
                        val name = "Test Company"
                        val email = "testcompany@test.com"
                        val password = "pA88W0rd%"
                    }
                ))
        }
        Assertions.assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `test company login`() = testWithApplication {
        client.post("/api/companies/register") {
            contentType(ContentType.Application.Json)
            setBody(
                jsonString(
                    object : JsonSerializable {
                        val name = "Test Company"
                        val email = "testcompany@test.com"
                        val password = "pA88W0rd%"
                    }
                ))
        }

        val response = client.post("/api/companies/login") {
            contentType(ContentType.Application.Json)
            setBody(
                jsonString(
                    object : JsonSerializable {
                        val email = "testcompany@test.com"
                        val password = "pA88W0rd%"
                    }
                ))
        }
        Assertions.assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `test unknown company login`() = testWithApplication {
        val response = client.post("/api/companies/login") {
            contentType(ContentType.Application.Json)
            setBody(
                jsonString(
                    object : JsonSerializable {
                        val email = "dddd@dddd.ddd"
                        val password = "pA88W0rd%"
                    }
                )
            )
        }
        Assertions.assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `test company login with wrong password`() = testWithApplication {
        client.post("/api/companies/register") {
            contentType(ContentType.Application.Json)
            setBody(
                jsonString(
                    object : JsonSerializable {
                        val name = "Test Company"
                        val email = "testcompany@test.com"
                        val password = "pA88W0rd%"
                    }
                ))
        }
        val response = client.post("/api/companies/login") {
            contentType(ContentType.Application.Json)
            setBody(
                jsonString(
                    object : JsonSerializable {
                        val email = "testcompany@test.com"
                        val password = "wrongpassword"
                    }
                )
            )
        }
        Assertions.assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `test duplicate company registration`() = testWithApplication {
        client.post("/api/companies/register") {
            contentType(ContentType.Application.Json)
            setBody(
                jsonString(
                    object : JsonSerializable {
                        val name = "Test Company"
                        val email = "testcompany@test.com"
                        val password = "pA88W0rd%"
                    }
                )
            )
        }
        val response = client.post("/api/companies/register") {
            contentType(ContentType.Application.Json)
            setBody(
                jsonString(
                    object : JsonSerializable {
                        val name = "Test Company"
                        val email = "testcompany@test.com"
                        val password = "pA88W0rd%"
                    }
                )
            )
        }
        Assertions.assertEquals(HttpStatusCode.Conflict, response.status)
    }
}