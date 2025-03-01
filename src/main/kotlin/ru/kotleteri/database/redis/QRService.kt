package ru.kotleteri.database.redis

import io.github.crackthecodeabhi.kreds.args.SetOption
import ru.kotleteri.database.redis.RedisInit.redis
import java.util.*

object QRService {
    private const val prefix = "qr:"
    suspend fun generateCode(payload: String): UUID {
        val id = UUID.randomUUID()
        redis.set(prefix + id.toString(), payload, setOption = SetOption.Builder().exSeconds((60*5).toULong()).build())
        return id
    }
    suspend fun getCode(id: UUID): String? {
        return redis.get(prefix + id.toString())
    }
}