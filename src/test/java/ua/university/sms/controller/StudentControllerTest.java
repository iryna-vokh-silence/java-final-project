package ua.university.sms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ua.university.sms.exception.ResourceNotFoundException;
import ua.university.sms.model.dto.student.StudentRequest;
import ua.university.sms.model.dto.student.StudentResponse;
import ua.university.sms.model.entity.StudentStatus;
import ua.university.sms.service.StudentService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    private StudentResponse sampleResponse() {
        return new StudentResponse(1L, "John", "Doe", "john@uni.ua",
                LocalDate.of(2023, 9, 1), StudentStatus.ACTIVE, 1);
    }

    @Test
    void createStudent_returns201() throws Exception {
        StudentRequest req = new StudentRequest("John", "Doe", "john@uni.ua",
                LocalDate.of(2023, 9, 1), StudentStatus.ACTIVE, 1);
        when(studentService.create(any())).thenReturn(sampleResponse());

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void getStudentById_returns200() throws Exception {
        when(studentService.getById(1L)).thenReturn(sampleResponse());

        mockMvc.perform(get("/api/v1/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@uni.ua"));
    }

    @Test
    void getStudentById_notFound_returns404() throws Exception {
        when(studentService.getById(99L)).thenThrow(new ResourceNotFoundException("Student", 99L));

        mockMvc.perform(get("/api/v1/students/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Student not found with id: 99"));
    }

    @Test
    void getAll_withStatusParam_callsFilterByStatus() throws Exception {
        when(studentService.filterByStatus(StudentStatus.ACTIVE)).thenReturn(List.of(sampleResponse()));

        mockMvc.perform(get("/api/v1/students").param("status", "ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("ACTIVE"));
    }

    @Test
    void createStudent_invalidBody_returns400() throws Exception {
        String invalidBody = "{\"firstName\":\"\",\"email\":\"not-an-email\"}";

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}
