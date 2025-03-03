package ru.kotleteri.integration.client

import JsonSerializable
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import jsonString
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.kotleteri.ApplicationTest

class ClientOfferTest : ApplicationTest() {
    @Test
    fun `test get empty offers`() = testWithCompanyAndClient { client, companyToken, token ->
        val response = client.get("/api/offers/client?limit=100&offset=0") {
            bearerAuth(token)
        }
        Assertions.assertEquals("[]", response.bodyAsText())
    }

    @Test
    fun `test get offers`() = testWithCompanyAndClient { client, companyToken, token ->
        client.post("/api/offers/create") {
            contentType(io.ktor.http.ContentType.Application.Json)
            bearerAuth(companyToken)
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
        val response = client.get("/api/offers/client?limit=100&offset=0") {
            bearerAuth(token)
        }
        Assertions.assertNotEquals("[]", response.bodyAsText())
    }

    @Test
    fun `test generate qr`() = testWithCompanyAndClient { client, companyToken, token ->
        client.post("/api/offers/create") {
            contentType(io.ktor.http.ContentType.Application.Json)
            bearerAuth(companyToken)
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
        val response = client.get("/api/offers/client?limit=100&offset=0") {
            bearerAuth(token)
        }
        val body = response.body<JsonElement>()
        val id = body.jsonArray[0].jsonObject["id"]!!.jsonPrimitive.content
        val qrResponse = client.post("/api/offers/client/generateQr?offerId=$id") {
            bearerAuth(token)
        }
        Assertions.assertEquals(HttpStatusCode.OK, qrResponse.status)
        val qrBody = qrResponse.body<JsonElement>()
        val payload = qrBody.jsonObject["payload"]?.jsonPrimitive?.content
        Assertions.assertNotNull(payload)
    }

    @Test
    fun `test scan qr`() = testWithCompanyAndClient { client, companyToken, token ->
        client.post("/api/offers/create") {
            contentType(io.ktor.http.ContentType.Application.Json)
            bearerAuth(companyToken)
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
        val response = client.get("/api/offers/client?limit=100&offset=0") {
            bearerAuth(token)
        }
        val body = response.body<JsonElement>()
        val id = body.jsonArray[0].jsonObject["id"]!!.jsonPrimitive.content
        val qrResponse = client.post("/api/offers/client/generateQr?offerId=$id") {
            bearerAuth(token)
        }
        val qrBody = qrResponse.body<JsonElement>()
        val payload = qrBody.jsonObject["payload"]?.jsonPrimitive?.content
        Assertions.assertNotNull(payload)
        val scanResponse = client.post("/api/offers/company/scanQr") {
            contentType(io.ktor.http.ContentType.Application.Json)
            bearerAuth(companyToken)
            setBody(
                jsonString(
                    object : JsonSerializable {
                        val payload = payload
                        val cost = 100.0
                    }
                ))
        }
        Assertions.assertEquals(HttpStatusCode.OK, scanResponse.status)
    }
}