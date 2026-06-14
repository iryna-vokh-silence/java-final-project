package ua.university.sms.exception;

public class DuplicateEnrollmentException extends RuntimeException {
    public DuplicateEnrollmentException(Long studentId, Long courseId) {
        super("Student " + studentId + " is already enrolled in course " + courseId);
    }
}
