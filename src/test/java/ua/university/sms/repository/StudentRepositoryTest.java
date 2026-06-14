package ua.university.sms.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import ua.university.sms.model.entity.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    private Student student1;
    private Student student2;

    @BeforeEach
    void setUp() {
        enrollmentRepository.deleteAll();
        courseRepository.deleteAll();
        studentRepository.deleteAll();
        teacherRepository.deleteAll();

        student1 = studentRepository.save(Student.builder()
                .firstName("John").lastName("Smith").email("john@uni.ua")
                .enrollmentDate(LocalDate.of(2023, 9, 1))
                .status(StudentStatus.ACTIVE).year(1).build());

        student2 = studentRepository.save(Student.builder()
                .firstName("Anna").lastName("Johnson").email("anna@uni.ua")
                .enrollmentDate(LocalDate.of(2022, 9, 1))
                .status(StudentStatus.INACTIVE).year(2).build());
    }

    @Test
    void searchByName_partialFirstName_returnsMatch() {
        List<Student> result = studentRepository.searchByName("smith");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    void searchByName_partialLastName_returnsMatches() {
        List<Student> result = studentRepository.searchByName("son");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLastName()).isEqualTo("Johnson");
    }

    @Test
    void findByStatus_returnsOnlyActive() {
        List<Student> result = studentRepository.findByStatus(StudentStatus.ACTIVE);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("john@uni.ua");
    }

    @Test
    void findByYear_returnsCorrect() {
        List<Student> result = studentRepository.findByYear(2);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("anna@uni.ua");
    }

    @Test
    void findTopStudentsByGpa_returnsStudentsWithGrades() {
        Teacher teacher = teacherRepository.save(Teacher.builder()
                .firstName("Prof").lastName("X").email("prof@uni.ua").department("CS").build());
        Course course = courseRepository.save(Course.builder()
                .title("Math").credits(3).teacher(teacher).build());

        enrollmentRepository.save(Enrollment.builder()
                .student(student1).course(course)
                .enrollmentDate(LocalDate.now()).grade(new BigDecimal("90.0")).paid(true).build());

        List<Student> top = studentRepository.findTopStudentsByGpa(PageRequest.of(0, 5));

        assertThat(top).isNotEmpty();
        assertThat(top.get(0).getId()).isEqualTo(student1.getId());
    }
}
