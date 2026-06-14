package ua.university.sms.model.dto.enrollment;

import jakarta.validation.constraints.*;

public record EnrollmentRequest(
    @NotNull(message = "Student ID is required") Long studentId,
    @NotNull(message = "Course ID is required") Long courseId,
    @NotBlank(message = "Semester is required") String semester,
    @NotNull(message = "Year is required") @Min(1900) @Max(2100) Integer year
) {}
