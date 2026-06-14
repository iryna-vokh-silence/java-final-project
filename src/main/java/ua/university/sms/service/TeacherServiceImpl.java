package ua.university.sms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.university.sms.exception.ResourceNotFoundException;
import ua.university.sms.mapper.TeacherMapper;
import ua.university.sms.model.dto.teacher.TeacherRequest;
import ua.university.sms.model.dto.teacher.TeacherResponse;
import ua.university.sms.model.entity.Teacher;
import ua.university.sms.repository.TeacherRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    @Override
    public TeacherResponse create(TeacherRequest request) {
        Teacher teacher = teacherMapper.toEntity(request);
        return teacherMapper.toResponse(teacherRepository.save(teacher));
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherResponse getById(Long id) {
        return teacherMapper.toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherResponse> getAll() {
        return teacherRepository.findAll().stream()
                .map(teacherMapper::toResponse).toList();
    }

    @Override
    public TeacherResponse update(Long id, TeacherRequest request) {
        Teacher teacher = findOrThrow(id);
        teacherMapper.updateEntity(teacher, request);
        return teacherMapper.toResponse(teacherRepository.save(teacher));
    }

    @Override
    public void delete(Long id) {
        findOrThrow(id);
        teacherRepository.deleteById(id);
    }

    private Teacher findOrThrow(Long id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", id));
    }
}
