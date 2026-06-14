package ua.university.sms.model.dto.enrollment;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EnrollmentResponse(
    Long id,
    Long studentId,
    String studentFullName,
    Long courseId,
    String courseTitle,
    LocalDate enrollmentDate,
    BigDecimal grade,
    Boolean paid
) {}
