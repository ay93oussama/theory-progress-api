package com.theory.demo.theoryprogress.service

import com.theory.demo.theoryprogress.domain.TheoryProgress
import com.theory.demo.theoryprogress.repository.InMemoryStudentRepository
import org.springframework.stereotype.Service

@Service
class TheoryProgressService(private val studentRepository: InMemoryStudentRepository) {
    fun getTheoryProgress(studentId: String): TheoryProgress =
        studentRepository.findTheoryProgressByStudentId(studentId)
            ?: throw StudentNotFoundException(studentId)
}
