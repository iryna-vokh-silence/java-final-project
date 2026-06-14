package ua.university.sms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.university.sms.model.entity.Enrollment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByCourseId(Long courseId);

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    List<Enrollment> findByPaidFalse();

    List<Enrollment> findByStudentIdAndPaidFalse(Long studentId);

    @Query("SELECT e.course.id AS courseId, e.course.title AS courseTitle, " +
           "AVG(e.grade) AS averageGpa " +
           "FROM Enrollment e " +
           "WHERE e.grade IS NOT NULL " +
           "GROUP BY e.course.id, e.course.title")
    List<GpaProjection> findAverageGpaByCourse();

    @Query("SELECT AVG(e.grade) FROM Enrollment e WHERE e.course.id = :courseId AND e.grade IS NOT NULL")
    Optional<Double> findAverageGradeByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT e.course.id AS courseId, e.course.title AS courseTitle, " +
           "AVG(e.grade) AS averageGpa " +
           "FROM Enrollment e " +
           "WHERE e.grade IS NOT NULL " +
           "  AND e.enrollmentDate BETWEEN :from AND :to " +
           "GROUP BY e.course.id, e.course.title")
    List<GpaProjection> findAverageGpaBySemester(@Param("from") LocalDate from,
                                                  @Param("to") LocalDate to);
}
