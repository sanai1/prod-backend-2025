package ru.kotleteri.plugins

import io.bkbn.kompendium.core.metadata.GetInfo
import io.bkbn.kompendium.core.metadata.PostInfo
import io.bkbn.kompendium.core.metadata.PutInfo
import io.bkbn.kompendium.core.plugin.NotarizedRoute
import io.bkbn.kompendium.json.schema.definition.TypeDefinition
import io.bkbn.kompendium.oas.payload.Parameter
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kotleteri.controllers.client.AuthClientController
import ru.kotleteri.controllers.client.ClientController
import ru.kotleteri.controllers.company.AuthCompanyController
import ru.kotleteri.controllers.company.CompanyController
import ru.kotleteri.controllers.company.PublicPictureController
import ru.kotleteri.controllers.offer.OfferClientController
import ru.kotleteri.controllers.offer.OfferCompanyController
import ru.kotleteri.controllers.offer.OfferController
import ru.kotleteri.controllers.statistics.StatisticsController
import ru.kotleteri.data.models.inout.ErrorResponse
import ru.kotleteri.data.models.inout.clients.*
import ru.kotleteri.data.models.inout.companies.GetCompanyProfileResponseModel
import ru.kotleteri.data.models.inout.offers.*
import ru.kotleteri.data.models.inout.statistics.StatisticsDateResponseModel
import ru.kotleteri.data.models.inout.statistics.StatisticsHourResponseModel
import ru.kotleteri.data.models.inout.statistics.StatisticsMonthResponseModel
import ru.kotleteri.utils.generateSampleTokenForClient
import ru.kotleteri.utils.generateSampleTokenForCompany

fun Application.configureRouting() =
    routing {
        swaggerUI(
            "/docs",
            "/openapi.json",
            "client" to ::generateSampleTokenForClient,
            "company" to ::generateSampleTokenForCompany
        )
        route("/api") {
            route("/ping") {
                install(NotarizedRoute()) {
                    get = GetInfo.builder {
                        tags("ping")
                        response {
                            description("Ничего не возвращает")
                            responseCode(HttpStatusCode.OK)
                            responseType<Unit>()
                        }
                        summary("Эндпоинт пинга")
                        description("")
                    }
                    get {
                        call.respond(HttpStatusCode.OK)
                    }
                }
            }

            route("/clients") {
                route("/register") {
                    install(NotarizedRoute()) {
                        post = PostInfo.builder {
                            tags("clients")
                            summary("Регистрация клиента")
                            description("Эндпоинт регистрации клиента")
                            request {
                                requestType<RegisterRequestModel>()
                                description("Данные для регистрации")
                            }
                            response {
                                responseCode(HttpStatusCode.OK)
                                responseType<LoginResponseModel>()
                                description("JWT-токен для аутентификации")
                            }
                            canRespond {
                                responseCode(HttpStatusCode.Conflict)
                                responseType<ErrorResponse>()
                                description("Пользователь с таким email уже существует")
                            }

                        }
                        post {
                            ClientController(call).register()
                        }
                    }
                }
                route("/login") {
                    install(NotarizedRoute()) {
                        post = PostInfo.builder {
                            tags("clients")
                            summary("Аутентификация клиента")
                            description("Эндпоинт аутентификации клиента")
                            request {
                                requestType<LoginRequestModel>()
                                description("Данные для аутентификации")
                            }
                            response {
                                responseCode(HttpStatusCode.OK)
                                responseType<LoginResponseModel>()
                                description("JWT-токен для аутентификации")
                            }
                            canRespond {
                                responseCode(HttpStatusCode.Unauthorized)
                                responseType<ErrorResponse>()
                                description("Пользователь с таким email и паролем не существует")
                            }
                        }
                        post {
                            ClientController(call).login()
                        }
                    }
                }

                authenticate("client") {
                    route("/profile") {
                        install(NotarizedRoute()) {
                            get = GetInfo.builder {
                                tags("clients")

                                summary("Получение профиля клиента")
                                description("Эндпоинт для получения профиля клиента")
                                response {
                                    responseCode(HttpStatusCode.OK)
                                    responseType<GetClientProfileResponseModel>()
                                    description("Профиль клиента")
                                }
                                canRespond {
                                    responseCode(HttpStatusCode.NotFound)
                                    responseType<ErrorResponse>()
                                    description("Пользователь не найден")
                                }

                            }
                            get {
                                AuthClientController(call).getProfile()
                            }

                            put = PutInfo.builder {
                                tags("clients")
                                summary("Изменение таргетинга клиента")
                                description("Эндпоинт для изменения таргетинга клиента")
                                request {
                                    requestType<AddClientTargetingDataModel>()
                                    description("Данные для изменения таргетинга")
                                }
                                response {
                                    responseCode(HttpStatusCode.OK)
                                    responseType<Unit>()
                                    description("Успешное изменение таргетинга")
                                }
                            }
                            put {
                                AuthClientController(call).editTarget()
                            }
                        }
                    }
                }
            }

            route("/companies") {
                route("/register") {
                    install(NotarizedRoute()) {
                        post = PostInfo.builder {
                            tags("companies")
                            summary("Регистрация компании")
                            description("Эндпоинт регистрации компании")
                            request {
                                requestType<RegisterCompanyRequestModel>()
                                description("Данные для регистрации")
                            }
                            response {
                                responseCode(HttpStatusCode.OK)
                                responseType<LoginResponseModel>()
                                description("JWT-токен для аутентификации")
                            }
                            canRespond {
                                responseCode(HttpStatusCode.Conflict)
                                responseType<ErrorResponse>()
                                description("Компания с таким email уже существует")
                            }
                        }
                        post {
                            CompanyController(call).register()
                        }
                    }
                }
                route("/login") {
                    install(NotarizedRoute()) {
                        post = PostInfo.builder {
                            tags("companies")
                            summary("Аутентификация компании")
                            description("Эндпоинт аутентификации компании")
                            request {
                                requestType<LoginRequestModel>()
                                description("Данные для аутентификации")
                            }
                            response {
                                responseCode(HttpStatusCode.OK)
                                responseType<LoginResponseModel>()
                                description("JWT-токен для аутентификации")
                            }
                            canRespond {
                                responseCode(HttpStatusCode.Unauthorized)
                                responseType<ErrorResponse>()
                                description("Компания с таким email и паролем не существует")
                            }
                        }
                        post {
                            CompanyController(call).login()
                        }
                    }
                }

                authenticate("company") {

                    route("/profile") {
                        install(NotarizedRoute()) {
                            get = GetInfo.builder {
                                tags("companies")
                                summary("Получение профиля компании")
                                description("Эндпоинт для получения профиля компании")
                                response {
                                    responseCode(HttpStatusCode.OK)
                                    responseType<GetCompanyProfileResponseModel>()
                                    description("Профиль компании")
                                }
                                canRespond {
                                    responseCode(HttpStatusCode.NotFound)
                                    responseType<ErrorResponse>()
                                    description("Компания не найдена")
                                }
                            }
                            get {
                                AuthCompanyController(call).getProfile()
                            }
                        }
                    }
                    route("/stats") {
                        route("/byDate") {
                            install(NotarizedRoute()) {
                                get = GetInfo.builder {
                                    tags("companies")
                                    summary("Статистика по дате")
                                    description("Эндпоинт для получения статистики по дате")
                                    response {
                                        responseCode(HttpStatusCode.OK)
                                        responseType<List<StatisticsDateResponseModel>>()
                                        description("Статистика по дате")
                                    }
                                }
                                get {
                                    StatisticsController(call).getStatsByDateCompany()
                                }
                            }
                        }

                        route("/byHour") {
                            install(NotarizedRoute()) {
                                get = GetInfo.builder {
                                    tags("companies")
                                    summary("Статистика по часам")
                                    description("Эндпоинт для получения статистики по часам")
                                    response {
                                        responseCode(HttpStatusCode.OK)
                                        responseType<List<StatisticsHourResponseModel>>()
                                        description("Статистика по часам")
                                    }
                                }
                                get {
                                    StatisticsController(call).getStatsByHourCompany()
                                }
                            }
                        }

                        route("/byMonth") {
                            install(NotarizedRoute()) {
                                get = GetInfo.builder {
                                    tags("companies")
                                    summary("Статистика по месяцам")
                                    description("Эндпоинт для получения статистики по месяцам")
                                    response {
                                        responseCode(HttpStatusCode.OK)
                                        responseType<List<StatisticsMonthResponseModel>>()
                                        description("Статистика по месяцам")
                                    }
                                    canRespond {
                                        responseCode(HttpStatusCode.BadRequest)
                                        responseType<ErrorResponse>()
                                        description("Ошибка получения статистики")
                                    }
                                }
                                get {
                                    StatisticsController(call).getStatsByMonthCompany()
                                }
                            }

                        }
                    }
                }
            }

            route("/offers") {
                authenticate("company") {
                    route("/create") {
                        install(NotarizedRoute()) {
                            post = PostInfo.builder {
                                tags("offers")
                                summary("Создание оффера")
                                description("Эндпоинт для создания оффера")
                                request {
                                    requestType<CreateRequestModel>()
                                    description("Данные для создания оффера")
                                }
                                response {
                                    responseCode(HttpStatusCode.Created)
                                    responseType<Unit>()
                                    description("Успешное создание оффера")
                                }
                                canRespond {
                                    responseCode(HttpStatusCode.NotFound)
                                    responseType<ErrorResponse>()
                                    description("Компания не найдена")
                                }
                            }
                            post {
                                OfferController(call).create()
                            }
                        }
                    }
                    route("/company") {
                        install(NotarizedRoute()) {
                            get = GetInfo.builder {
                                tags("offers")
                                summary("Получение списка офферов")
                                description("Эндпоинт для получения списка офферов")
                                parameters = listOf(
                                    Parameter(
                                        name = "offset",
                                        `in` = Parameter.Location.query,
                                        schema = TypeDefinition.INT
                                    ),
                                    Parameter(
                                        name = "limit",
                                        `in` = Parameter.Location.query,
                                        schema = TypeDefinition.INT
                                    )
                                )
                                response {
                                    responseCode(HttpStatusCode.OK)
                                    responseType<List<GetOfferResponseModel>>()
                                    description("Список офферов")
                                }
                                canRespond {
                                    responseCode(HttpStatusCode.BadRequest)
                                    responseType<ErrorResponse>()
                                    description("Неправильный формат offset или limit")
                                }
                            }
                            get {
                                OfferCompanyController(call).getAllOffersByCompany()
                            }
                        }

                        route("/scanQr") {
                            install(NotarizedRoute()) {
                                post = PostInfo.builder {
                                    tags("offers")
                                    summary("Сканирование QR-кода")
                                    description("Эндпоинт для сканирования QR-кода")
                                    request {
                                        requestType<GetOfferByQrRequestModel>()
                                        description("Данные для сканирования QR-кода")
                                    }
                                    response {
                                        responseCode(HttpStatusCode.OK)
                                        responseType<GetOfferByQrResponseModel>()
                                        description("Успешное сканирование QR-кода")
                                    }
                                    canRespond {
                                        responseCode(HttpStatusCode.NotFound)
                                        responseType<ErrorResponse>()
                                        description("QR-код не найден")
                                    }
                                    canRespond {
                                        responseCode(HttpStatusCode.BadRequest)
                                        responseType<ErrorResponse>()
                                        description("Оффер или клиент не найден")
                                    }
                                    canRespond {
                                        responseCode(HttpStatusCode.Forbidden)
                                        responseType<ErrorResponse>()
                                        description("Оффер принадлежит другой компании")
                                    }
                                }
                                post {
                                    OfferCompanyController(call).receiveOfferQr()
                                }
                            }
                        }
                    }
                }
                authenticate("client") {
                    route("/client") {
                        install(NotarizedRoute()) {
                            get = GetInfo.builder {
                                tags("offers")
                                summary("Получение списка офферов")
                                description("Эндпоинт для получения списка офферов")
                                parameters = listOf(
                                    Parameter(
                                        name = "offset",
                                        `in` = Parameter.Location.query,
                                        schema = TypeDefinition.INT
                                    ),
                                    Parameter(
                                        name = "limit",
                                        `in` = Parameter.Location.query,
                                        schema = TypeDefinition.INT
                                    )
                                )
                                response {
                                    responseCode(HttpStatusCode.OK)
                                    responseType<List<GetOfferResponseModel>>()
                                    description("Список офферов")
                                }
                                canRespond {
                                    responseCode(HttpStatusCode.BadRequest)
                                    responseType<ErrorResponse>()
                                    description("Неправильный формат offset или limit")
                                }
                            }
                            get {
                                OfferClientController(call).getOffersList()
                            }
                        }
                        route("/generateQr") {
                            install(NotarizedRoute()) {
                                post = PostInfo.builder {
                                    tags("offers")
                                    summary("Генерация QR-кода")
                                    description("Эндпоинт для генерации QR-кода")
                                    parameters = listOf(
                                        Parameter(
                                            name = "offerId",
                                            `in` = Parameter.Location.query,
                                            schema = TypeDefinition.STRING
                                        )
                                    )
                                    response {
                                        responseCode(HttpStatusCode.OK)
                                        responseType<GenerateQRPayloadResponseModel>()
                                        description("QR-код")
                                    }
                                    canRespond {
                                        responseCode(HttpStatusCode.NotFound)
                                        responseType<ErrorResponse>()
                                        description("Оффер не найден")
                                    }
                                    canRespond {
                                        responseCode(HttpStatusCode.BadRequest)
                                        responseType<ErrorResponse>()
                                        description("Неправильный формат offerId")
                                    }
                                }

                                post {
                                    OfferClientController(call).generateQrPayload()
                                }
                            }
                        }
                    }
                }


            }

            route("/company") {
                authenticate("company") {
                    route("/image") {
                        install(NotarizedRoute()) {
                            post = PostInfo.builder {
                                tags("images")
                                summary("Загрузка изображения")
                                description("Эндпоинт для загрузки изображения")
                                request {
                                    mediaTypes("image/*")
                                    requestType<ByteArray>()
                                    description("Изображение")
                                }
                                response {
                                    responseCode(HttpStatusCode.OK)
                                    responseType<Unit>()
                                    description("Успешная загрузка изображения")
                                }
                                canRespond {
                                    responseCode(HttpStatusCode.BadRequest)
                                    responseType<ErrorResponse>()
                                    description("Ошибка загрузки изображения")
                                }
                            }
                            post {
                                AuthCompanyController(call).setPicture()
                            }
                        }
                    }
                }
                    route("/{companyId}") {
                        route("/image") {
                            install(NotarizedRoute()) {
                                get = GetInfo.builder {
                                    tags("images")
                                    summary("Получение изображения")
                                    description("Эндпоинт для получения изображения")
                                    parameters = listOf(
                                        Parameter(
                                            name = "companyId",
                                            `in` = Parameter.Location.path,
                                            schema = TypeDefinition.STRING
                                        )
                                    )
                                    response {
                                        mediaTypes("image/*")
                                        responseType<ByteArray>()
                                        responseCode(HttpStatusCode.OK)
                                        description("Изображение")
                                    }
                                    canRespond {
                                        responseCode(HttpStatusCode.BadRequest)
                                        responseType<ErrorResponse>()
                                        description("Неправильный формат companyId")
                                    }
                                    canRespond {
                                        responseCode(HttpStatusCode.NotFound)
                                        responseType<ErrorResponse>()
                                        description("Изображение не найдено")
                                    }
                                }
                                get {
                                    PublicPictureController(call).getPicture()
                                }

                        }
                    }
                }
            }


        }
    }

