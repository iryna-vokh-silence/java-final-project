package ua.university.sms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.university.sms.model.entity.Enrollment;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByCourseId(Long courseId);

    boolean existsByStudentIdAndCourseIdAndSemesterAndYear(
            Long studentId, Long courseId, String semester, Integer year);

    List<Enrollment> findByPaidFalse();

    List<Enrollment> findByStudentIdAndPaidFalse(Long studentId);

    @Query("SELECT e.course.id AS courseId, e.course.name AS courseTitle, " +
           "COUNT(CASE WHEN e.grade = 'A' THEN 1 END) * 1.0 / NULLIF(COUNT(e), 0) AS averageGpa " +
           "FROM Enrollment e " +
           "WHERE e.grade <> 'NA' " +
           "GROUP BY e.course.id, e.course.name")
    List<GpaProjection> findAverageGpaByCourse();

    @Query("SELECT AVG(CASE e.grade " +
           "WHEN 'A' THEN 4.0 WHEN 'B' THEN 3.0 WHEN 'C' THEN 2.0 " +
           "WHEN 'D' THEN 1.0 WHEN 'F' THEN 0.0 ELSE NULL END) " +
           "FROM Enrollment e WHERE e.course.id = :courseId AND e.grade <> 'NA'")
    Optional<Double> findAverageGradeByCourseId(@Param("courseId") Long courseId);
}
