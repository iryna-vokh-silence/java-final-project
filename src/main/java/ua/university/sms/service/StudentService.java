package ua.university.sms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.university.sms.model.dto.enrollment.TranscriptResponse;
import ua.university.sms.model.dto.student.StudentRequest;
import ua.university.sms.model.dto.student.StudentResponse;
import ua.university.sms.model.entity.StudentStatus;

import java.util.List;

public interface StudentService {
    StudentResponse create(StudentRequest request);
    StudentResponse getById(Long id);
    Page<StudentResponse> getAll(Pageable pageable);
    StudentResponse update(Long id, StudentRequest request);
    void delete(Long id);

    Page<StudentResponse> filterByStatus(StudentStatus status, Pageable pageable);
    Page<StudentResponse> filterByYear(Integer year, Pageable pageable);
    Page<StudentResponse> filterByStatusAndYear(StudentStatus status, Integer year, Pageable pageable);

    List<StudentResponse> searchByName(String name);
    List<StudentResponse> searchByEmail(String email);

    List<StudentResponse> getTopStudentsByGpa(int topN);

    TranscriptResponse getTranscript(Long studentId);
}
