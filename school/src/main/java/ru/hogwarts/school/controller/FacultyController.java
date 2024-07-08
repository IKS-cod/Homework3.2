package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}") //http:localhost:8080/student/id
    public Faculty getFaculty(@PathVariable Long id) {
        return facultyService.get(id);
    }

    @GetMapping //http:localhost:8080/student/id
    public Collection<Faculty> findAllFacultyForColor(@RequestParam String color) {
        return facultyService.findAllFacultyForColor(color);
    }

    @PostMapping
    public Faculty createStudent(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @PutMapping
    public void editStudent(@RequestParam long id, @RequestBody Faculty faculty) {
        facultyService.editFaculty(id, faculty);
    }

    @DeleteMapping("{id}")
    public Faculty deleteFaculty(@PathVariable long id) {
        return facultyService.deleteFaculty(id);
    }


}
