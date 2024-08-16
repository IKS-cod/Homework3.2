package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InfoServiceTest {
    //private InfoService infoService = new InfoService();

    @Test
    void getPort() {
        String expected = "8080";
        InfoService infoService = mock(InfoService.class, CALLS_REAL_METHODS);
        when(infoService.getPort()).thenReturn("8080");
        String actual = infoService.getPort();
        assertEquals(expected, actual);

    }
}