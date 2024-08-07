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
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerRestTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private FacultyController facultyController;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private StudentRepository studentRepository;
    @LocalServerPort
    private int port;
    private final Faker faker = new Faker();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @AfterEach
    void clear() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @Test
    void contextLoads() throws Exception {
        assertThat(facultyController).isNotNull();
    }

    @Test
    @DisplayName("Создание студента")
    public void createFacultyTest() {
        //data
        Faculty faculty = createFaculty();
        //test
        ResponseEntity<Faculty> forEntity = testRestTemplate
                .postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        //check
        Assertions.assertEquals(forEntity.getStatusCode(), HttpStatusCode.valueOf(200));
        Faculty created = forEntity.getBody();
        assertThat(created).isNotNull();
        assertThat(created).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(faculty);
        Optional<Faculty> fromDb = facultyRepository.findById(created.getId());
        assertThat(fromDb).isPresent();
        assertThat(fromDb.get())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(faculty);

    }

    @Test
    @DisplayName("Изменение факультета ")
    public void updateFacultyTest() {
        //data
        Faculty faculty = createFaculty();
        System.out.println(faculty);
        ResponseEntity<Faculty> forEntity = testRestTemplate
                .postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        Assertions.assertEquals(forEntity.getStatusCode(), HttpStatusCode.valueOf(200));
        Faculty created = forEntity.getBody();
        Optional<Faculty> fromDb = facultyRepository.findById(created.getId());
        System.out.println(fromDb);
        assertThat(fromDb).isPresent();
        assertThat(fromDb.get())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(faculty);
        Faculty newFaculty = new Faculty();
        newFaculty.setName(faker.harryPotter().house());
        newFaculty.setColor(faker.color().name());
        long id = created.getId();
        System.out.println(newFaculty);
        System.out.println(id);
        //test
        testRestTemplate.put("http://localhost:" + port + "/faculty/" + id, newFaculty);
        //check
        Optional<Faculty> fromDbNew = facultyRepository.findById(id);
        System.out.println(fromDbNew);
        assertThat(fromDbNew).isPresent();
        assertThat(fromDbNew.get())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(newFaculty);
    }

    @Test
    @DisplayName("Удаление факультета")
    public void deleteFaculty() {
        //date
        Faculty faculty = createFaculty();
        facultyRepository.save(faculty);
        long id = 0;
        Collection<Faculty> collection = facultyRepository.findAll();
        for (Faculty element : collection) {
            if (element != null) {
                if (element.getName().equals(faculty.getName())) {
                    id = element.getId();
                }
            }
        }
        assertThat(facultyRepository.findById(id)).isPresent();
        Assertions.assertEquals(facultyRepository.findById(id).get(), faculty);
        //test
        testRestTemplate.delete("http://localhost:" + port + "/faculty/" + id);
        //check
        assertThat(facultyRepository.findById(id)).isEmpty();
    }

    @Test
    @DisplayName("Запрос факультета по id")
    public void getFaculty() {
        //date
        Faculty faculty = createFaculty();
        facultyRepository.save(faculty);
        long id = 0;
        Collection<Faculty> collection = facultyRepository.findAll();
        for (Faculty element : collection) {
            if (element != null) {
                if (element.getName().equals(faculty.getName())) {
                    id = element.getId();
                }
            }
        }
        assertThat(facultyRepository.findById(id)).isPresent();
        Assertions.assertEquals(facultyRepository.findById(id).get(), faculty);
        assertThat(facultyRepository.findById(id)).isPresent();
        Assertions.assertEquals(facultyRepository.findById(id).get(), faculty);
        //test
        ResponseEntity<Faculty> facultyNew = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/" + id, Faculty.class);
        //check
        Assertions.assertEquals(facultyNew.getBody(), faculty);
    }

    @Test
    @DisplayName("Запрос факультета по id которого нет")
    public void getFacultyNegative() {
        //date
        List<Faculty> facultyList = facultyRepository.findAll();
        assertThat(facultyList).isEmpty();
        //test
        long id = 0;
        ResponseEntity<String> facultyNew = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/faculty/" + id, String.class);
        //check
        assertThat(facultyNew.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(facultyNew.getBody()).isEqualTo("Факультет с id = %d не найден!".formatted(id));
    }

    @Test
    @DisplayName("Запрос факультета по цвету")
    public void findAllByColor() {
        Faculty faculty1 = createFaculty();
        Faculty faculty2 = createFaculty();
        Faculty faculty3 = createFaculty();
        Faculty faculty4 = createFaculty();
        Faculty faculty5 = createFaculty();
        List<Faculty> faculties = new ArrayList<>();
        faculties.add(faculty1);
        faculties.add(faculty2);
        faculties.add(faculty3);
        faculties.add(faculty4);
        faculties.add(faculty5);
        facultyRepository.save(faculty1);
        facultyRepository.save(faculty2);
        facultyRepository.save(faculty3);
        facultyRepository.save(faculty4);
        facultyRepository.save(faculty5);
        Assertions.assertEquals(facultyRepository.count(), 5);
        List<Faculty> facultiesNew = new ArrayList<>();
        facultiesNew = facultyRepository.findAll();
        assertThat(facultiesNew)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(faculties);
        int countNew = 0;
        for (Faculty element : faculties) {
            if (element.getColor() != null) {
                if (element.getColor().equals(faculty2.getColor())) {
                    countNew++;
                }
            }
        }
        System.out.println(countNew);
        //test
        String color = faculty2.getColor();
        System.out.println(color);
        ResponseEntity<Collection<Faculty>> facultyCollection =
                testRestTemplate.exchange("http://localhost:" + port + "/faculty?color=" + color,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });
        System.out.println(facultyCollection);
        int count = 0;
        Collection<Faculty> facultyListNew = facultyCollection.getBody();
        System.out.println(facultyListNew);
        for (Faculty element : facultyListNew) {
            if (element.getColor() != null) {
                if (element.getColor().equals(faculty2.getColor())) {
                    count++;
                }
            }
        }
        System.out.println(count);
        //check
        Assertions.assertEquals(facultyCollection.getStatusCode(), HttpStatusCode.valueOf(200));
        Assertions.assertEquals(count, countNew);
    }

   /* @Test
    @DisplayName("Запрос факультета по цвету которого нет")
    public void findAllByColorNegative() {
        //data
        List<Faculty> faculties = facultyRepository.findAll();
        assertThat(faculties).isEmpty();
        //test
        String color = "test";
        ResponseEntity<String> facultyNew = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/faculty?color=" + color, String.class);
        //check
        assertThat(facultyNew.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        System.out.println(facultyNew.getBody());
        assertThat(facultyNew.getBody()).isEqualTo("Факультет с color = %s не найден!".formatted(color));
    }*/

    @Test
    @DisplayName("Запрос по имени или цвету параметром Цвет")
    public void findByNameIgnoreCaseOrColorIgnoreCaseWithColor() {
        Faculty faculty1 = createFaculty();
        Faculty faculty2 = createFaculty();
        Faculty faculty3 = createFaculty();
        Faculty faculty4 = createFaculty();
        Faculty faculty5 = createFaculty();
        List<Faculty> faculties = new ArrayList<>();
        faculties.add(faculty1);
        faculties.add(faculty2);
        faculties.add(faculty3);
        faculties.add(faculty4);
        faculties.add(faculty5);
        facultyRepository.save(faculty1);
        facultyRepository.save(faculty2);
        facultyRepository.save(faculty3);
        facultyRepository.save(faculty4);
        facultyRepository.save(faculty5);
        Assertions.assertEquals(facultyRepository.count(), 5);
        List<Faculty> facultiesNew = new ArrayList<>();
        facultiesNew = facultyRepository.findAll();
        assertThat(facultiesNew)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(faculties);
        int countNew = 0;
        for (Faculty element : faculties) {
            if (element.getColor() != null) {
                if (element.getColor().equals(faculty2.getColor())) {
                    countNew++;
                }
            }
        }
        System.out.println(countNew);
        //test
        String nameOrColor = faculty2.getColor();
        System.out.println(nameOrColor);
        ResponseEntity<Collection<Faculty>> facultyCollection =
                testRestTemplate.exchange("http://localhost:" + port + "/faculty?nameOrColor=" + nameOrColor,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });
        System.out.println(facultyCollection);
        int count = 0;
        Collection<Faculty> facultyListNew = facultyCollection.getBody();
        System.out.println(facultyListNew);
        for (Faculty element : facultyListNew) {
            if (element.getColor() != null) {
                if (element.getColor().equals(faculty2.getColor())) {
                    count++;
                }
            }
        }
        System.out.println(count);
        //check
        Assertions.assertEquals(facultyCollection.getStatusCode(), HttpStatusCode.valueOf(200));
        Assertions.assertEquals(count, countNew);

    }

    /*@Test
    @DisplayName("Запрос по имени или цвету параметром Цвет которого нет")
    public void findByNameIgnoreCaseOrColorIgnoreCaseWithColorNegative() {
        //data
        List<Faculty> faculties = facultyRepository.findAll();
        assertThat(faculties).isEmpty();
        //test
        String nameOrColor = "test";
        ResponseEntity<String> facultyNew = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/faculty?nameOrColor=" + nameOrColor, String.class);
        //check
        assertThat(facultyNew.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        System.out.println(facultyNew.getBody());
        //  assertThat(facultyNew.getBody()).isEqualTo("Факультет с color или name = %s не найден!".formatted(nameOrColor));
    }*/

    @Test
    @DisplayName("Запрос по имени или цвету параметром Имя")
    public void findByNameIgnoreCaseOrColorIgnoreCaseWithName() {
        Faculty faculty1 = createFaculty();
        Faculty faculty2 = createFaculty();
        Faculty faculty3 = createFaculty();
        Faculty faculty4 = createFaculty();
        Faculty faculty5 = createFaculty();
        List<Faculty> faculties = new ArrayList<>();
        faculties.add(faculty1);
        faculties.add(faculty2);
        faculties.add(faculty3);
        faculties.add(faculty4);
        faculties.add(faculty5);
        facultyRepository.save(faculty1);
        facultyRepository.save(faculty2);
        facultyRepository.save(faculty3);
        facultyRepository.save(faculty4);
        facultyRepository.save(faculty5);
        Assertions.assertEquals(facultyRepository.count(), 5);
        List<Faculty> facultiesNew = new ArrayList<>();
        facultiesNew = facultyRepository.findAll();
        assertThat(facultiesNew)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(faculties);
        int countNew = 0;
        for (Faculty element : faculties) {
            if (element.getName() != null) {
                if (element.getName().equals(faculty2.getName())) {
                    countNew++;
                }
            }
        }
        System.out.println(countNew);
        //test
        String nameOrColor = faculty2.getName();
        System.out.println(nameOrColor);
        ResponseEntity<Collection<Faculty>> facultyCollection =
                testRestTemplate.exchange("http://localhost:" + port + "/faculty?nameOrColor=" + nameOrColor,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });
        System.out.println(facultyCollection);
        int count = 0;
        Collection<Faculty> facultyListNew = facultyCollection.getBody();
        System.out.println(facultyListNew);
        for (Faculty element : facultyListNew) {
            if (element.getName() != null) {
                if (element.getName().equals(faculty2.getName())) {
                    count++;
                }
            }
        }
        System.out.println(count);
        //check
        Assertions.assertEquals(facultyCollection.getStatusCode(), HttpStatusCode.valueOf(200));
        Assertions.assertEquals(count, countNew);
    }

    @Test
    @DisplayName("Студенты по id факультета")
    public void findStudentsByFacultyId() {
        //data
        Faculty faculty1 = createFaculty();
        facultyRepository.save(faculty1);

        Student student1 = createStudent(faculty1);
        Student student2 = createStudent(faculty1);
        Student student3 = createStudent(faculty1);
        Student student4 = createStudent(faculty1);
        Student student5 = createStudent(faculty1);
        List<Student> studentList = new ArrayList<>();
        studentList.add(student1);
        studentList.add(student2);
        studentList.add(student3);
        studentList.add(student4);
        studentList.add(student5);

        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);
        studentRepository.save(student4);
        studentRepository.save(student5);

        Assertions.assertEquals(studentRepository.count(), 5);
        Assertions.assertEquals(1, facultyRepository.count());

        //test
        long id = student1.getFaculty().getId();
        ResponseEntity<List<Student>> studentCollection =
                testRestTemplate.exchange("http://localhost:" + port + "/faculty/" + id + "/students",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        });
        List<Student> students = studentCollection.getBody();
        System.out.println(students);
        //check
        Assertions.assertEquals(studentCollection.getStatusCode(), HttpStatusCode.valueOf(200));
        assertThat(students).isEqualTo(studentList);
    }

    private Faculty createFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName(faker.harryPotter().house());
        faculty.setColor(faker.color().name());
        return faculty;
    }

    private Student createStudent(Faculty faculty) {
        Student student = new Student();
        student.setName(faker.harryPotter().character());
        student.setFaculty(faculty);
        student.setAge(faker.random().nextInt(10, 20));
        return student;
    }
}
