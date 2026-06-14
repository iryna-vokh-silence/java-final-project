package ua.university.sms.service;

import ua.university.sms.model.dto.teacher.TeacherRequest;
import ua.university.sms.model.dto.teacher.TeacherResponse;

import java.util.List;

public interface TeacherService {
    TeacherResponse create(TeacherRequest request);
    TeacherResponse getById(Long id);
    List<TeacherResponse> getAll();
    TeacherResponse update(Long id, TeacherRequest request);
    void delete(Long id);
}
