package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.*;

@Service
public class StudentService {
    private final Map<Long, Student> students = new HashMap<>();
    private long countId = 0;

    public Student createStudent(Student student) {
        student.setId(++countId);
        students.put(countId, student);
        return student;
    }

    public Student get(long id) {
        if (!students.containsKey(id)) {
            throw new StudentNotFoundException(id);
        }
        return students.get(id);
    }

    public Collection<Student> findAllStudentForAge(int age) {
        List<Student> studentList = new ArrayList<>();
        for (Student student : students.values()) {
            if (student.getAge() == age) {
                studentList.add(student);
            }
        }
        return Collections.unmodifiableList(studentList);
    }

    public void editStudent(long id, Student student) {
        if (!students.containsKey(id)) {
            throw new StudentNotFoundException(id);
        }
        Student oldStudent = students.get(id);
        oldStudent.setName(student.getName());
        oldStudent.setAge(student.getAge());
    }

    public Student deleteStudent(long id) {
        if (!students.containsKey(id)) {
            throw new StudentNotFoundException(id);
        }
       return students.remove(id);
    }
}
