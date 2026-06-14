package ua.university.sms.model.dto.course;

import jakarta.validation.constraints.*;

public record CourseRequest(
    @NotBlank(message = "Title is required") String title,
    String description,
    @NotNull(message = "Credits are required") @Min(1) Integer credits,
    @NotNull(message = "Teacher ID is required") Long teacherId
) {}
