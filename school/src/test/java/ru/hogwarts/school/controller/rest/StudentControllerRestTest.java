package ru.hogwarts.school.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerRestTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private StudentController studentController;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @LocalServerPort
    private int port;
    private final Faker faker = new Faker();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @AfterEach
    void clear() {
        studentRepository.deleteAll();
    }

    @Test
    void contextLoads() throws Exception {
        assertThat(studentController).isNotNull();
    }

    @Test
    @DisplayName("Запрос студентов по возрасту")
    void getAllStudentForAge() {
        //data
        Faculty faculty = createFaculty();
        Student student1 = createStudent();
        student1.setFaculty(faculty);
        studentRepository.save(student1);
        Student student2 = createStudent();
        student2.setFaculty(faculty);
        studentRepository.save(student2);
        Student student3 = createStudent();
        student3.setFaculty(faculty);
        studentRepository.save(student3);
        int age = student2.getAge();
        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        students.add(student3);
        int count = 0;
        for (Student el : students) {
            if (el != null) {
                if (el.getAge() == age) {
                    count++;
                }
            }
        }
        System.out.println(count);
        List<Student> actual = studentRepository.findAll();
        Assertions.assertEquals(students, actual);
        //test
        ResponseEntity<List<Student>> forEntity =
                testRestTemplate.exchange("http://localhost:" + port + "/student/age?age=" + age,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });
        List<Student> studentList = forEntity.getBody();
        int countNew = 0;
        for (Student el : studentList) {
            if (el != null) {
                if (el.getAge() == age) {
                    countNew++;
                }
            }
        }
        System.out.println(countNew);
        //check
        Assertions.assertEquals(count, countNew);
    }

    @Test
    @DisplayName("Запрос студентов по min/max возрасту")
    public void findStudentByAgeBetween() {
        int minAge = faker.random().nextInt(10, 15);
        int maxAge = faker.random().nextInt(minAge, 60);

        Faculty faculty = createFaculty();
        Student student1 = createStudent();
        student1.setFaculty(faculty);
        studentRepository.save(student1);
        Student student2 = createStudent();
        student2.setFaculty(faculty);
        studentRepository.save(student2);
        Student student3 = createStudent();
        student3.setFaculty(faculty);
        studentRepository.save(student3);
        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        students.add(student3);
        System.out.println(students);
        List<Student> studentsNew = new ArrayList<>();
        for (Student el : students) {
            if (el != null) {
                if (el.getAge() >= minAge && el.getAge() <= maxAge) {
                    studentsNew.add(el);
                }
            }
        }
        ResponseEntity<List<Student>> listResponseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/student/minAge_maxAge?minAge=" + minAge + "&&maxAge=" + maxAge,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        List<Student> studentList = listResponseEntity.getBody();
        //check
        Assertions.assertEquals(studentsNew, studentList);
        System.out.println(studentsNew);
        System.out.println(studentList);

    }

    @Test
    @DisplayName("Запрос студента по id")
    public void getStudent() {
        //data
        assertThat(studentRepository.findAll()).isEmpty();
        Faculty faculty = createFaculty();
        Student student1 = createStudent();
        student1.setFaculty(faculty);
        Student student2 = studentRepository.save(student1);
        assertThat(studentRepository.findAll()).isNotEmpty();
        long id = student2.getId();
        //test
        ResponseEntity<Student> responseEntity = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/student/" + id,
                Student.class);
        //check
        assertThat(responseEntity.getBody()).isEqualTo(student1);
    }

    @Test
    @DisplayName("Запрос студента по id которого нет")
    public void getStudentNegative() {
        //data
        assertThat(studentRepository.findAll()).isEmpty();
        long id = 0;
        //test
        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/student/" + id,
                String.class);
        //check
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("Студент с id = %d не найден!".formatted(id));
    }

    @Test
    @DisplayName("Создание студента")
    void createStudentTest() {
        //data
        Faculty faculty = createFaculty();
        Student student1 = createStudent();
        student1.setFaculty(faculty);
        //test
        ResponseEntity<Student> forEntity = testRestTemplate.postForEntity(
                "http://localhost:" + port + "/student",
                student1,
                Student.class);
        //check
        System.out.println(forEntity);
        Assertions.assertEquals(forEntity.getStatusCode(), HttpStatusCode.valueOf(200));
        Student actual = forEntity.getBody();
        long id = actual.getId();
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(student1);
        Student studentInBase = studentRepository.findById(actual.getId()).orElseThrow();
        assertThat(studentInBase)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(student1);

    }

    @Test
    @DisplayName("Создание студента с отсутствующим факультетом")
    void createStudentNegativeTest() {
        //data
        Faculty faculty = new Faculty();
        faculty.setId(-1L);
        Student student1 = createStudent();
        student1.setFaculty(faculty);
        //test
        ResponseEntity<String> forEntity = testRestTemplate.postForEntity(
                "http://localhost:" + port + "/student",
                student1,
                String.class);
        //check
        assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(forEntity.getBody()).isEqualTo("Факультет с id = %d не найден!".formatted(-1));
    }

    @Test
    @DisplayName("Изменение студента")
    void updateStudent() {
        //data
        Faculty faculty1 = createFaculty();
        Student student1 = createStudent();
        student1.setFaculty(faculty1);
        Student student2 = studentRepository.save(student1);
        long id = student2.getId();
        Student student3 = createStudent();
        student3.setFaculty(faculty1);
        //test
        testRestTemplate.put("http://localhost:" + port + "/student/" + id,
                student3);
        //check
        Optional<Student> actual = studentRepository.findById(id);
        Student actualNew = actual.orElseThrow();
        assertThat(actualNew)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(student3);
    }

    @Test
    @DisplayName("Удаление студента по id")
    void deleteStudent() {
        //data
        assertThat(studentRepository.findAll()).isEmpty();
        Faculty faculty1 = createFaculty();
        Student student1 = createStudent();
        student1.setFaculty(faculty1);
        Student student2 = studentRepository.save(student1);
        long id = student2.getId();
        Student studentInBase = studentRepository.findById(student2.getId()).orElseThrow();
        Assertions.assertEquals(student1, studentInBase);
        //test
        testRestTemplate.delete("http://localhost:" + port + "/student/" + id);
        //check
        assertThat(studentRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("Нахождение факультетов по id студента")
    void findStudentsFaculty() {
        //data
        assertThat(studentRepository.findAll()).isEmpty();
        Faculty faculty1 = createFaculty();
        Student student1 = createStudent();
        student1.setFaculty(faculty1);
        Student student2 = studentRepository.save(student1);
        long id = student2.getId();
        Student studentInBase = studentRepository.findById(student2.getId()).orElseThrow();
        Assertions.assertEquals(student1, studentInBase);
        //test
        ResponseEntity<Faculty> facultyResponseEntity = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/student/" + id + "/faculty",
                Faculty.class);
        Faculty faculty = facultyResponseEntity.getBody();
        Faculty facultyInBase = studentRepository.findById(id).get().getFaculty();
        //check
        Assertions.assertEquals(facultyResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));
        Assertions.assertEquals(faculty1,faculty);
        Assertions.assertEquals(faculty1,facultyInBase);
    }

    private Student createStudent() {
        Student student = new Student();
        student.setName(faker.name().name());
        student.setAge(faker.random().nextInt(20, 50));
        return student;
    }

    private Faculty createFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName(faker.harryPotter().house());
        faculty.setColor(faker.color().name());
        return facultyRepository.save(faculty);
    }
    @Test
    @DisplayName("Запрос в БД на количество студентов")
    void getCountStudents() {
        //data
        Faculty faculty = createFaculty();
        Student student1 = createStudent();
        student1.setFaculty(faculty);
        studentRepository.save(student1);
        Student student2 = createStudent();
        student2.setFaculty(faculty);
        studentRepository.save(student2);
        Student student3 = createStudent();
        student3.setFaculty(faculty);
        studentRepository.save(student3);
        //test
        ResponseEntity <Long> listResponseEntity = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/student/count", Long.class);
        //check
        Assertions.assertEquals(listResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));
        long actual = listResponseEntity.getBody();
        System.out.println(actual);
        assertThat(actual).isEqualTo(3);
    }
    @Test
    @DisplayName("Запрос в БД на средний возраст студентов")
    void getAvgAgeStudents() {
        Faculty faculty = createFaculty();
        Student student1 = new Student(1L, "Test1", 10);
        student1.setFaculty(faculty);
        studentRepository.save(student1);
        Student student2 = new Student(1L, "Test2", 10);
        student2.setFaculty(faculty);
        studentRepository.save(student2);
        Student student3 = new Student(1L, "Test3", 10);
        student3.setFaculty(faculty);
        studentRepository.save(student3);
        double expected = (double) (student1.getAge() + student2.getAge() + student3.getAge()) /3;
        //test
        ResponseEntity <Double> listResponseEntity = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/student/age-avg", Double.class);
        //check
        Assertions.assertEquals(listResponseEntity.getStatusCode(), HttpStatusCode.valueOf(200));
        double actual = listResponseEntity.getBody();
        System.out.println(actual);
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    @DisplayName("Запрос в БД на список последних 5 студентов")
    void getDescFiveStudents() {
        //data
        Faculty faculty = createFaculty();
        Student student1 = createStudent();
        student1.setFaculty(faculty);
        studentRepository.save(student1);
        Student student2 = createStudent();
        student2.setFaculty(faculty);
        studentRepository.save(student2);
        Student student3 = createStudent();
        student3.setFaculty(faculty);
        studentRepository.save(student3);
        Student student4 = createStudent();
        student4.setFaculty(faculty);
        studentRepository.save(student4);
        Student student5 = createStudent();
        student5.setFaculty(faculty);
        studentRepository.save(student5);
        Student student6 = createStudent();
        student6.setFaculty(faculty);
        studentRepository.save(student6);
        List<Student> studentList =new ArrayList<>();
        studentList.add(student2);
        studentList.add(student3);
        studentList.add(student4);
        studentList.add(student5);
        studentList.add(student6);
        System.out.println(studentList);
        //test
        ResponseEntity<List<Student>> listResponseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/student/desc-five",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        List<Student> studentListNew = listResponseEntity.getBody();
        studentList.sort(Comparator.comparing(Student::getId));
        studentListNew.sort(Comparator.comparing(Student::getId));
        System.out.println(studentListNew);
        //check
        assertThat(studentListNew).isEqualTo(studentList);

    }
}
