import kotlinx.serialization.json.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.reflect.full.memberProperties

// Marker interface for anonymous objects
interface JsonSerializable

fun jsonString(obj: Any): String {
    return Json.encodeToString(jsonObject(obj))
}

fun jsonObject(obj: Any): JsonObject {
    return buildJsonObject {
        obj::class.memberProperties.forEach { prop ->
            val key = prop.name
            val value = prop.getter.call(obj)
            putJsonValue(this, key, value)
        }
    }
}

fun JsonObjectBuilder.putJsonValue(builder: JsonObjectBuilder, key: String, value: Any?) {
    builder.put(
        key, when (value) {
            null -> JsonNull
            is String -> JsonPrimitive(value)
            is Number -> JsonPrimitive(value)
            is Boolean -> JsonPrimitive(value)
            is JsonElement -> value
            is JsonSerializable -> jsonObject(value) // Handles nested anonymous objects
            is Map<*, *> -> buildJsonObject { value.forEach { (k, v) -> putJsonValue(this, k.toString(), v) } }
            is List<*> -> buildJsonArray { value.forEach { addJsonValue(it) } }
            else -> throw IllegalArgumentException("Unsupported type: ${value::class.simpleName}")
        })
}

fun JsonArrayBuilder.addJsonValue(value: Any?) {
    add(
        when (value) {
            null -> JsonNull
            is String -> JsonPrimitive(value)
            is Number -> JsonPrimitive(value)
            is Boolean -> JsonPrimitive(value)
            is JsonElement -> value
            is JsonSerializable -> jsonObject(value) // Handles anonymous object lists
            is Map<*, *> -> buildJsonObject { value.forEach { (k, v) -> putJsonValue(this, k.toString(), v) } }
            is List<*> -> buildJsonArray { value.forEach { addJsonValue(it) } }
            else -> throw IllegalArgumentException("Unsupported type: ${value::class.simpleName}")
        })
}

class TestJson {
    @Test
    fun test() {
        val json = jsonObject(
            object : JsonSerializable {
                val name = "John"
                val age = 30
                val isStudent = false
                val scores = listOf(90, 85, 88)
                val address = mapOf("city" to "New York", "zip" to "10001")
                val nestedObject = object : JsonSerializable {
                    val hobby = "Reading"
                    val languages = listOf("English", "Spanish")
                }
            }
        )

        Assertions.assertEquals(
            "{\"address\":{\"city\":\"New York\",\"zip\":\"10001\"},\"age\":30,\"isStudent\":false,\"name\":\"John\",\"nestedObject\":{\"hobby\":\"Reading\",\"languages\":[\"English\",\"Spanish\"]},\"scores\":[90,85,88]}",
            Json.encodeToString(json)
        )
    }
}