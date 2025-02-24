package ru.kotleteri.data.models.inout.jokes

import kotlinx.serialization.Serializable

@Serializable
data class GetJokeResponseModel (
    val requester: String,
    val joke: String
)