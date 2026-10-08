package com.theory.demo.theoryprogress.api

import com.theory.demo.theoryprogress.service.StudentNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(assignableTypes = [TheoryProgressController::class])
class TheoryProgressExceptionHandler {
    @ExceptionHandler(StudentNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleStudentNotFound(exception: StudentNotFoundException): ApiErrorResponse =
        ApiErrorResponse(message = requireNotNull(exception.message))
}

data class ApiErrorResponse(val message: String)
