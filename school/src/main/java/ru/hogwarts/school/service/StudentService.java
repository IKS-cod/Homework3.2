package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;
@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student createStudent(Student student) {
        Faculty faculty = null;
        if (student.getFaculty() != null && student.getFaculty().getId() != null) {
            faculty = facultyRepository.findById(student.getFaculty().getId())
                    .orElseThrow(() -> new FacultyNotFoundException(student.getFaculty().getId()));
        }
        student.setFaculty(faculty);
        student.setId(null);
        return studentRepository.save(student);
    }

    public Student getStudent(long id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    public Collection<Student> findAllStudentForAge(int age) {
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findStudentByAgeBetween(int minAge, int maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }


    public void updateStudent(long id, Student student) {
        Student oldStudent = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        Faculty faculty = null;
        if (student.getFaculty() != null && student.getFaculty().getId() != null) {
            faculty = facultyRepository.findById(student.getFaculty().getId())
                    .orElseThrow(() -> new FacultyNotFoundException(student.getFaculty().getId()));
        }
        oldStudent.setName(student.getName());
        oldStudent.setAge(student.getAge());
        oldStudent.setFaculty(faculty);
        studentRepository.save(oldStudent);
    }

    public void deleteStudent(long id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }
        studentRepository.deleteById(id);
    }

    public Faculty findStudentsFaculty(long id) {
        return getStudent(id).getFaculty();
    }
}
