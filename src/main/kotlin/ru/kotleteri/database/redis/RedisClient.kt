package ru.kotleteri.database.redis

import io.github.crackthecodeabhi.kreds.args.SetOption

interface RedisClient {
    fun initialize()
    suspend fun set(key: String, value: String, setOption: SetOption)
    suspend fun get(key: String): String?
}