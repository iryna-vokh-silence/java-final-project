package ua.university.sms.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.university.sms.model.entity.Student;
import ua.university.sms.model.entity.StudentStatus;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByStatus(StudentStatus status);

    List<Student> findByYear(Integer year);

    List<Student> findByStatusAndYear(StudentStatus status, Integer year);

    @Query("SELECT s FROM Student s WHERE " +
           "LOWER(s.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(s.lastName)  LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Student> searchByName(@Param("name") String name);

    List<Student> findByEmailContainingIgnoreCase(String email);

    @Query("SELECT s FROM Student s " +
           "JOIN s.enrollments e " +
           "WHERE e.grade IS NOT NULL " +
           "GROUP BY s " +
           "ORDER BY AVG(e.grade) DESC")
    List<Student> findTopStudentsByGpa(Pageable pageable);

    boolean existsByEmail(String email);
}
