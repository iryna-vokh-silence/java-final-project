package ua.university.sms.model.dto.student;

import jakarta.validation.constraints.*;
import ua.university.sms.model.entity.StudentStatus;

import java.time.LocalDate;

public record StudentRequest(
    @NotBlank(message = "First name is required") String firstName,
    @NotBlank(message = "Last name is required") String lastName,
    @Email(message = "Invalid email format") @NotBlank(message = "Email is required") String email,
    @NotNull(message = "Enrollment date is required") LocalDate enrollmentDate,
    @NotNull(message = "Status is required") StudentStatus status,
    @NotNull(message = "Year is required") @Min(1) @Max(6) Integer year
) {}
