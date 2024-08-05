package ru.hogwarts.school.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;
import ru.hogwarts.school.exception.AvatarProcessingException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repository.AvatarRepository;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AvatarServiceTest {
    @Mock
    AvatarRepository avatarRepository;
    @InjectMocks
    AvatarService avatarService;

    @Test
    void uploadAvatarNegativeTest() {
    }

    @Test
    void getAvatarFromDbNegativeTest() {
        long studentId = 1L;
        when(avatarRepository.findByStudent_Id(studentId)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(StudentNotFoundException.class)
                .isThrownBy(() -> avatarService.getAvatarFromDb(studentId));
    }

    @Test// Нужно доделать throw new AvatarProcessingException();
    void getAvatarFromFsNegativeTest() {
        long studentId = 1L;
        when(avatarRepository.findByStudent_Id(studentId)).thenReturn(Optional.empty());
        Assertions.assertThatExceptionOfType(StudentNotFoundException.class)
                .isThrownBy(() -> avatarService.getAvatarFromFs(studentId));


    }
}