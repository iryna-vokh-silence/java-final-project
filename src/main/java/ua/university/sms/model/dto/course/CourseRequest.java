package ua.university.sms.model.dto.course;

import jakarta.validation.constraints.*;

public record CourseRequest(
    @NotBlank(message = "Name is required") String name,
    String description,
    @NotNull(message = "Credits are required") @Min(1) Integer credits,
    @NotNull(message = "Teacher ID is required") Long teacherId
) {}
