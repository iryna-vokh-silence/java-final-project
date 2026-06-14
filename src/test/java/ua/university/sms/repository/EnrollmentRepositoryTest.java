package ua.university.sms.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ua.university.sms.model.entity.*;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EnrollmentRepositoryTest {

    @Autowired private EnrollmentRepository enrollmentRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private TeacherRepository teacherRepository;

    private Student student;
    private Course course;

    @BeforeEach
    void setUp() {
        enrollmentRepository.deleteAll();
        courseRepository.deleteAll();
        studentRepository.deleteAll();
        teacherRepository.deleteAll();

        student = studentRepository.save(Student.builder()
                .firstName("John").lastName("Doe").email("jd@uni.ua")
                .enrollmentYear(2023).status(StudentStatus.ACTIVE).build());

        Teacher teacher = teacherRepository.save(Teacher.builder()
                .firstName("Prof").lastName("T").email("pt@uni.ua")
                .position(TeacherPosition.LECTURER).build());

        course = courseRepository.save(Course.builder()
                .name("Algorithms").credits(4).teacher(teacher).build());
    }

    @Test
    void existsByStudentIdAndCourseIdAndSemesterAndYear_trueWhenExists() {
        enrollmentRepository.save(Enrollment.builder()
                .student(student).course(course)
                .semester("Fall").year(2023).paid(false).build());

        assertThat(enrollmentRepository
                .existsByStudentIdAndCourseIdAndSemesterAndYear(student.getId(), course.getId(), "Fall", 2023))
                .isTrue();
    }

    @Test
    void existsByStudentIdAndCourseIdAndSemesterAndYear_falseWhenAbsent() {
        assertThat(enrollmentRepository
                .existsByStudentIdAndCourseIdAndSemesterAndYear(student.getId(), course.getId(), "Fall", 2023))
                .isFalse();
    }

    @Test
    void findByPaidFalse_returnsUnpaid() {
        enrollmentRepository.save(Enrollment.builder()
                .student(student).course(course)
                .semester("Fall").year(2023).paid(false).build());

        List<Enrollment> unpaid = enrollmentRepository.findByPaidFalse();

        assertThat(unpaid).hasSize(1);
        assertThat(unpaid.get(0).getPaid()).isFalse();
    }

    @Test
    void findAverageGpaByCourse_returnsProjection() {
        enrollmentRepository.save(Enrollment.builder()
                .student(student).course(course)
                .semester("Fall").year(2023).grade(Grade.A).paid(true).build());

        List<GpaProjection> gpaList = enrollmentRepository.findAverageGpaByCourse();

        assertThat(gpaList).hasSize(1);
        assertThat(gpaList.get(0).getCourseTitle()).isEqualTo("Algorithms");
    }
}
