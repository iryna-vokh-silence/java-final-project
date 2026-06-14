package ua.university.sms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.university.sms.model.dto.enrollment.EnrollmentRequest;
import ua.university.sms.model.dto.enrollment.EnrollmentResponse;
import ua.university.sms.model.dto.enrollment.GpaResponse;
import ua.university.sms.model.dto.enrollment.GradeRequest;
import ua.university.sms.model.entity.Grade;
import ua.university.sms.service.EnrollmentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnrollmentController.class)
class EnrollmentControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean  private EnrollmentService enrollmentService;

    @Test
    void enroll_returns201() throws Exception {
        EnrollmentRequest req = new EnrollmentRequest(1L, 2L, "Fall", 2024);
        EnrollmentResponse resp = new EnrollmentResponse(10L, 1L, "John Doe",
                2L, "Math", "Fall", 2024, Grade.NA, false);
        when(enrollmentService.enroll(any())).thenReturn(resp);

        mockMvc.perform(post("/api/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.paid").value(false))
                .andExpect(jsonPath("$.grade").value("NA"));
    }

    @Test
    void setGrade_returns200() throws Exception {
        GradeRequest gradeReq = new GradeRequest(Grade.A);
        EnrollmentResponse resp = new EnrollmentResponse(10L, 1L, "John Doe",
                2L, "Math", "Fall", 2024, Grade.A, false);
        when(enrollmentService.setGrade(eq(10L), any())).thenReturn(resp);

        mockMvc.perform(put("/api/enrollments/10/grade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gradeReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grade").value("A"));
    }

    @Test
    void markAsPaid_returns200() throws Exception {
        EnrollmentResponse resp = new EnrollmentResponse(10L, 1L, "John Doe",
                2L, "Math", "Fall", 2024, Grade.NA, true);
        when(enrollmentService.markAsPaid(10L)).thenReturn(resp);

        mockMvc.perform(put("/api/enrollments/10/paid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paid").value(true));
    }

    @Test
    void gpaByCourse_returns200WithList() throws Exception {
        when(enrollmentService.getAverageGpaByCourse())
                .thenReturn(List.of(new GpaResponse(2L, "Math", 3.5)));

        mockMvc.perform(get("/api/enrollments/reports/gpa-by-course"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseName").value("Math"))
                .andExpect(jsonPath("$[0].averageGpa").value(3.5));
    }
}
