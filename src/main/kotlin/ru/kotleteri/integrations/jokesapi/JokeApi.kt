package ru.kotleteri.integrations.jokesapi

import io.ktor.client.call.*
import io.ktor.client.request.*
import ru.kotleteri.data.models.inout.integrations.jokeapi.GetJokeApiResponseModel
import ru.kotleteri.utils.client

object JokeApi {
    suspend fun getJoke(): GetJokeApiResponseModel? {
        val response = client.get("https://v2.jokeapi.dev/joke/Any"){
            parameter("type", "single")
        }

        if (response.status.value != 200) return null
        return response.body<GetJokeApiResponseModel>()
    }
}