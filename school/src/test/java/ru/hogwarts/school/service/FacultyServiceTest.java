package ru.hogwarts.school.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.FacultyNotFoundExceptionForColor;
import ru.hogwarts.school.exception.FacultyNotFoundExceptionForNameOrColor;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FacultyServiceTest {
    @Mock
    FacultyRepository facultyRepository;
    @InjectMocks
    FacultyService facultyService;


    @Test
    void getFacultyNegativeTest() {
        long id = 1L;
        when(facultyRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(FacultyNotFoundException.class)
                .isThrownBy(() -> facultyService.getFaculty(id));
    }

    @Test
    void findAllByColorNegativeTest() {
        String color = "red";
        when(facultyRepository.findAllByColor(color)).thenReturn(Collections.emptyList());
        Assertions.assertThatExceptionOfType(FacultyNotFoundExceptionForColor.class)
                .isThrownBy(() -> facultyService.findAllByColor(color));


    }

    @Test
    void findByNameOrColorNegativeTest() {
        String nameOrColor = "nameOrColor";
        when(facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(nameOrColor, nameOrColor)).thenReturn(Collections.emptyList());
        Assertions.assertThatExceptionOfType(FacultyNotFoundExceptionForNameOrColor.class)
                .isThrownBy(() -> facultyService.findByNameOrColor(nameOrColor));

    }

    @Test
    void updateFacultyNegativeTest() {
        Faculty faculty = new Faculty();
        long id = 1L;
        when(facultyRepository.existsById(id)).thenReturn(false);
        Assertions.assertThatExceptionOfType(FacultyNotFoundException.class)
                .isThrownBy(() -> facultyService.updateFaculty(id,faculty));
        //Почему не работает следующая проверка????????
       /*when(facultyRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(FacultyNotFoundException.class)
                .isThrownBy(() -> facultyService.updateFaculty(id, faculty));*/
    }

    @Test
    void deleteFacultyNegativeTest() {
        long id = 1L;
        when(facultyRepository.existsById(id)).thenReturn(false);
        Assertions.assertThatExceptionOfType(FacultyNotFoundException.class)
                .isThrownBy(() -> facultyService.deleteFaculty(id));

    }


}