package ua.university.sms.model.dto.enrollment;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record GradeRequest(
    @NotNull(message = "Grade is required")
    @DecimalMin(value = "0.0", message = "Grade must be at least 0")
    @DecimalMax(value = "100.0", message = "Grade must be at most 100")
    BigDecimal grade
) {}
