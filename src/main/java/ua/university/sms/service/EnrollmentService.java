package ua.university.sms.service;

import ua.university.sms.model.dto.enrollment.EnrollmentRequest;
import ua.university.sms.model.dto.enrollment.EnrollmentResponse;
import ua.university.sms.model.dto.enrollment.GpaResponse;
import ua.university.sms.model.dto.enrollment.GradeRequest;

import java.time.LocalDate;
import java.util.List;

public interface EnrollmentService {
    EnrollmentResponse enroll(EnrollmentRequest request);
    EnrollmentResponse getById(Long id);
    List<EnrollmentResponse> getByStudent(Long studentId);
    List<EnrollmentResponse> getByCourse(Long courseId);

    EnrollmentResponse setGrade(Long enrollmentId, GradeRequest gradeRequest);
    EnrollmentResponse markAsPaid(Long enrollmentId);

    List<EnrollmentResponse> getUnpaidEnrollments();
    List<EnrollmentResponse> getUnpaidByStudent(Long studentId);

    List<GpaResponse> getAverageGpaByCourse();
    List<GpaResponse> getAverageGpaBySemester(LocalDate from, LocalDate to);
}
