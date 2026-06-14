package ua.university.sms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.university.sms.exception.ResourceNotFoundException;
import ua.university.sms.mapper.StudentMapper;
import ua.university.sms.model.dto.student.StudentRequest;
import ua.university.sms.model.dto.student.StudentResponse;
import ua.university.sms.model.entity.Student;
import ua.university.sms.model.entity.StudentStatus;
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
    public List<StudentResponse> getAll() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toResponse)
                .toList();
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
    public List<StudentResponse> filterByStatus(StudentStatus status) {
        return studentRepository.findByStatus(status).stream()
                .map(studentMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> filterByYear(Integer year) {
        return studentRepository.findByYear(year).stream()
                .map(studentMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponse> filterByStatusAndYear(StudentStatus status, Integer year) {
        return studentRepository.findByStatusAndYear(status, year).stream()
                .map(studentMapper::toResponse).toList();
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

    private Student findOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", id));
    }
}
