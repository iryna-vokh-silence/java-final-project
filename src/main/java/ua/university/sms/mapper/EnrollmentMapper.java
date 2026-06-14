package ua.university.sms.mapper;

import org.springframework.stereotype.Component;
import ua.university.sms.model.dto.enrollment.EnrollmentResponse;
import ua.university.sms.model.entity.Enrollment;

@Component
public class EnrollmentMapper {

    public EnrollmentResponse toResponse(Enrollment e) {
        return new EnrollmentResponse(
                e.getId(),
                e.getStudent().getId(),
                e.getStudent().getFirstName() + " " + e.getStudent().getLastName(),
                e.getCourse().getId(),
                e.getCourse().getName(),
                e.getSemester(),
                e.getYear(),
                e.getGrade(),
                e.getPaid()
        );
    }
}
