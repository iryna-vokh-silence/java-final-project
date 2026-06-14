package ua.university.sms.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.university.sms.model.entity.Student;
import ua.university.sms.model.entity.StudentStatus;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Page<Student> findAll(Pageable pageable);

    List<Student> findByStatus(StudentStatus status);
    Page<Student> findByStatus(StudentStatus status, Pageable pageable);

    List<Student> findByEnrollmentYear(Integer enrollmentYear);
    Page<Student> findByEnrollmentYear(Integer enrollmentYear, Pageable pageable);

    List<Student> findByStatusAndEnrollmentYear(StudentStatus status, Integer enrollmentYear);
    Page<Student> findByStatusAndEnrollmentYear(StudentStatus status, Integer enrollmentYear, Pageable pageable);

    @Query("SELECT s FROM Student s WHERE " +
           "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(s.lastName)  LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Student> searchByName(@Param("name") String name);

    List<Student> findByEmailContainingIgnoreCase(String email);

    @Query("SELECT s FROM Student s " +
           "JOIN s.enrollments e " +
           "WHERE e.grade <> 'NA' " +
           "GROUP BY s " +
           "ORDER BY COUNT(CASE WHEN e.grade = 'A' THEN 1 END) DESC")
    List<Student> findTopStudentsByGpa(Pageable pageable);

    boolean existsByEmail(String email);
}
