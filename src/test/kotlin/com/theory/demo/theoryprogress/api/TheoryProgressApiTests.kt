package com.theory.demo.theoryprogress.api

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.json.JsonCompareMode
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class TheoryProgressApiTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @ParameterizedTest(name = "student {0} ({1}): basic={2}, special={3}, completed={4}")
    @CsvSource(
        "2, Julian, 0, 0, false",
        "1, Tom, 8, 1, false",
        "3, Oussama, 12, 2, true",
    )
    fun `given a known student when progress is requested then the API contract is returned`(
        studentId: String,
        studentName: String,
        basicTopicsAttended: Int,
        specialTopicsAttended: Int,
        completed: Boolean,
    ) {
        mockMvc.get("/api/students/{id}/theory-progress", studentId) {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content {
                contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                json(
                    """
                    {
                      "studentId": "$studentId",
                      "studentName": "$studentName",
                      "licenseClass": "B",
                      "basicTopics": { "attended": $basicTopicsAttended, "required": 12 },
                      "specialTopics": { "attended": $specialTopicsAttended, "required": 2 },
                      "completed": $completed
                    }
                    """.trimIndent(),
                    JsonCompareMode.STRICT,
                )
            }
        }
    }

    @Test
    fun `given an unknown student when progress is requested then a short 404 error is returned`() {
        mockMvc.get("/api/students/999/theory-progress") {
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNotFound() }
            content {
                contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                json("""{"message":"Student '999' not found"}""", JsonCompareMode.STRICT)
            }
        }
    }
}
