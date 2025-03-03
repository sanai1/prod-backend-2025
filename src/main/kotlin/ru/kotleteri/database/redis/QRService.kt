package ru.kotleteri.database.redis

import io.github.crackthecodeabhi.kreds.args.SetOption
import kotlinx.serialization.json.Json
import ru.kotleteri.data.models.redis.QRDataModel
import ru.kotleteri.database.redis.RedisInit.redis
import java.util.*

object QRService {
    private const val PREFIX = "qr:"
    private suspend fun savePayload(payload: String): UUID {
        val id = UUID.randomUUID()
        redis.set(
            PREFIX + id.toString(),
            payload,
            setOption = SetOption.Builder().exSeconds((60 * 5).toULong()).build()
        )
        return id
    }

    private suspend fun getPayload(id: String): String? {
        return redis.get(PREFIX + id)
    }


    suspend fun generateCode(data: QRDataModel): UUID =
        savePayload(Json.encodeToString(data))

    suspend fun getCode(id: String): QRDataModel? {
        return Json.decodeFromString(getPayload(id) ?: return null)
    }
}