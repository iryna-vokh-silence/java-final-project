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
import ua.university.sms.service.EnrollmentService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnrollmentController.class)
class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EnrollmentService enrollmentService;

    @Test
    void enroll_returns201() throws Exception {
        EnrollmentRequest req = new EnrollmentRequest(1L, 2L);
        EnrollmentResponse resp = new EnrollmentResponse(10L, 1L, "John Doe",
                2L, "Math", LocalDate.now(), null, false);
        when(enrollmentService.enroll(any())).thenReturn(resp);

        mockMvc.perform(post("/api/v1/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.paid").value(false));
    }

    @Test
    void setGrade_returns200() throws Exception {
        GradeRequest gradeReq = new GradeRequest(new BigDecimal("92.0"));
        EnrollmentResponse resp = new EnrollmentResponse(10L, 1L, "John Doe",
                2L, "Math", LocalDate.now(), new BigDecimal("92.0"), false);
        when(enrollmentService.setGrade(eq(10L), any())).thenReturn(resp);

        mockMvc.perform(patch("/api/v1/enrollments/10/grade")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gradeReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grade").value(92.0));
    }

    @Test
    void gpaByCourse_returns200WithList() throws Exception {
        when(enrollmentService.getAverageGpaByCourse())
                .thenReturn(List.of(new GpaResponse(2L, "Math", 88.5)));

        mockMvc.perform(get("/api/v1/enrollments/reports/gpa-by-course"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseTitle").value("Math"))
                .andExpect(jsonPath("$[0].averageGpa").value(88.5));
    }
}
