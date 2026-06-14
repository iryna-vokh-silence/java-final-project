package ua.university.sms.model.dto.enrollment;

import ua.university.sms.model.entity.Grade;

public record EnrollmentResponse(
    Long id,
    Long studentId,
    String studentFullName,
    Long courseId,
    String courseName,
    String semester,
    Integer year,
    Grade grade,
    Boolean paid
) {}
