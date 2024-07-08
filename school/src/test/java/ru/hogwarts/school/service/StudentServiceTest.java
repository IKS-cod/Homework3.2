package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {
    private final StudentService studentService = new StudentService();

    @Test
    void createStudentTest() {
        assertThatExceptionOfType(StudentNotFoundException.class).isThrownBy(() -> studentService.get(1L));
        List<Student> studentList = new ArrayList<>();
        Student student1 = new Student(0L, "Ivan", 10);
        Student student2 = new Student(0L, "Oleg", 5);
        Student student3 = new Student(0L, "Sergei", 20);
        studentList.add(student1);
        studentList.add(student2);
        studentList.add(student3);
        studentService.createStudent(student1);
        studentService.createStudent(student2);
        studentService.createStudent(student3);
        Student student = studentService.get(1L);
        System.out.println(student);
        boolean flag = false;
        for (Student s : studentList) {
            if (s.equals(student)) {
                System.out.println(s.equals(student));
                flag = true;
                break;
            }
        }
        assertThat(flag).isEqualTo(true);
    }

    @Test
    void getTest() {
        assertThatExceptionOfType(StudentNotFoundException.class).isThrownBy(() -> studentService.get(1L));
        List<Student> studentList = new ArrayList<>();
        Student student1 = new Student(0L, "Ivan", 10);
        Student student2 = new Student(0L, "Oleg", 5);
        Student student3 = new Student(0L, "Sergei", 20);
        studentList.add(student1);
        studentList.add(student2);
        studentList.add(student3);
        studentService.createStudent(student1);
        studentService.createStudent(student2);
        studentService.createStudent(student3);
        Student student = studentService.get(1L);
        System.out.println(student);
        boolean flag = false;
        for (Student s : studentList) {
            if (s.equals(student)) {
                System.out.println(s.equals(student));
                flag = true;
                break;
            }
        }
        assertThat(flag).isEqualTo(true);
    }

    @Test
    void findAllStudentForAgeTest() {
        Student student1 = new Student(0L, "Ivan", 10);
        Student student2 = new Student(0L, "Oleg", 10);
        Student student3 = new Student(0L, "Sergei", 20);
        studentService.createStudent(student1);
        studentService.createStudent(student2);
        studentService.createStudent(student3);
        assertThat(studentService.findAllStudentForAge(10).size()).isEqualTo(2);
        System.out.println(studentService.findAllStudentForAge(10));
    }

    @Test
    void editStudentTest() {
        assertThatExceptionOfType(StudentNotFoundException.class).isThrownBy(() -> studentService.get(1L));
        List<Student> studentList = new ArrayList<>();
        Student student1 = new Student(0L, "Ivan", 10);
        Student student2 = new Student(0L, "Oleg", 5);
        Student student3 = new Student(0L, "Sergei", 20);
        studentList.add(student1);
        studentList.add(student2);
        studentList.add(student3);
        studentService.createStudent(student1);
        studentService.createStudent(student2);
        studentService.createStudent(student3);
        Student student = studentService.get(1L);
        System.out.println(student);
        boolean flag = false;
        for (Student s : studentList) {
            if (s.equals(student)) {
                System.out.println(s.equals(student));
                flag = true;
                break;
            }
        }
        System.out.println("flag " + flag);
        assertThat(flag).isEqualTo(true);
        Student student4 = new Student(1L, "New", 50);
        studentService.editStudent(1, student4);
        assertThat(studentService.get(1L).equals(student4)).isEqualTo(true);
        System.out.println(studentService.get(1L));

    }

    @Test
    void deleteStudentTest() {
        assertThatExceptionOfType(StudentNotFoundException.class).isThrownBy(() -> studentService.get(1L));
        List<Student> studentList = new ArrayList<>();
        Student student1 = new Student(0L, "Ivan", 10);
        Student student2 = new Student(0L, "Oleg", 5);
        Student student3 = new Student(0L, "Sergei", 20);
        studentList.add(student1);
        studentList.add(student2);
        studentList.add(student3);
        studentService.createStudent(student1);
        studentService.createStudent(student2);
        studentService.createStudent(student3);
        Student student = studentService.get(1L);
        System.out.println(student);
        boolean flag = false;
        for (Student s : studentList) {
            if (s.equals(student)) {
                System.out.println(s.equals(student));
                flag = true;
                break;
            }
        }
        assertThat(flag).isEqualTo(true);
        studentService.deleteStudent(1L);
        assertThatExceptionOfType(StudentNotFoundException.class).isThrownBy(() -> studentService.get(1L));

    }
}