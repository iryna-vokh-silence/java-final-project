package ua.university.sms.model.dto.enrollment;

import jakarta.validation.constraints.NotNull;
import ua.university.sms.model.entity.Grade;

public record GradeRequest(
    @NotNull(message = "Grade is required") Grade grade
) {}
