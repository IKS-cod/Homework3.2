package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("/{id}") //http:localhost:8080/student/id
    public Faculty getFaculty(@PathVariable Long id) {
        return facultyService.getFaculty(id);
    }
    @GetMapping(params = "color")
    public Collection<Faculty> findAllByColor(@RequestParam/*(required = false) */String color){
        return facultyService.findAllByColor(color);
    }
    @GetMapping(params = "nameOrColor")
    public Collection<Faculty> findByNameIgnoreCaseOrColorIgnoreCase(@RequestParam/*(required = false)*/ String nameOrColor){
        return facultyService.findByNameOrColor(nameOrColor);
    }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @PutMapping("/{id}")
    public void updateFaculty(@PathVariable long id, @RequestBody Faculty faculty) {
        facultyService.updateFaculty(id, faculty);
    }

    @DeleteMapping("/{id}")
    public void deleteFaculty(@PathVariable long id) {
        facultyService.deleteFaculty(id);
    }

    @GetMapping("/{id}/students")
    public List<Student> findStudentsByFacultyId(@PathVariable long id) {
        return facultyService.findStudentsByFacultyId(id);
    }
}
