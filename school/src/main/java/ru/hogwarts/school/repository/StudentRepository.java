package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAge(int age);

    Collection<Student> findByAgeBetween(int minAge, int maxAge);

    List<Student> findAllByFaculty_Id(long facultyId);
    @Query(value = "SELECT count(*) AS count FROM student", nativeQuery = true)
    long getCountStudents();

    @Query(value = "SELECT AVG(age) AS avg FROM student", nativeQuery = true)
    double getAvgAgeStudents();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> getDescFiveStudents();
    @Query(value = "SELECT name FROM student LIMIT 5", nativeQuery = true)
    List<String> getFirstSixStudents();


}
