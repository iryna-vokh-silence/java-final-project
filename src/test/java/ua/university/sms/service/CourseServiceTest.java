package ua.university.sms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.university.sms.exception.ResourceNotFoundException;
import ua.university.sms.mapper.CourseMapper;
import ua.university.sms.model.dto.course.CourseRequest;
import ua.university.sms.model.entity.Course;
import ua.university.sms.model.entity.Teacher;
import ua.university.sms.repository.CourseRepository;
import ua.university.sms.repository.TeacherRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Test
    void create_withNonExistentTeacher_throwsException() {
        CourseRequest req = new CourseRequest("Math", "Basic math", 3, 99L);
        when(teacherRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.create(req))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Teacher not found with id: 99");
    }

    @Test
    void create_withValidTeacher_savesAndReturns() {
        CourseRequest req = new CourseRequest("Math", "Basic math", 3, 1L);
        Teacher teacher = Teacher.builder().id(1L).firstName("Prof").lastName("X").build();
        Course course = Course.builder().id(10L).title("Math").credits(3).teacher(teacher).build();

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(courseMapper.toEntity(req, teacher)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(course);

        courseService.create(req);

        verify(courseRepository).save(course);
    }
}
