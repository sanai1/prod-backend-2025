package ru.kotleteri.database.redis

import io.github.crackthecodeabhi.kreds.args.SetOption
import io.github.crackthecodeabhi.kreds.connection.Endpoint
import io.github.crackthecodeabhi.kreds.connection.KredsClient
import io.github.crackthecodeabhi.kreds.connection.newClient
import ru.kotleteri.utils.REDIS_HOST
import ru.kotleteri.utils.REDIS_PORT

class RedisClientImpl : RedisClient {
    private lateinit var redis: KredsClient
    override fun initialize() {
        redis = newClient(Endpoint(REDIS_HOST, REDIS_PORT))
    }

    override suspend fun set(key: String, value: String, setOption: SetOption) {
        redis.set(key, value, setOption)
    }

    override suspend fun get(key: String): String? =
        redis.get(key)
}