package ru.kotleteri.unit

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.kotleteri.data.enums.ValidateResult
import ru.kotleteri.utils.Validate
import ru.kotleteri.utils.Validateable

class ValidationTest {
    data class ClassToBeTested(
        val string: String,
        val number: Int,
        val child: ChildClass
    ) : Validateable {
        data class ChildClass(
            val string: String
        ) : Validateable {
            override fun performValidation() {
                Validate.string(this::string, 1..32)
            }
        }

        override fun performValidation() {
            Validate.string(this::string, 1..32)
            Validate.number(this::number, 1..100)
            Validate.field(this::child)
        }
    }


    @Test
    fun `validate correct data`() {
        val data = ClassToBeTested("string", 1, ClassToBeTested.ChildClass("string"))
        val result = data.validate()
        Assertions.assertNull(result.first)
        Assertions.assertEquals(ValidateResult.Valid, result.second)
    }

    @Test
    fun `validate incorrect string`() {
        val data = ClassToBeTested("", 1, ClassToBeTested.ChildClass("string"))
        val result = data.validate()
        Assertions.assertEquals("string", result.first)
        Assertions.assertEquals(ValidateResult.InvalidLength, result.second)
    }

    @Test
    fun `validate incorrect number`() {
        val data = ClassToBeTested("string", 101, ClassToBeTested.ChildClass("string"))
        val result = data.validate()
        Assertions.assertEquals("number", result.first)
        Assertions.assertEquals(ValidateResult.OutOfBounds, result.second)
    }

    @Test
    fun `validate incorrect child string`() {
        val data = ClassToBeTested("string", 1, ClassToBeTested.ChildClass(""))
        val result = data.validate()
        Assertions.assertEquals("child.string", result.first)
        Assertions.assertEquals(ValidateResult.InvalidLength, result.second)
    }

}