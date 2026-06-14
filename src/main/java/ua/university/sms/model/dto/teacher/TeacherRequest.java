package ua.university.sms.model.dto.teacher;

import jakarta.validation.constraints.*;

public record TeacherRequest(
    @NotBlank(message = "First name is required") String firstName,
    @NotBlank(message = "Last name is required") String lastName,
    @Email(message = "Invalid email format") @NotBlank(message = "Email is required") String email,
    @NotBlank(message = "Department is required") String department
) {}
