package ua.university.sms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.university.sms.model.dto.enrollment.TranscriptResponse;
import ua.university.sms.model.dto.student.StudentRequest;
import ua.university.sms.model.dto.student.StudentResponse;
import ua.university.sms.model.entity.StudentStatus;
import ua.university.sms.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Tag(name = "Students", description = "Student management API")
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new student")
    public StudentResponse create(@Valid @RequestBody StudentRequest request) {
        return studentService.create(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID")
    public StudentResponse getById(@PathVariable Long id) {
        return studentService.getById(id);
    }

    @GetMapping
    @Operation(summary = "List students (paginated). Optional filters: status, year")
    public Page<StudentResponse> getAll(
            @RequestParam(required = false) StudentStatus status,
            @RequestParam(required = false) Integer year,
            @PageableDefault(size = 20, sort = "lastName") Pageable pageable) {
        if (status != null && year != null) {
            return studentService.filterByStatusAndYear(status, year, pageable);
        } else if (status != null) {
            return studentService.filterByStatus(status, pageable);
        } else if (year != null) {
            return studentService.filterByYear(year, pageable);
        }
        return studentService.getAll(pageable);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update student")
    public StudentResponse update(@PathVariable Long id, @Valid @RequestBody StudentRequest request) {
        return studentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete student")
    public void delete(@PathVariable Long id) {
        studentService.delete(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search students by name or email")
    public List<StudentResponse> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {
        if (name != null) return studentService.searchByName(name);
        if (email != null) return studentService.searchByEmail(email);
        return List.of();
    }

    @GetMapping("/top")
    @Operation(summary = "Get top-N students by GPA")
    public List<StudentResponse> topByGpa(@RequestParam(defaultValue = "10") int n) {
        return studentService.getTopStudentsByGpa(n);
    }

    @GetMapping("/{id}/transcript")
    @Operation(summary = "Get student transcript with GPA calculation")
    public TranscriptResponse getTranscript(@PathVariable Long id) {
        return studentService.getTranscript(id);
    }
}
