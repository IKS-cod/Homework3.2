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
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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
    void getFaculty() {
    }

    @Test
    void findAllByColor() {
    }

    @Test
    void findByNameIgnoreCaseOrColorIgnoreCase() {
    }

    @Test
    @DisplayName("Создание студента")
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
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(nameNew))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.color").value(colorNew));

    }

    @Test
    @DisplayName("Удаление факультета")
    void deleteFaculty() throws Exception {
        //data
        long id = 1L;

        /*Faculty oldFaculty = new Faculty();
        oldFaculty.setId(id);
        oldFaculty.setName(faker.harryPotter().house());
        oldFaculty.setColor(faker.color().name());

        Faculty faculty = new Faculty();*/

        //test, check
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").isEmpty())
                .andExpect(jsonPath("$.id").isEmpty())
                .andExpect(jsonPath("$.color").isEmpty());
    }

    @Test
    void findStudentsByFacultyId() {
    }


}
