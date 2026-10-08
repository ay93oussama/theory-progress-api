package com.theory.demo.theoryprogress.service

class StudentNotFoundException(studentId: String) : RuntimeException("Student '$studentId' not found")
