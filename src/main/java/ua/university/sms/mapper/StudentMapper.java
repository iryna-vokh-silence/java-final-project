package ua.university.sms.mapper;

import org.springframework.stereotype.Component;
import ua.university.sms.model.dto.student.StudentRequest;
import ua.university.sms.model.dto.student.StudentResponse;
import ua.university.sms.model.entity.Student;

@Component
public class StudentMapper {

    public Student toEntity(StudentRequest req) {
        return Student.builder()
                .firstName(req.firstName())
                .lastName(req.lastName())
                .email(req.email())
                .enrollmentYear(req.enrollmentYear())
                .status(req.status())
                .build();
    }

    public StudentResponse toResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getEnrollmentYear(),
                student.getStatus()
        );
    }

    public void updateEntity(Student student, StudentRequest req) {
        student.setFirstName(req.firstName());
        student.setLastName(req.lastName());
        student.setEmail(req.email());
        student.setEnrollmentYear(req.enrollmentYear());
        student.setStatus(req.status());
    }
}
