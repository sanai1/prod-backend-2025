package ru.kotleteri.unit

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.kotleteri.utils.toUUIDOrNull
import kotlin.test.assertTrue

class UUIDTest {

    @Test
    fun testNullReadFromString(){
        val mustBeNull = "ahahahahah".toUUIDOrNull()
        assertTrue { mustBeNull == null }
    }

    @Test
    fun testRealReadFromString(){
        val checkUUIDString = "ded613f9-99ba-497b-9ce1-23536783a049"
        val uuid = checkUUIDString.toUUIDOrNull()
        Assertions.assertNotNull(uuid)

        assertTrue { checkUUIDString == uuid.toString() }
    }
}