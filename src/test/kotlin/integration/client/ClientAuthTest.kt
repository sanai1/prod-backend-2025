package ru.kotleteri.integration.client

import JsonSerializable
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import jsonString
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.kotleteri.ApplicationTest

class ClientAuthTest : ApplicationTest() {
    @Test
    fun `test client registration`() = testWithApplication { client ->
        val response = client.post("/api/clients/register") {
            contentType(ContentType.Application.Json)
            setBody(
                jsonString(
                    object : JsonSerializable {
                        val firstName = "Kakashi"
                        val lastName = "Hatake"
                        val email = "testclient@test.com"
                        val password = "pA88W0rd%"
                    }
                )
            )
        }
        val token = response.body<Map<String, String>>()["token"]
        Assertions.assertNotNull(token)
    }

    @Test
    fun `test client login`() = testWithApplication { client ->
        client.post("/api/clients/register") {
            contentType(ContentType.Application.Json)
            setBody(
                jsonString(
                    object : JsonSerializable {
                        val firstName = "Kakashi"
                        val lastName = "Hatake"
                        val email = "testclient@test.com"
                        val password = "pA88W0rd%"
                    }
                )
            )
        }
        val response = client.post("/api/clients/login") {
            contentType(ContentType.Application.Json)
            setBody(
                jsonString(
                    object : JsonSerializable {
                        val email = "testclient@test.com"
                        val password = "pA88W0rd%"
                    }
                )
            )
        }
        val token = response.body<Map<String, String>>()["token"]
        Assertions.assertNotNull(token)
    }

    @Test
    fun `test unknown client login`() = testWithApplication { client ->
        val response = client.post("/api/clients/login") {
            contentType(ContentType.Application.Json)
            setBody(
                jsonString(
                    object : JsonSerializable {
                        val email = "asdasd@asd.asd"
                        val password = "asdasdasd"
                    }
                )
            )
        }
        Assertions.assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `test client login with wrong password`() = testWithApplication { client ->
        client.post("/api/clients/register") {
            contentType(ContentType.Application.Json)
            setBody(
                jsonString(
                    object : JsonSerializable {
                        val firstName = "Kakashi"
                        val lastName = "Hatake"
                        val email = "testclient@test.com"
                        val password = "pA88W0rd%"
                    }
                )
            )
        }
        val response = client.post("/api/clients/login") {
            contentType(ContentType.Application.Json)
            setBody(
                jsonString(
                    object : JsonSerializable {
                        val email = "testclient@test.com"
                        val password = "wrongpassword"
                    }
                )
            )
        }
        Assertions.assertEquals(HttpStatusCode.Unauthorized, response.status)
    }
}