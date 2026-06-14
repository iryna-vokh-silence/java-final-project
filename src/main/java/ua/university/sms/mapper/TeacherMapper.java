package ua.university.sms.mapper;

import org.springframework.stereotype.Component;
import ua.university.sms.model.dto.teacher.TeacherRequest;
import ua.university.sms.model.dto.teacher.TeacherResponse;
import ua.university.sms.model.entity.Teacher;

@Component
public class TeacherMapper {

    public Teacher toEntity(TeacherRequest req) {
        return Teacher.builder()
                .firstName(req.firstName())
                .lastName(req.lastName())
                .email(req.email())
                .department(req.department())
                .build();
    }

    public TeacherResponse toResponse(Teacher teacher) {
        return new TeacherResponse(
                teacher.getId(),
                teacher.getFirstName(),
                teacher.getLastName(),
                teacher.getEmail(),
                teacher.getDepartment()
        );
    }

    public void updateEntity(Teacher teacher, TeacherRequest req) {
        teacher.setFirstName(req.firstName());
        teacher.setLastName(req.lastName());
        teacher.setEmail(req.email());
        teacher.setDepartment(req.department());
    }
}
