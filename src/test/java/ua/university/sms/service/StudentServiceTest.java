package ua.university.sms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ua.university.sms.exception.ResourceNotFoundException;
import ua.university.sms.mapper.StudentMapper;
import ua.university.sms.model.dto.student.StudentRequest;
import ua.university.sms.model.dto.student.StudentResponse;
import ua.university.sms.model.entity.Student;
import ua.university.sms.model.entity.StudentStatus;
import ua.university.sms.repository.StudentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void create_savesAndReturnsResponse() {
        StudentRequest req = new StudentRequest("John", "Doe", "john@uni.ua",
                LocalDate.of(2023, 9, 1), StudentStatus.ACTIVE, 1);
        Student entity = Student.builder().id(1L).firstName("John").lastName("Doe").build();
        StudentResponse response = new StudentResponse(1L, "John", "Doe", "john@uni.ua",
                LocalDate.of(2023, 9, 1), StudentStatus.ACTIVE, 1);

        when(studentMapper.toEntity(req)).thenReturn(entity);
        when(studentRepository.save(entity)).thenReturn(entity);
        when(studentMapper.toResponse(entity)).thenReturn(response);

        StudentResponse result = studentService.create(req);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.firstName()).isEqualTo("John");
        verify(studentRepository).save(entity);
    }

    @Test
    void getById_notFound_throwsException() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Student not found with id: 99");
    }

    @Test
    void update_existingStudent_updatesAndReturns() {
        StudentRequest req = new StudentRequest("Jane", "Doe", "jane@uni.ua",
                LocalDate.of(2023, 9, 1), StudentStatus.ACTIVE, 2);
        Student existing = Student.builder().id(1L).build();
        StudentResponse response = new StudentResponse(1L, "Jane", "Doe", "jane@uni.ua",
                LocalDate.of(2023, 9, 1), StudentStatus.ACTIVE, 2);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(studentRepository.save(existing)).thenReturn(existing);
        when(studentMapper.toResponse(existing)).thenReturn(response);

        StudentResponse result = studentService.update(1L, req);

        assertThat(result.firstName()).isEqualTo("Jane");
        verify(studentMapper).updateEntity(existing, req);
    }

    @Test
    void delete_existingStudent_deletesById() {
        Student existing = Student.builder().id(1L).build();
        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));

        studentService.delete(1L);

        verify(studentRepository).deleteById(1L);
    }

    @Test
    void filterByStatus_delegatesToRepo() {
        Student s = Student.builder().id(1L).status(StudentStatus.ACTIVE).build();
        StudentResponse r = new StudentResponse(1L, "A", "B", "a@b.ua",
                LocalDate.now(), StudentStatus.ACTIVE, 1);

        when(studentRepository.findByStatus(StudentStatus.ACTIVE)).thenReturn(List.of(s));
        when(studentMapper.toResponse(s)).thenReturn(r);

        List<StudentResponse> result = studentService.filterByStatus(StudentStatus.ACTIVE);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).status()).isEqualTo(StudentStatus.ACTIVE);
    }

    @Test
    void getTopStudentsByGpa_callsPageableQuery() {
        when(studentRepository.findTopStudentsByGpa(any(Pageable.class))).thenReturn(List.of());

        studentService.getTopStudentsByGpa(5);

        verify(studentRepository).findTopStudentsByGpa(any(Pageable.class));
    }
}
