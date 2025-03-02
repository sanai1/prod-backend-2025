package ru.kotleteri.unit

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.kotleteri.data.models.redis.QRDataModel
import ru.kotleteri.database.redis.QRService
import kotlin.test.assertEquals

class QRServiceTest {
    @Test
    fun createAndRead(){
        val qrdata = QRDataModel(
            "00000000-0000-0000-0000-000000000000",
            "00000000-0000-0000-0000-000000000000"
        )

        runBlocking {
            val id = QRService.generateCode(
                qrdata
            )

            val checkData = QRService.getCode(id.toString())

            Assertions.assertNull(checkData)

            assertEquals(qrdata, checkData)
        }


    }
}