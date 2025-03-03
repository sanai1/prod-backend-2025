package ru.kotleteri.integration.company

import JsonSerializable
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import jsonString
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.kotleteri.ApplicationTest

class CompanyOfferTest : ApplicationTest() {
    @Test
    fun `test create offer`() = testWithCompany { client, token ->
        val response = client.post("/api/offers/create") {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(
                jsonString(
                object : JsonSerializable {
                    val title = "Test Offer"
                    val description = "Test Description"
                    val discount = 5.0
                    val type = "DISCOUNT"
                    val start_date = "2025-02-20T00:00:00"
                    val end_date = "2030-10-31T23:59:59"
                }
            ))

        }
        Assertions.assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `test get empty offers`() = testWithCompany { client, token ->
        val response = client.get("/api/offers/company?limit=100&offset=0") {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
        }
        Assertions.assertEquals(HttpStatusCode.OK, response.status)
        Assertions.assertEquals("[]", response.bodyAsText())
    }

    @Test
    fun `test get offers`() = testWithCompany { client, token ->
        client.post("/api/offers/create") {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(
                jsonString(
                object : JsonSerializable {
                    val title = "Test Offer"
                    val description = "Test Description"
                    val discount = 5.0
                    val type = "DISCOUNT"
                    val start_date = "2025-02-20T00:00:00"
                    val end_date = "2030-10-31T23:59:59"
                }
            ))
        }
        val response = client.get("/api/offers/company?limit=100&offset=0") {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
        }
        Assertions.assertEquals(HttpStatusCode.OK, response.status)
        val body = response.body<JsonElement>()
        Assertions.assertEquals(1, body.jsonArray.size)
    }
}