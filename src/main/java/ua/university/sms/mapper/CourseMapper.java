package ua.university.sms.mapper;

import org.springframework.stereotype.Component;
import ua.university.sms.model.dto.course.CourseRequest;
import ua.university.sms.model.dto.course.CourseResponse;
import ua.university.sms.model.entity.Course;
import ua.university.sms.model.entity.Teacher;

@Component
public class CourseMapper {

    public Course toEntity(CourseRequest req, Teacher teacher) {
        return Course.builder()
                .title(req.title())
                .description(req.description())
                .credits(req.credits())
                .teacher(teacher)
                .build();
    }

    public CourseResponse toResponse(Course course) {
        Teacher teacher = course.getTeacher();
        return new CourseResponse(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getCredits(),
                teacher.getId(),
                teacher.getFirstName() + " " + teacher.getLastName()
        );
    }

    public void updateEntity(Course course, CourseRequest req, Teacher teacher) {
        course.setTitle(req.title());
        course.setDescription(req.description());
        course.setCredits(req.credits());
        course.setTeacher(teacher);
    }
}
