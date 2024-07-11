package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudent(long id) {
        if(studentRepository.findById(id).isEmpty()){
            throw new StudentNotFoundException(id);
        }
        return studentRepository.findById(id).get();
    }

    public Collection<Student> findAllStudentForAge(int age) {
        return studentRepository.findByAge(age);
    }

    public void updateStudent(long id, Student student) {
        if(studentRepository.findById(id).isEmpty()){
            throw new StudentNotFoundException(id);
        }
        studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        if(studentRepository.findById(id).isEmpty()){
            throw new StudentNotFoundException(id);
        }
        studentRepository.deleteById(id);
    }
}
