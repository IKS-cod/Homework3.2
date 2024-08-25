package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    final Object object = new Object();

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method for \"createStudent\"");
        Faculty faculty = null;
        if (student.getFaculty() != null && student.getFaculty().getId() != null) {
            faculty = facultyRepository.findById(student.getFaculty().getId())
                    .orElseThrow(() -> {
                        logger.error("There is not faculty with id = " + student.getFaculty().getId());
                        return new FacultyNotFoundException(student.getFaculty().getId());
                    });
        }
        student.setFaculty(faculty);
        student.setId(null);
        logger.debug("Was transmitted \"student\"={} in repository from method \"createStudent\"", student);
        return studentRepository.save(student);
    }

    public Student getStudent(long id) {
        logger.info("Was invoked method for \"getStudent\"");
        logger.debug("Was request \"studentRepository.findById(id)\"={} in repository from method \"getStudent\"", id);
        return studentRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not student with id = " + id);
            return new StudentNotFoundException(id);
        });
    }

    public Collection<Student> findAllStudentForAge(int age) {
        logger.info("Was invoked method for \"findAllStudentForAge\"");
        logger.debug("Was request \"studentRepository.findByAge(age)\"={} " +
                "in repository from method \"getStudent\"", age);
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findStudentByAgeBetween(int minAge, int maxAge) {
        logger.info("Was invoked method for \"findStudentByAgeBetween\"");
        logger.info("Was request \"studentRepository.findByAgeBetween(minAge, maxAge)\"={},{} " +
                "in repository from method \"findStudentByAgeBetween\"", minAge, maxAge);
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }


    public void updateStudent(long id, Student student) {
        logger.info("Was invoked method for \"updateStudent\"");
        logger.debug("Was request \"studentRepository.deleteById(id)\"={} " +
                "in repository from method \"updateStudent\"", id);
        Student oldStudent = studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is not student with id = " + id);
                    return new StudentNotFoundException(id);
                });
        Faculty faculty = null;
        if (student.getFaculty() != null && student.getFaculty().getId() != null) {
            faculty = facultyRepository.findById(student.getFaculty().getId())
                    .orElseThrow(() -> {
                        logger.error("There is not faculty with id = " + student.getFaculty().getId());
                        return new FacultyNotFoundException(student.getFaculty().getId());
                    });
        }
        oldStudent.setName(student.getName());
        oldStudent.setAge(student.getAge());
        oldStudent.setFaculty(faculty);
        logger.debug("Was transmitted \"oldStudent\"={} in repository from method \"updateStudent\"", oldStudent);
        studentRepository.save(oldStudent);
    }

    public void deleteStudent(long id) {
        logger.info("Was invoked method for \"deleteStudent\"");
        if (!studentRepository.existsById(id)) {
            logger.error("There is not student with id = " + id);
            throw new StudentNotFoundException(id);
        }
        logger.debug("Was request \"studentRepository.deleteById(id)\"={} " +
                "in repository from method \"deleteStudent\"", id);
        studentRepository.deleteById(id);
    }

    public Faculty findStudentsFaculty(long id) {
        logger.info("Was invoked method for \"findStudentsFaculty\"");
        logger.debug("Was request \"getStudent(id).getFaculty()\"={} " +
                "in repository from method \"findStudentsFaculty\"", id);
        return getStudent(id).getFaculty();
    }

    public long getCountStudents() {
        logger.info("Was invoked method for \"getCountStudents\"");
        return studentRepository.getCountStudents();
    }

    public double getAvgAgeStudents() {
        logger.info("Was invoked method for \"getAvgAgeStudents\"");
        return studentRepository.getAvgAgeStudents();
    }

    public List<Student> getDescFiveStudents() {
        logger.info("Was invoked method for \"getDescFiveStudents\"");
        return studentRepository.getDescFiveStudents();
    }

    public List<String> getAllStudentWithNameOnLetterA() {
        return studentRepository.findAll().stream()
                .parallel()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(s -> s.startsWith("A"))
                .sorted()
                .collect(Collectors.toList());
    }

    public double getAgeMediumAllStudent() {
        return studentRepository.findAll().stream()
                .parallel()
                .collect(Collectors.averagingInt(Student::getAge));
    }

    public long getNumberTypeInt() {
        return Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .parallel()
                .reduce(0, (a, b) -> a + b);
    }

    int count = 0;

    public void getNameStudentsInParallelMethod() {
        List<String> namesFirstSixStudents = studentRepository.getFirstSixStudents();
        System.out.println(namesFirstSixStudents);
        printStudentsName(namesFirstSixStudents, 0);
        printStudentsName(namesFirstSixStudents, 1);

        new Thread(() -> {
            printStudentsName(namesFirstSixStudents, 2);
            printStudentsName(namesFirstSixStudents, 3);
        }).start();

        new Thread(() -> {
            printStudentsName(namesFirstSixStudents, 4);
            printStudentsName(namesFirstSixStudents, 5);

        }).start();
    }

    public void getNameStudentsInParallelMethodWithSynchronized() {
        List<String> namesFirstSixStudents = studentRepository.getFirstSixStudents();
        System.out.println(namesFirstSixStudents);
        printStudentsName(namesFirstSixStudents, 0);
        printStudentsName(namesFirstSixStudents, 1);

        new Thread(() -> {
            synchronized (object){
                printStudentsName(namesFirstSixStudents, 2);
                printStudentsName(namesFirstSixStudents, 3);}
        }).start();

        new Thread(() -> {
            synchronized (object){
                printStudentsName(namesFirstSixStudents, 4);
                printStudentsName(namesFirstSixStudents, 5);}
        }).start();
    }

    void printStudentsName(List<String> name, int id) {
        if (name.get(id) != null) {
            System.out.println(name.get(id) + ", " + count);
            count++;
        }
    }

    /*synchronized void printStudentsNameWithSynchronized(List<String> name, int id) {
        if (name.get(id) != null) {
            System.out.println(name.get(id) + ", " + count);
            count++;
        }
    }*/

}
