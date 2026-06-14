package ua.university.sms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.university.sms.exception.DuplicateEnrollmentException;
import ua.university.sms.exception.ResourceNotFoundException;
import ua.university.sms.mapper.EnrollmentMapper;
import ua.university.sms.model.dto.enrollment.EnrollmentRequest;
import ua.university.sms.model.dto.enrollment.EnrollmentResponse;
import ua.university.sms.model.dto.enrollment.GpaResponse;
import ua.university.sms.model.dto.enrollment.GradeRequest;
import ua.university.sms.model.entity.Course;
import ua.university.sms.model.entity.Enrollment;
import ua.university.sms.model.entity.Student;
import ua.university.sms.repository.CourseRepository;
import ua.university.sms.repository.EnrollmentRepository;
import ua.university.sms.repository.StudentRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;

    @Override
    public EnrollmentResponse enroll(EnrollmentRequest request) {
        if (enrollmentRepository.existsByStudentIdAndCourseId(request.studentId(), request.courseId())) {
            throw new DuplicateEnrollmentException(request.studentId(), request.courseId());
        }
        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", request.studentId()));
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", request.courseId()));

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .enrollmentDate(LocalDate.now())
                .paid(false)
                .build();
        return enrollmentMapper.toResponse(enrollmentRepository.save(enrollment));
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentResponse getById(Long id) {
        return enrollmentMapper.toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getByStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(enrollmentMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getByCourse(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(enrollmentMapper::toResponse).toList();
    }

    @Override
    public EnrollmentResponse setGrade(Long enrollmentId, GradeRequest gradeRequest) {
        Enrollment enrollment = findOrThrow(enrollmentId);
        enrollment.setGrade(gradeRequest.grade());
        return enrollmentMapper.toResponse(enrollmentRepository.save(enrollment));
    }

    @Override
    public EnrollmentResponse markAsPaid(Long enrollmentId) {
        Enrollment enrollment = findOrThrow(enrollmentId);
        enrollment.setPaid(true);
        return enrollmentMapper.toResponse(enrollmentRepository.save(enrollment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getUnpaidEnrollments() {
        return enrollmentRepository.findByPaidFalse().stream()
                .map(enrollmentMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getUnpaidByStudent(Long studentId) {
        return enrollmentRepository.findByStudentIdAndPaidFalse(studentId).stream()
                .map(enrollmentMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GpaResponse> getAverageGpaByCourse() {
        return enrollmentRepository.findAverageGpaByCourse().stream()
                .map(p -> new GpaResponse(p.getCourseId(), p.getCourseTitle(), p.getAverageGpa()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GpaResponse> getAverageGpaBySemester(LocalDate from, LocalDate to) {
        return enrollmentRepository.findAverageGpaBySemester(from, to).stream()
                .map(p -> new GpaResponse(p.getCourseId(), p.getCourseTitle(), p.getAverageGpa()))
                .toList();
    }

    private Enrollment findOrThrow(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", id));
    }
}
