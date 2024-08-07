package ru.hogwarts.school.controller.mvc;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FacultyController.class)
public class FacultyControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private FacultyRepository facultyRepository;
    /*@MockBean
    private AvatarService avatarService;*/

    @SpyBean
    private StudentService studentService;
    @SpyBean
    private FacultyService facultyService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Faker faker = new Faker();

    @Test
    @DisplayName("Запрос факультета по id")
    void getFaculty() throws Exception {
        //data
        long id = 1;
        String name = "RRR";
        String color = "red";

        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setId(id);
        faculty.setColor(color);

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("id", id);
        facultyObject.put("color", color);

        when(facultyRepository.findById(any())).thenReturn(Optional.of(faculty));
        //test
        //check
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.color").value(color));
        verify(facultyRepository, only()).findById(any());

    }

    @Test
    @DisplayName("Запрос факультета по id которого нет, выброс исключения")
    void getFacultyNegative() throws Exception {
        long id = 1;
        when(facultyRepository.findById(id)).thenThrow(FacultyNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/faculty/{id}", id))
                .andExpect(result -> assertInstanceOf(FacultyNotFoundException.class, result.getResolvedException()));
    }

    @Test
    @DisplayName("Запрос всех факультетов по цвету")
    void findAllByColor() throws Exception {
        //data
        long id1 = 1L;
        long id2 = 2L;
        long id3 = 3L;
        String name1 = "test1";
        String name2 = "test2";
        String name3 = "test3";
        String color = "red";

        Faculty faculty1 = new Faculty();
        faculty1.setName(name1);
        faculty1.setId(id1);
        faculty1.setColor(color);

        Faculty faculty2 = new Faculty();
        faculty2.setName(name2);
        faculty2.setId(id2);
        faculty2.setColor(color);

        Faculty faculty3 = new Faculty();
        faculty3.setName(name3);
        faculty3.setId(id3);
        faculty3.setColor(color);

        List<Faculty> facultyList = new ArrayList<>();
        facultyList.add(faculty1);
        facultyList.add(faculty2);
        facultyList.add(faculty3);
        when(facultyRepository.findAllByColor(any())).thenReturn(facultyList);
        //test
        //check
        System.out.println(facultyList);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?color=" + color)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].color").value(color))
                .andExpect(jsonPath("$[1].color").value(color))
                .andExpect(jsonPath("$[2].color").value(color));

        verify(facultyRepository, only()).findAllByColor(any());
    }

    @Test
    @DisplayName("Запрос факультета по имени или цвету(по цвету)")
    void findByNameIgnoreCaseOrColorIgnoreCaseForColor() throws Exception {
        //data
        long id1 = 1L;
        long id2 = 2L;
        long id3 = 3L;
        String name1 = "test1";
        String color = "red";
        String nameOrColor = "red";

        Faculty faculty1 = new Faculty();
        faculty1.setName(name1);
        faculty1.setId(id1);
        faculty1.setColor(color);

        Faculty faculty2 = new Faculty();
        faculty2.setName(name1);
        faculty2.setId(id2);
        faculty2.setColor(color);

        Faculty faculty3 = new Faculty();
        faculty3.setName(name1);
        faculty3.setId(id3);
        faculty3.setColor(color);

        List<Faculty> facultyList = new ArrayList<>();
        facultyList.add(faculty1);
        facultyList.add(faculty2);
        facultyList.add(faculty3);
        when(facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(any(), any())).thenReturn(facultyList);
        //test
        //check
        System.out.println(facultyList);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?nameOrColor=" + nameOrColor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].color").value(color))
                .andExpect(jsonPath("$[1].color").value(color))
                .andExpect(jsonPath("$[2].color").value(color));

        verify(facultyRepository, only()).findByNameIgnoreCaseOrColorIgnoreCase(any(), any());

    }

    @Test
    @DisplayName("Запрос факультета по имени или цвету(по названию)")
    void findByNameIgnoreCaseOrColorIgnoreCaseForName() throws Exception {
        //data
        long id1 = 1L;
        long id2 = 2L;
        long id3 = 3L;
        String name1 = "test1";
        String color = "red";
        String nameOrColor = "red";

        Faculty faculty1 = new Faculty();
        faculty1.setName(name1);
        faculty1.setId(id1);
        faculty1.setColor(color);

        Faculty faculty2 = new Faculty();
        faculty2.setName(name1);
        faculty2.setId(id2);
        faculty2.setColor(color);

        Faculty faculty3 = new Faculty();
        faculty3.setName(name1);
        faculty3.setId(id3);
        faculty3.setColor(color);

        List<Faculty> facultyList = new ArrayList<>();
        facultyList.add(faculty1);
        facultyList.add(faculty2);
        facultyList.add(faculty3);
        when(facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(any(), any())).thenReturn(facultyList);
        //test
        //check
        System.out.println(facultyList);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?nameOrColor=" + nameOrColor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(name1))
                .andExpect(jsonPath("$[1].name").value(name1))
                .andExpect(jsonPath("$[2].name").value(name1));

        verify(facultyRepository, only()).findByNameIgnoreCaseOrColorIgnoreCase(any(), any());

    }

    @Test
    @DisplayName("Создание факультета")
    public void createFacultyTest() throws Exception {
        //data
        long id = 1;
        String name = "RRR";
        String color = "red";

        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setId(id);
        faculty.setColor(color);

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("id", id);
        facultyObject.put("color", color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        //test
        //check
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.color").value(color));
        verify(facultyRepository, only()).save(any());
    }

    @Test
    @DisplayName("Изменение факультета")
    void updateFaculty() throws Exception {
        //data
        long id = 1L;
        String nameNew = "test";
        String colorNew = "test";
        Faculty oldFaculty = new Faculty();
        oldFaculty.setId(id);
        oldFaculty.setName(faker.harryPotter().house());
        oldFaculty.setColor(faker.color().name());

        Faculty newFaculty = new Faculty();
        newFaculty.setName(nameNew);
        newFaculty.setColor(colorNew);
        newFaculty.setId(id);

        JSONObject newFacultyTest = new JSONObject();
        newFacultyTest.put("name", nameNew);
        newFacultyTest.put("id", id);
        newFacultyTest.put("color", colorNew);
        System.out.println(oldFaculty);
        System.out.println(newFaculty);
        when(facultyRepository.findById(id)).thenReturn(Optional.of(oldFaculty));
        when(facultyRepository.save(any())).thenReturn(newFaculty);
        //test, check
        mockMvc.perform(MockMvcRequestBuilders
                .put("/faculty/{id}", id)
                .content(newFacultyTest.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        verify(facultyRepository, times(1)).save(any());

    }
    @Test
    @DisplayName("Изменение факультета которого нет")
    void updateFacultyNegative() throws Exception {
        //data
        long id = 1L;
        String nameNew = "test";
        String colorNew = "test";
        Faculty oldFaculty = new Faculty();
        oldFaculty.setId(id);
        oldFaculty.setName(faker.harryPotter().house());
        oldFaculty.setColor(faker.color().name());

        Faculty newFaculty = new Faculty();
        newFaculty.setName(nameNew);
        newFaculty.setColor(colorNew);
        newFaculty.setId(id);

        JSONObject newFacultyTest = new JSONObject();
        newFacultyTest.put("name", nameNew);
        newFacultyTest.put("id", id);
        newFacultyTest.put("color", colorNew);
        System.out.println(oldFaculty);
        System.out.println(newFaculty);
        when(facultyRepository.findById(id)).thenThrow(FacultyNotFoundException.class);
        when(facultyRepository.save(any())).thenReturn(newFaculty);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/{id}", id)
                .content(newFacultyTest.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertInstanceOf(FacultyNotFoundException.class, result.getResolvedException()));
    }

    @Test
    @DisplayName("Удаление факультета")
    void deleteFaculty() throws Exception {
        //data
        long id = 1L;

        Faculty oldFaculty = new Faculty();
        oldFaculty.setId(id);
        oldFaculty.setName(faker.harryPotter().house());
        oldFaculty.setColor(faker.color().name());
        when(facultyRepository.save(any(Faculty.class))).thenReturn(oldFaculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(oldFaculty));
        when(facultyRepository.existsById(any())).thenReturn(true);

        //test, check
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/faculty/{id}", id));
        verify(facultyRepository, times(1)).deleteById(id);
    }
    @Test
    @DisplayName("Удаление факультета которого нет")
    void deleteFacultyNegative() throws Exception {
        long id = 1;
        when(facultyRepository.existsById(id)).thenThrow(FacultyNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/{id}", id))
                .andExpect(result -> assertInstanceOf(FacultyNotFoundException.class, result.getResolvedException()));


    }

    @Test
    @DisplayName("Нахождение студентов по id факультета")
    void findStudentsByFacultyId() throws Exception {
        //data
        long id = 1L;
        Faculty faculty = new Faculty();
        faculty.setColor("red");
        faculty.setName(faker.harryPotter().house());
        faculty.setId(id);

        Student student1 = new Student();
        student1.setId(1L);
        student1.setAge(10);
        student1.setName(faker.harryPotter().character());
        student1.setFaculty(faculty);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setAge(12);
        student2.setName(faker.harryPotter().character());
        student2.setFaculty(faculty);

        Student student3 = new Student();
        student3.setId(3L);
        student3.setAge(12);
        student3.setName(faker.harryPotter().character());
        student3.setFaculty(faculty);

        List<Student> studentList = new ArrayList<>();
        studentList.add(student1);
        studentList.add(student2);
        studentList.add(student3);

        when(studentRepository.findAllByFaculty_Id(any(Long.class))).thenReturn(studentList);
        //test, check
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + id + "/students"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        verify(studentRepository, times(1)).findAllByFaculty_Id(id);

    }


}
