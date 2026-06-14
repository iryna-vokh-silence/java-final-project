package ua.university.sms.service;

import ua.university.sms.model.dto.student.StudentRequest;
import ua.university.sms.model.dto.student.StudentResponse;
import ua.university.sms.model.entity.StudentStatus;

import java.util.List;

public interface StudentService {
    StudentResponse create(StudentRequest request);
    StudentResponse getById(Long id);
    List<StudentResponse> getAll();
    StudentResponse update(Long id, StudentRequest request);
    void delete(Long id);

    List<StudentResponse> filterByStatus(StudentStatus status);
    List<StudentResponse> filterByYear(Integer year);
    List<StudentResponse> filterByStatusAndYear(StudentStatus status, Integer year);

    List<StudentResponse> searchByName(String name);
    List<StudentResponse> searchByEmail(String email);

    List<StudentResponse> getTopStudentsByGpa(int topN);
}
