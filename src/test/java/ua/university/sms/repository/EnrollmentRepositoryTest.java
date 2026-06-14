package ua.university.sms.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ua.university.sms.model.entity.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EnrollmentRepositoryTest {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

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
                .enrollmentDate(LocalDate.now()).status(StudentStatus.ACTIVE).year(1).build());

        Teacher teacher = teacherRepository.save(Teacher.builder()
                .firstName("Prof").lastName("T").email("pt@uni.ua").department("CS").build());

        course = courseRepository.save(Course.builder()
                .title("Algorithms").credits(4).teacher(teacher).build());
    }

    @Test
    void existsByStudentIdAndCourseId_returnsTrueWhenExists() {
        enrollmentRepository.save(Enrollment.builder()
                .student(student).course(course)
                .enrollmentDate(LocalDate.now()).paid(false).build());

        assertThat(enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), course.getId()))
                .isTrue();
    }

    @Test
    void existsByStudentIdAndCourseId_returnsFalseWhenAbsent() {
        assertThat(enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), course.getId()))
                .isFalse();
    }

    @Test
    void findByPaidFalse_returnsUnpaid() {
        enrollmentRepository.save(Enrollment.builder()
                .student(student).course(course)
                .enrollmentDate(LocalDate.now()).paid(false).build());

        List<Enrollment> unpaid = enrollmentRepository.findByPaidFalse();

        assertThat(unpaid).hasSize(1);
        assertThat(unpaid.get(0).getPaid()).isFalse();
    }

    @Test
    void findAverageGpaByCourse_returnsProjection() {
        enrollmentRepository.save(Enrollment.builder()
                .student(student).course(course)
                .enrollmentDate(LocalDate.now())
                .grade(new BigDecimal("80.0")).paid(true).build());

        List<GpaProjection> gpaList = enrollmentRepository.findAverageGpaByCourse();

        assertThat(gpaList).hasSize(1);
        assertThat(gpaList.get(0).getCourseTitle()).isEqualTo("Algorithms");
        assertThat(gpaList.get(0).getAverageGpa()).isEqualTo(80.0);
    }
}
