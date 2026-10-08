package com.theory.demo.theoryprogress.repository

import com.theory.demo.theoryprogress.domain.TheoryProgress
import org.springframework.stereotype.Repository

@Repository
class InMemoryStudentRepository {
    private val progressByStudentId = listOf(
        TheoryProgress(studentId = "1", studentName = "Julian"),
        TheoryProgress(studentId = "2", basicTopicsAttended = 8, specialTopicsAttended = 1, studentName = "Tom"),
        TheoryProgress(studentId = "3", basicTopicsAttended = 12, specialTopicsAttended = 2, studentName = "Oussama"),
    ).associateBy { it.studentId }

    fun findTheoryProgressByStudentId(studentId: String): TheoryProgress? = progressByStudentId[studentId]
}
