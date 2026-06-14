package ua.university.sms.model.dto.teacher;

public record TeacherResponse(
    Long id,
    String firstName,
    String lastName,
    String email,
    String department
) {}
