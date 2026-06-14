package ua.university.sms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.university.sms.model.dto.course.CourseRequest;
import ua.university.sms.model.dto.course.CourseResponse;
import ua.university.sms.service.CourseService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
@Tag(name = "Courses", description = "Course management API")
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new course")
    public CourseResponse create(@Valid @RequestBody CourseRequest request) {
        return courseService.create(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID")
    public CourseResponse getById(@PathVariable Long id) {
        return courseService.getById(id);
    }

    @GetMapping
    @Operation(summary = "List courses with optional filters: teacherId, credits")
    public List<CourseResponse> getAll(
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) Integer credits) {
        if (teacherId != null && credits != null) {
            return courseService.filterByTeacherAndCredits(teacherId, credits);
        } else if (teacherId != null) {
            return courseService.filterByTeacher(teacherId);
        } else if (credits != null) {
            return courseService.filterByCredits(credits);
        }
        return courseService.getAll();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update course")
    public CourseResponse update(@PathVariable Long id, @Valid @RequestBody CourseRequest request) {
        return courseService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete course")
    public void delete(@PathVariable Long id) {
        courseService.delete(id);
    }
}
