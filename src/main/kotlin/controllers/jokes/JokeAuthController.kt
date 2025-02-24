package ru.kotleteri.controllers.jokes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.kotleteri.controllers.AbstractAuthController
import ru.kotleteri.data.models.inout.ErrorResponse
import ru.kotleteri.data.models.inout.jokes.GetJokeResponseModel
import ru.kotleteri.integrations.jokesapi.JokeApi

class JokeAuthController(call: ApplicationCall): AbstractAuthController(call) {
    suspend fun getJoke(){
        val joke = JokeApi.getJoke()
        if (joke == null){
            call.respond(HttpStatusCode.NotFound, ErrorResponse("Joke not found"))
            return
        }

        call.respond(HttpStatusCode.OK, GetJokeResponseModel(
            email,
            joke.joke
        ))
    }
}