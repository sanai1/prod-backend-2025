package ru.kotleteri.utils

import ru.kotleteri.database.redis.RedisClient

class TestRedis : RedisClient {
    private val storage = mutableMapOf<String, String>()
    override fun initialize() {

    }

    override suspend fun set(key: String, value: String, setOption: io.github.crackthecodeabhi.kreds.args.SetOption) {
        storage[key] = value
    }

    override suspend fun get(key: String): String? {
        return storage[key]
    }
}