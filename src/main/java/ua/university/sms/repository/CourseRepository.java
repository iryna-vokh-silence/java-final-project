package ua.university.sms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.university.sms.model.entity.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByTeacherId(Long teacherId);

    List<Course> findByCredits(Integer credits);

    List<Course> findByCreditsGreaterThanEqual(Integer minCredits);

    List<Course> findByTeacherIdAndCredits(Long teacherId, Integer credits);
}
