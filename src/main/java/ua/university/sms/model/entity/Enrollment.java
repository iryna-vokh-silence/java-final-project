package ua.university.sms.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "enrollments",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_student_course_semester_year",
        columnNames = {"student_id", "course_id", "semester", "enrollment_year"}
    )
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment implements Payable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "enrollment_seq")
    @SequenceGenerator(name = "enrollment_seq", sequenceName = "enrollments_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false, length = 20)
    private String semester;

    @Column(name = "enrollment_year", nullable = false)
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 5)
    @Builder.Default
    private Grade grade = Grade.NA;

    @Column(nullable = false)
    @Builder.Default
    private Boolean paid = false;

    @Override
    public void markAsPaid() {
        this.paid = true;
    }
}
