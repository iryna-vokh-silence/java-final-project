package ua.university.sms.repository;

public interface GpaProjection {
    Long getCourseId();
    String getCourseTitle();
    Double getAverageGpa();
}
