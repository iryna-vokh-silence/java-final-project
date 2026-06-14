package ua.university.sms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.university.sms.model.dto.enrollment.EnrollmentRequest;
import ua.university.sms.model.dto.enrollment.EnrollmentResponse;
import ua.university.sms.model.dto.enrollment.GpaResponse;
import ua.university.sms.model.dto.enrollment.GradeRequest;
import ua.university.sms.service.EnrollmentService;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
@Tag(name = "Enrollments", description = "Enrollment management API")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Enroll a student into a course")
    public EnrollmentResponse enroll(@Valid @RequestBody EnrollmentRequest request) {
        return enrollmentService.enroll(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get enrollment by ID")
    public EnrollmentResponse getById(@PathVariable Long id) {
        return enrollmentService.getById(id);
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get all enrollments for a student")
    public List<EnrollmentResponse> getByStudent(@PathVariable Long studentId) {
        return enrollmentService.getByStudent(studentId);
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "Get all enrollments for a course")
    public List<EnrollmentResponse> getByCourse(@PathVariable Long courseId) {
        return enrollmentService.getByCourse(courseId);
    }

    @PutMapping("/{id}/grade")
    @Operation(summary = "Set grade for an enrollment")
    public EnrollmentResponse setGrade(@PathVariable Long id, @Valid @RequestBody GradeRequest request) {
        return enrollmentService.setGrade(id, request);
    }

    @PutMapping("/{id}/paid")
    @Operation(summary = "Mark enrollment as paid")
    public EnrollmentResponse markAsPaid(@PathVariable Long id) {
        return enrollmentService.markAsPaid(id);
    }

    @GetMapping("/unpaid")
    @Operation(summary = "Get all unpaid enrollments")
    public List<EnrollmentResponse> getUnpaid() {
        return enrollmentService.getUnpaidEnrollments();
    }

    @GetMapping("/unpaid/student/{studentId}")
    @Operation(summary = "Get unpaid enrollments for a specific student")
    public List<EnrollmentResponse> getUnpaidByStudent(@PathVariable Long studentId) {
        return enrollmentService.getUnpaidByStudent(studentId);
    }

    @GetMapping("/reports/gpa-by-course")
    @Operation(summary = "Average GPA per course")
    public List<GpaResponse> gpaByCourse() {
        return enrollmentService.getAverageGpaByCourse();
    }
}
