package com.theory.demo.theoryprogress.api

import com.theory.demo.theoryprogress.service.TheoryProgressService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/students")
class TheoryProgressController(private val theoryProgressService: TheoryProgressService) {
    @GetMapping("/{id}/theory-progress")
    fun getTheoryProgress(@PathVariable("id") studentId: String): TheoryProgressResponse =
        TheoryProgressResponse.from(theoryProgressService.getTheoryProgress(studentId))
}
