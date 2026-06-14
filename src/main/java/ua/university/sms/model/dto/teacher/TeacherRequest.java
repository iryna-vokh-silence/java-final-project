package ua.university.sms.model.dto.teacher;

import jakarta.validation.constraints.*;
import ua.university.sms.model.entity.TeacherPosition;

import java.time.LocalDate;

public record TeacherRequest(
    @NotBlank(message = "First name is required") String firstName,
    @NotBlank(message = "Last name is required") String lastName,
    @Email(message = "Invalid email format") @NotBlank(message = "Email is required") String email,
    LocalDate dateOfBirth,
    @NotNull(message = "Position is required") TeacherPosition position
) {}
