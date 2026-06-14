package ua.university.sms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.university.sms.model.dto.teacher.TeacherRequest;
import ua.university.sms.model.dto.teacher.TeacherResponse;
import ua.university.sms.service.TeacherService;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
@Tag(name = "Teachers", description = "Teacher management API")
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new teacher")
    public TeacherResponse create(@Valid @RequestBody TeacherRequest request) {
        return teacherService.create(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get teacher by ID")
    public TeacherResponse getById(@PathVariable Long id) {
        return teacherService.getById(id);
    }

    @GetMapping
    @Operation(summary = "List all teachers")
    public List<TeacherResponse> getAll() {
        return teacherService.getAll();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update teacher")
    public TeacherResponse update(@PathVariable Long id, @Valid @RequestBody TeacherRequest request) {
        return teacherService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete teacher")
    public void delete(@PathVariable Long id) {
        teacherService.delete(id);
    }
}
