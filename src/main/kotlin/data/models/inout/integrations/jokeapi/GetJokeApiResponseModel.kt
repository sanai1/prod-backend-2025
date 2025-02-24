package ru.kotleteri.data.models.inout.integrations.jokeapi

import kotlinx.serialization.Serializable

@Serializable
data class GetJokeApiResponseModel(
    val category: String,
    val joke: String,
    val safe: Boolean,
    val lang: String
)
