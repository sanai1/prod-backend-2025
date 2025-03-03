package ru.kotleteri

import JsonSerializable
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import jsonString
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import ru.kotleteri.database.DatabaseInit
import ru.kotleteri.database.redis.RedisInit
import ru.kotleteri.plugins.*
import ru.kotleteri.utils.TestRedis
import ru.kotleteri.utils.connectTestDatabase


open class ApplicationTest {

    @BeforeEach
    fun setup() {
        connectTestDatabase()
        transaction {
            SchemaUtils.drop(*DatabaseInit.tables.toTypedArray())
        }
        DatabaseInit.initialize()
    }

    inline fun testWithApplication(crossinline block: suspend ApplicationTestBuilder.(HttpClient) -> Unit) =
        testApplication {
            application {
                RedisInit.initialize(TestRedis())
                configureDocumentation()
                configureSerialization()
                configureHTTP()
                configureSecurity()
                configureRouting()
                configureStatusPages()
                configureValidation()
                configureRateLimiting()
            }
            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }
            block(client)
        }

    inline fun testWithCompany(crossinline block: suspend ApplicationTestBuilder.(HttpClient, String) -> Unit) =
        testWithApplication { client ->
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
            response.body<Map<String, String>>()["token"]?.let { token ->
                block(client, token)
            } ?: throw Exception("Token is null")
        }

    inline fun testWithCompanyAndClient(crossinline block: suspend ApplicationTestBuilder.(HttpClient, String, String) -> Unit) =
        testWithCompany { client, companyToken ->
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
            response.body<Map<String, String>>()["token"]?.let { token ->
                block(client, companyToken, token)
            } ?: throw Exception("Token is null")
        }

}
