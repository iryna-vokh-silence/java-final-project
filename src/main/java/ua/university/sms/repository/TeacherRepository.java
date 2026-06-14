package ua.university.sms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.university.sms.model.entity.Teacher;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    boolean existsByEmail(String email);

    Optional<Teacher> findByEmail(String email);
}
