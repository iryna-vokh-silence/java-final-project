package ua.university.sms.service;

import ua.university.sms.model.dto.course.CourseRequest;
import ua.university.sms.model.dto.course.CourseResponse;

import java.util.List;

public interface CourseService {
    CourseResponse create(CourseRequest request);
    CourseResponse getById(Long id);
    List<CourseResponse> getAll();
    CourseResponse update(Long id, CourseRequest request);
    void delete(Long id);

    List<CourseResponse> filterByTeacher(Long teacherId);
    List<CourseResponse> filterByCredits(Integer credits);
    List<CourseResponse> filterByTeacherAndCredits(Long teacherId, Integer credits);
}
