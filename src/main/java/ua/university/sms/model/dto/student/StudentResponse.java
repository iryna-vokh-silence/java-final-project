package ua.university.sms.model.dto.student;

import ua.university.sms.model.entity.StudentStatus;

import java.time.LocalDate;

public record StudentResponse(
    Long id,
    String firstName,
    String lastName,
    String email,
    LocalDate enrollmentDate,
    StudentStatus status,
    Integer year
) {}
