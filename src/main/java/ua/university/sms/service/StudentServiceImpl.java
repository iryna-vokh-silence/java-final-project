package ua.university.sms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.university.sms.exception.ResourceNotFoundException;
import ua.university.sms.mapper.StudentMapper;
import ua.university.sms.model.dto.enrollment.TranscriptResponse;
import ua.university.sms.model.dto.student.StudentRequest;
import ua.university.sms.model.dto.student.StudentResponse;
import ua.university.sms.model.entity.*;
import ua.university.sms.repository.StudentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    public StudentResponse create(StudentRequest request) {
        Student student = studentMapper.toEntity(request);
        return studentMapper.toResponse(studentRepository.save(student));
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse getById(Long id) {
        return studentMapper.toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentResponse> getAll(Pageable pageable) {
        return studentRepository.findAll(pageable).map(studentMapper::toResponse);
    }

    @Override
    public StudentResponse update(Long id, StudentRequest request) {
        Student student = findOrThrow(id);
        studentMapper.updateEntity(student, request);
        return studentMapper.toResponse(studentRepository.save(student));
    }

    @Override
    public void delete(Long id) {
        findOrThrow(id);
        studentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentResponse> filterByStatus(StudentStatus status, Pageable pageable) {
        return studentRepository.findByStatus(status, pageable).map(studentMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentResponse> filterByYear(Integer year, Pageable pageable) {
        return studentRepository.findByEnrollmentYear(year, pageable).map(studentMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentResponse> filterByStatusAndYear(StudentStatus status, Integer year, Pageable pageable) {
        return studentRepository.findByStatusAndEnrollmentYear(status, year, pageable).map(studentMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> searchByName(String name) {
        return studentRepository.searchByName(name).stream()
                .map(studentMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> searchByEmail(String email) {
        return studentRepository.findByEmailContainingIgnoreCase(email).stream()
                .map(studentMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> getTopStudentsByGpa(int topN) {
        return studentRepository.findTopStudentsByGpa(PageRequest.of(0, topN)).stream()
                .map(studentMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TranscriptResponse getTranscript(Long studentId) {
        Student student = findOrThrow(studentId);

        List<TranscriptResponse.TranscriptEntry> entries = student.getEnrollments().stream()
                .map(e -> new TranscriptResponse.TranscriptEntry(
                        e.getCourse().getName(),
                        e.getCourse().getCredits(),
                        e.getSemester(),
                        e.getYear(),
                        e.getGrade()
                ))
                .toList();

        double gpa = calculateGpa(student.getEnrollments());

        return new TranscriptResponse(
                student.getId(),
                student.getFirstName() + " " + student.getLastName(),
                entries,
                gpa
        );
    }

    private double calculateGpa(List<Enrollment> enrollments) {
        return enrollments.stream()
                .filter(e -> e.getGrade() != Grade.NA)
                .mapToDouble(e -> switch (e.getGrade()) {
                    case A -> 4.0;
                    case B -> 3.0;
                    case C -> 2.0;
                    case D -> 1.0;
                    case F -> 0.0;
                    default -> 0.0;
                })
                .average()
                .orElse(0.0);
    }

    private Student findOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
    }
}
