package ua.university.sms.model.dto.enrollment;

import ua.university.sms.model.entity.Grade;

import java.util.List;

public record TranscriptResponse(
    Long studentId,
    String studentFullName,
    List<TranscriptEntry> entries,
    Double gpa
) {
    public record TranscriptEntry(
        String courseName,
        Integer credits,
        String semester,
        Integer year,
        Grade grade
    ) {}
}
