package ua.university.sms.model.dto.enrollment;

public record GpaResponse(
    Long courseId,
    String courseName,
    Double averageGpa
) {}
