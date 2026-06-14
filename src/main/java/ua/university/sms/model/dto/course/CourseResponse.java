package ua.university.sms.model.dto.course;

public record CourseResponse(
    Long id,
    String name,
    String description,
    Integer credits,
    Long teacherId,
    String teacherFullName
) {}
