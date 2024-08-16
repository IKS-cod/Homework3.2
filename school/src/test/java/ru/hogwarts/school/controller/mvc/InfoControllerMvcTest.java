package ru.hogwarts.school.controller.mvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.InfoController;
import ru.hogwarts.school.service.InfoService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InfoController.class)
class InfoControllerMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InfoService infoService;

    @Test
    @DisplayName("Возвращение номера порта")
    void getPort() throws Exception {
        when(infoService.getPort()).thenReturn("8080");
        mockMvc.perform(get("/port"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("8080"));
    }
}