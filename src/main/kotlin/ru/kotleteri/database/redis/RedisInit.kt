package ru.kotleteri.database.redis

import io.github.crackthecodeabhi.kreds.connection.Endpoint
import io.github.crackthecodeabhi.kreds.connection.KredsClient
import io.github.crackthecodeabhi.kreds.connection.newClient
import ru.kotleteri.utils.REDIS_HOST
import ru.kotleteri.utils.REDIS_PORT

object RedisInit {
    lateinit var redis: KredsClient

    fun initialize() {
        redis = newClient(Endpoint(REDIS_HOST, REDIS_PORT))
    }
}