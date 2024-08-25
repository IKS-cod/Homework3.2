package ru.hogwarts.school.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    FacultyRepository facultyRepository;
    @Mock
    StudentRepository studentRepository;
    @InjectMocks
    StudentService studentService;

    @Test//StudentService
    public void createStudentNegativeTest() {
        Faculty faculty = new Faculty(1L, "RRR", "red");
        Student student = new Student(1L, "Alex", 10);
        student.setFaculty(faculty);
        when(facultyRepository.findById(student.getFaculty().getId())).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(FacultyNotFoundException.class)
                .isThrownBy(() -> studentService.createStudent(student));
    }

    @Test//StudentService
    public void getStudentNegativeTest() {
        long id = 1L;
        when(studentRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(StudentNotFoundException.class)
                .isThrownBy(() -> studentService.getStudent(id));
    }

    @Test//StudentService
    public void updateStudentNegativeTest() {
        long id = 1L;
        when(studentRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(StudentNotFoundException.class)
                .isThrownBy(() -> studentService.getStudent(id));
        Faculty faculty = new Faculty(1L, "RRR", "red");
        Student student = new Student(1L, "Alex", 10);
        student.setFaculty(faculty);
        when(facultyRepository.findById(student.getFaculty().getId())).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(FacultyNotFoundException.class)
                .isThrownBy(() -> studentService.createStudent(student));
    }

    @Test//StudentService
    public void deleteStudentNegativeTest() {
        long id = 1L;
        when(studentRepository.existsById(id)).thenReturn(false);
        Assertions.assertThatExceptionOfType(StudentNotFoundException.class)
                .isThrownBy(() -> studentService.deleteStudent(id));
    }
}