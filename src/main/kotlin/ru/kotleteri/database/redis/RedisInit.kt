package ru.kotleteri.database.redis

object RedisInit {
    lateinit var redis: RedisClient

    fun initialize(client: RedisClient) {
        redis = client
        redis.initialize()
    }
}