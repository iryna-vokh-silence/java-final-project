package ua.university.sms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.university.sms.exception.ResourceNotFoundException;
import ua.university.sms.mapper.CourseMapper;
import ua.university.sms.model.dto.course.CourseRequest;
import ua.university.sms.model.dto.course.CourseResponse;
import ua.university.sms.model.entity.Course;
import ua.university.sms.model.entity.Teacher;
import ua.university.sms.repository.CourseRepository;
import ua.university.sms.repository.TeacherRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final CourseMapper courseMapper;

    @Override
    public CourseResponse create(CourseRequest request) {
        Teacher teacher = teacherRepository.findById(request.teacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", request.teacherId()));
        Course course = courseMapper.toEntity(request, teacher);
        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse getById(Long id) {
        return courseMapper.toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> getAll() {
        return courseRepository.findAll().stream()
                .map(courseMapper::toResponse).toList();
    }

    @Override
    public CourseResponse update(Long id, CourseRequest request) {
        Course course = findOrThrow(id);
        Teacher teacher = teacherRepository.findById(request.teacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", request.teacherId()));
        courseMapper.updateEntity(course, request, teacher);
        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Override
    public void delete(Long id) {
        findOrThrow(id);
        courseRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> filterByTeacher(Long teacherId) {
        return courseRepository.findByTeacherId(teacherId).stream()
                .map(courseMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> filterByCredits(Integer credits) {
        return courseRepository.findByCredits(credits).stream()
                .map(courseMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> filterByTeacherAndCredits(Long teacherId, Integer credits) {
        return courseRepository.findByTeacherIdAndCredits(teacherId, credits).stream()
                .map(courseMapper::toResponse).toList();
    }

    private Course findOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", id));
    }
}
