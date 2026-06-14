package ua.university.sms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.university.sms.exception.DuplicateEnrollmentException;
import ua.university.sms.mapper.EnrollmentMapper;
import ua.university.sms.model.dto.enrollment.EnrollmentRequest;
import ua.university.sms.model.dto.enrollment.EnrollmentResponse;
import ua.university.sms.model.dto.enrollment.GradeRequest;
import ua.university.sms.model.entity.*;
import ua.university.sms.repository.CourseRepository;
import ua.university.sms.repository.EnrollmentRepository;
import ua.university.sms.repository.StudentRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EnrollmentMapper enrollmentMapper;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    @Test
    void enroll_duplicate_throwsDuplicateEnrollmentException() {
        EnrollmentRequest req = new EnrollmentRequest(1L, 2L);
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 2L)).thenReturn(true);

        assertThatThrownBy(() -> enrollmentService.enroll(req))
                .isInstanceOf(DuplicateEnrollmentException.class)
                .hasMessageContaining("already enrolled");
    }

    @Test
    void enroll_happyPath_savesEnrollment() {
        EnrollmentRequest req = new EnrollmentRequest(1L, 2L);
        Student student = Student.builder().id(1L).firstName("A").lastName("B").build();
        Teacher teacher = Teacher.builder().id(1L).firstName("T").lastName("X").build();
        Course course = Course.builder().id(2L).title("Math").credits(3).teacher(teacher).build();
        Enrollment saved = Enrollment.builder().id(10L).student(student).course(course)
                .enrollmentDate(LocalDate.now()).paid(false).build();
        EnrollmentResponse response = new EnrollmentResponse(10L, 1L, "A B", 2L, "Math",
                LocalDate.now(), null, false);

        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 2L)).thenReturn(false);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(saved);
        when(enrollmentMapper.toResponse(saved)).thenReturn(response);

        EnrollmentResponse result = enrollmentService.enroll(req);

        assertThat(result.id()).isEqualTo(10L);
        assertThat(result.paid()).isFalse();
    }

    @Test
    void setGrade_updatesGrade() {
        Teacher teacher = Teacher.builder().id(1L).firstName("T").lastName("X").build();
        Course course = Course.builder().id(2L).title("Math").credits(3).teacher(teacher).build();
        Student student = Student.builder().id(1L).firstName("A").lastName("B").build();
        Enrollment enrollment = Enrollment.builder().id(5L).student(student).course(course)
                .enrollmentDate(LocalDate.now()).paid(false).build();
        GradeRequest gradeReq = new GradeRequest(new BigDecimal("85.5"));
        EnrollmentResponse response = new EnrollmentResponse(5L, 1L, "A B", 2L, "Math",
                LocalDate.now(), new BigDecimal("85.5"), false);

        when(enrollmentRepository.findById(5L)).thenReturn(Optional.of(enrollment));
        when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);
        when(enrollmentMapper.toResponse(enrollment)).thenReturn(response);

        EnrollmentResponse result = enrollmentService.setGrade(5L, gradeReq);

        assertThat(result.grade()).isEqualByComparingTo("85.5");
        assertThat(enrollment.getGrade()).isEqualByComparingTo("85.5");
    }

    @Test
    void markAsPaid_setsPaidTrue() {
        Teacher teacher = Teacher.builder().id(1L).firstName("T").lastName("X").build();
        Course course = Course.builder().id(2L).title("Math").credits(3).teacher(teacher).build();
        Student student = Student.builder().id(1L).firstName("A").lastName("B").build();
        Enrollment enrollment = Enrollment.builder().id(5L).student(student).course(course)
                .enrollmentDate(LocalDate.now()).paid(false).build();
        EnrollmentResponse response = new EnrollmentResponse(5L, 1L, "A B", 2L, "Math",
                LocalDate.now(), null, true);

        when(enrollmentRepository.findById(5L)).thenReturn(Optional.of(enrollment));
        when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);
        when(enrollmentMapper.toResponse(enrollment)).thenReturn(response);

        EnrollmentResponse result = enrollmentService.markAsPaid(5L);

        assertThat(result.paid()).isTrue();
        assertThat(enrollment.getPaid()).isTrue();
    }
}
