package com.theory.demo.theoryprogress.domain

data class TheoryProgress(
    val studentId: String,
    val basicTopicsAttended: Int = 0,
    val specialTopicsAttended: Int = 0,
    val studentName: String,
) {
    init {
        require(basicTopicsAttended >= 0) { "Basic topics attended must not be negative" }
        require(specialTopicsAttended >= 0) { "Special topics attended must not be negative" }
    }

    val completed: Boolean
        get() = basicTopicsAttended >= BASIC_TOPICS_REQUIRED &&
            specialTopicsAttended >= SPECIAL_TOPICS_REQUIRED

    companion object {
        const val LICENSE_CLASS = "B"
        const val BASIC_TOPICS_REQUIRED = 12
        const val SPECIAL_TOPICS_REQUIRED = 2
    }
}
