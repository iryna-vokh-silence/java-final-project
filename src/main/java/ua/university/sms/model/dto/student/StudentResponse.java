package ua.university.sms.model.dto.student;

import ua.university.sms.model.entity.StudentStatus;

public record StudentResponse(
    Long id,
    String firstName,
    String lastName,
    String email,
    Integer enrollmentYear,
    StudentStatus status
) {}
