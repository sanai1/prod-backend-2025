package ru.kotleteri.unit

import org.junit.jupiter.api.Test
import ru.kotleteri.utils.StatementPrepare
import kotlin.test.assertEquals


class StatementPrepareTest {

    @Test
    fun testStringBuildStatement() {
        val shouldBe = "'qwerty'"
        val start = "?"

        val buildStatement = StatementPrepare(start)
            .addParam("qwerty")
            .build()

        assertEquals(shouldBe, buildStatement)
    }

    @Test
    fun testStringArrayBuildStatement() {
        val shouldBe = "ARRAY['a','b']"
        val start = "?"

        val buildStatement = StatementPrepare(start)
            .addParam(listOf("a", "b"))
            .build()

        assertEquals(shouldBe, buildStatement)
    }

    @Test
    fun testIntArrayBuildStatement() {
        val shouldBe = "ARRAY[2,3]"
        val start = "?"

        val buildStatement = StatementPrepare(start)
            .addParam(listOf(2, 3))
            .build()

        assertEquals(shouldBe, buildStatement)
    }

    @Test
    fun testFloatArrayBuildStatement() {
        val shouldBe = "ARRAY[2.5,3.4]"
        val start = "?"

        val buildStatement = StatementPrepare(start)
            .addParam(listOf(2.5f, 3.4f))
            .build()

        assertEquals(shouldBe, buildStatement)
    }
}