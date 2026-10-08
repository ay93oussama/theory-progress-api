package com.theory.demo.theoryprogress.domain

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

class TheoryProgressTests {
    @ParameterizedTest(name = "basic={0}, special={1} -> completed={2}")
    @CsvSource(
        "8, 1, false",
        "12, 0, false",
        "0, 2, false",
        "11, 2, false",
        "12, 1, false",
        "12, 2, true",
        "13, 2, true",
        "12, 3, true",
        "13, 3, true",
    )
    fun `progress is completed only when both requirements are reached`(
        basicTopicsAttended: Int,
        specialTopicsAttended: Int,
        expectedCompleted: Boolean,
    ) {
        val progress = TheoryProgress(
            studentId = "1",
            studentName = "Tom",
            basicTopicsAttended = basicTopicsAttended,
            specialTopicsAttended = specialTopicsAttended,
        )

        assertEquals(expectedCompleted, progress.completed)
    }

    @Test
    fun `a student without lessons has zero attendance and incomplete progress`() {
        val progress = TheoryProgress(studentId = "2", studentName = "Julian")

        assertEquals(0, progress.basicTopicsAttended)
        assertEquals(0, progress.specialTopicsAttended)
        assertFalse(progress.completed)
    }

    @ParameterizedTest(name = "negative attendance is rejected: basic={0}, special={1}")
    @CsvSource("-1, 0", "0, -1")
    fun `attendance cannot be negative`(basicTopicsAttended: Int, specialTopicsAttended: Int) {
        assertFailsWith<IllegalArgumentException> {
            TheoryProgress(
                studentId = "1",
                studentName = "Tom",
                basicTopicsAttended = basicTopicsAttended,
                specialTopicsAttended = specialTopicsAttended,
            )
        }
    }
}
