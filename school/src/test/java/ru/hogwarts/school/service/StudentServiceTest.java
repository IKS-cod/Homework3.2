package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    /*@Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

private final List<Student> students=List.of(
        new Student(1L,"Ivan",10),
        new Student(2L,"Petr",10),
        new Student(3L,"Oleg",10),
        new Student(4L,"Maks",15),
        new Student(5L,"Alex",20)
);
@BeforeEach
    public void beforeEach(long id){
    when(studentRepository.findById(id)).thenReturn(students.);
}*/
}