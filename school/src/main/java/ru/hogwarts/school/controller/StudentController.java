package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/age")//(params = "age")
    public Collection<Student> getAllStudentForAge(@RequestParam int age) {
        return studentService.findAllStudentForAge(age);
    }

    @GetMapping("/minAge_maxAge")//(params = {"minAge", "maxAge"})
    public Collection<Student> findStudentByAgeBetween(@RequestParam int minAge, @RequestParam int maxAge) {
        return studentService.findStudentByAgeBetween(minAge, maxAge);
    }

    @GetMapping("/{id}") //http:localhost:8080/student/id
    public Student getStudent(@PathVariable long id) {
        return studentService.getStudent(id);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping("/{id}")
    public void updateStudent(@PathVariable long id, @RequestBody Student student) {
        studentService.updateStudent(id, student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping("/{id}/faculty")
    public Faculty findStudentsFaculty(@PathVariable long id){
        return studentService.findStudentsFaculty(id);
    }
}
