package ru.hogwarts.school.controller.mvc;

import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private FacultyRepository facultyRepository;
    @MockBean
    private AvatarService avatarService;
    @SpyBean
    private StudentService studentService;
    private final Faker faker = new Faker();

    @Test
    @DisplayName("Запрос студента по age")
    void getStudentAge() throws Exception {

        Student student1 = new Student();
        student1.setId(1L);
        student1.setAge(10);
        student1.setName(faker.harryPotter().character());

        Student student2 = new Student();
        student1.setId(2L);
        student1.setAge(10);
        student1.setName(faker.harryPotter().character());

        when(studentRepository.findByAge(10)).thenReturn(Arrays.asList(student1, student2));
        mockMvc.perform(get("/student/age?age=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("Найти студентов в диапазоне min/max age")
    void findStudentByAgeBetween() throws Exception {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setAge(15);
        student1.setName(faker.harryPotter().character());

        Student student2 = new Student();
        student1.setId(2L);
        student1.setAge(18);
        student1.setName(faker.harryPotter().character());

        when(studentRepository.findByAgeBetween(10, 20)).thenReturn(Arrays.asList(student1, student2));
        mockMvc.perform(get("/student/minAge_maxAge?minAge=10&&maxAge=20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

    }

    @Test
    @DisplayName("Найти студента по id")
    void getStudent() throws Exception {
        long id = 1L;
        Student student1 = new Student();
        student1.setId(id);
        student1.setAge(15);
        student1.setName(faker.harryPotter().character());
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));
        mockMvc.perform(get("/student/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(student1.getName()))
                .andExpect(jsonPath("$.age").value(student1.getAge()));

    }
    @Test
    @DisplayName("Найти студента по id которого нет")
    void getStudentNegative() throws Exception {
        long id = 1;
        when(studentRepository.findById(id)).thenThrow(StudentNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/{id}", id))
                .andExpect(result -> assertInstanceOf(StudentNotFoundException.class, result.getResolvedException()));
    }

    @Test
    @DisplayName("Создать студента")
    void createStudent() throws Exception {
        Student student1 = new Student(null, "Ivan", 30);
        when(studentRepository.save(any())).thenReturn(student1);
        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(student1.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(student1.getName()))
                .andExpect(jsonPath("$.age").value(student1.getAge()));
    }

    @Test
    @DisplayName("Заменить студента по id")
    void updateStudent() throws Exception {
        //data
        long id = 1L;
        Faculty faculty = new Faculty();
        faculty.setColor("red");
        faculty.setName(faker.harryPotter().house());
        faculty.setId(id);

        Student student1 = new Student();
        student1.setId(id);
        student1.setAge(10);
        student1.setName(faker.harryPotter().character());
        student1.setFaculty(faculty);

        Student student2 = new Student();
        student2.setId(id);
        student2.setAge(12);
        student2.setName(faker.harryPotter().character());
        student2.setFaculty(faculty);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student1));

        //test, check
        mockMvc.perform(put("/student/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(student2.toString()))
                .andExpect(status().isOk());
        verify(studentRepository, times(1)).save(any());

    }
    @Test
    @DisplayName("Заменить студента по id проверка исключений StudentNotFoundException")
    void updateStudentNegative1() throws Exception {
        //data
        long id = 1L;
        Faculty faculty = new Faculty();
        faculty.setColor("red");
        faculty.setName(faker.harryPotter().house());
        faculty.setId(id);

        Student student1 = new Student();
        student1.setId(id);
        student1.setAge(10);
        student1.setName(faker.harryPotter().character());
        student1.setFaculty(faculty);

        Student student2 = new Student();
        student2.setId(id);
        student2.setAge(12);
        student2.setName(faker.harryPotter().character());
        student2.setFaculty(faculty);
        when(studentRepository.findById(id)).thenThrow(StudentNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(student2.toString()))
                .andExpect(result -> assertInstanceOf(StudentNotFoundException.class, result.getResolvedException()));
    }
    @Test
    @DisplayName("Заменить студента по id проверка исключений FacultyNotFoundException")
    void updateStudentNegative2() throws Exception {
        //data
        long id = 1L;
        Faculty faculty = new Faculty();
        faculty.setColor("red");
        faculty.setName(faker.harryPotter().house());
        faculty.setId(id);

        Student student1 = new Student();
        student1.setId(id);
        student1.setAge(10);
        student1.setName(faker.harryPotter().character());
        student1.setFaculty(faculty);

        Student student2 = new Student();
        student2.setId(id);
        student2.setAge(12);
        student2.setName(faker.harryPotter().character());
        student2.setFaculty(faculty);
        when(studentRepository.findById(id)).thenReturn(Optional.of(student1));
        when(facultyRepository.findById(id)).thenThrow(FacultyNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(student2.toString()))
                .andExpect(result -> assertInstanceOf(FacultyNotFoundException.class, result.getResolvedException()));
    }
    @Test
    @DisplayName("Удалить студента по id")
    void deleteStudent() throws Exception {
        //data
        long id = 1L;
        Student student1 = new Student();
        student1.setId(id);
        student1.setAge(10);
        student1.setName(faker.harryPotter().character());

        when(studentRepository.existsById(any())).thenReturn(true);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));
        //test, check
        mockMvc.perform(delete("/student/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(studentRepository, times(1)).deleteById(any());

    }
    @Test
    @DisplayName("Негативный тест удалить студента по id которого нет")
    void deleteStudentNegative() throws Exception {
        long id = 1;
        when(studentRepository.existsById(id)).thenThrow(StudentNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/{id}", id))
                .andExpect(result -> assertInstanceOf(StudentNotFoundException.class, result.getResolvedException()));
    }

    @Test
    @DisplayName("Найти факультет по id студента")
    void findStudentsFaculty() throws Exception {
        //data
        long id = 1L;
        String name = faker.harryPotter().house();
        String color = faker.color().name();
        Faculty faculty = new Faculty();
        faculty.setColor(color);
        faculty.setName(name);
        faculty.setId(id);
        Student student1 = new Student();
        student1.setId(id);
        student1.setAge(10);
        student1.setName(faker.harryPotter().character());
        student1.setFaculty(faculty);

        when(studentRepository.existsById(any())).thenReturn(true);
        when(studentRepository.findById(id)).thenReturn(Optional.of(student1));
        //test, check
        mockMvc.perform(get("/student/" + id + "/faculty")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }


}
