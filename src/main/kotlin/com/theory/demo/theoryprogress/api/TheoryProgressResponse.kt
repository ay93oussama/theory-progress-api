package com.theory.demo.theoryprogress.api

import com.theory.demo.theoryprogress.domain.TheoryProgress

data class TheoryProgressResponse(
    val studentId: String,
    val licenseClass: String,
    val basicTopics: TopicProgressResponse,
    val specialTopics: TopicProgressResponse,
    val completed: Boolean,
) {
    companion object {
        fun from(progress: TheoryProgress): TheoryProgressResponse = TheoryProgressResponse(
            studentId = progress.studentId,
            licenseClass = TheoryProgress.LICENSE_CLASS,
            basicTopics = TopicProgressResponse(attended = progress.basicTopicsAttended, required = TheoryProgress.BASIC_TOPICS_REQUIRED),
            specialTopics = TopicProgressResponse(
            attended = progress.specialTopicsAttended,
            required = TheoryProgress.SPECIAL_TOPICS_REQUIRED,
            ),
            completed = progress.completed,
        )
    }
}

data class TopicProgressResponse(val attended: Int, val required: Int)
