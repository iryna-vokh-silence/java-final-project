package ua.university.sms.model.entity;

public interface Payable {
    Boolean getPaid();
    void markAsPaid();
}
