package ru.kotleteri.controllers.company

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.kotleteri.database.crud.CategoryCRUD

class CategoryController(val call: ApplicationCall) {
    suspend fun getAll() {
        val categories = CategoryCRUD.getAllCategories()

        call.respond(HttpStatusCode.OK, categories.map { it.toResponseModel() })
    }
}