package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

class FacultyServiceTest {
    private FacultyService facultyService = new FacultyService();

    @Test
    void createFacultyTest() {
        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(() -> facultyService.get(1L));
        List<Faculty> facultyList = new ArrayList<>();
        Faculty faculty1 = new Faculty(0L, "A", "D");
        Faculty faculty2 = new Faculty(0L, "B", "D");
        Faculty faculty3 = new Faculty(0L, "C", "D");
        facultyList.add(faculty1);
        facultyList.add(faculty2);
        facultyList.add(faculty3);
        facultyService.createFaculty(faculty1);
        facultyService.createFaculty(faculty2);
        facultyService.createFaculty(faculty3);
        Faculty faculty = facultyService.get(1L);
        System.out.println(faculty);
        boolean flag = false;
        for (Faculty f : facultyList) {
            if (f.equals(faculty)) {
                System.out.println(f.equals(faculty));
                flag = true;
                break;
            }
        }
        assertThat(flag).isEqualTo(true);
    }

    @Test
    void getTest() {
        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(() -> facultyService.get(1L));
        List<Faculty> facultyList = new ArrayList<>();
        Faculty faculty1 = new Faculty(0L, "A", "D");
        Faculty faculty2 = new Faculty(0L, "B", "D");
        Faculty faculty3 = new Faculty(0L, "C", "D");
        facultyList.add(faculty1);
        facultyList.add(faculty2);
        facultyList.add(faculty3);
        facultyService.createFaculty(faculty1);
        facultyService.createFaculty(faculty2);
        facultyService.createFaculty(faculty3);
        Faculty faculty = facultyService.get(1L);
        System.out.println(faculty);
        boolean flag = false;
        for (Faculty f : facultyList) {
            if (f.equals(faculty)) {
                System.out.println(f.equals(faculty));
                flag = true;
                break;
            }
        }
        assertThat(flag).isEqualTo(true);
    }

    @Test
    void findAllFacultyForColorTest() {
        Faculty faculty1 = new Faculty(0L, "A", "D");
        Faculty faculty2 = new Faculty(0L, "B", "D");
        Faculty faculty3 = new Faculty(0L, "C", "D");
        facultyService.createFaculty(faculty1);
        facultyService.createFaculty(faculty2);
        facultyService.createFaculty(faculty3);
        assertThat(facultyService.findAllFacultyForColor("D").size()).isEqualTo(3);
        System.out.println(facultyService.findAllFacultyForColor("D"));
    }

    @Test
    void editFacultyTest() {
        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(() -> facultyService.get(1L));
        List<Faculty> facultyList = new ArrayList<>();
        Faculty faculty1 = new Faculty(0L, "A", "D");
        Faculty faculty2 = new Faculty(0L, "B", "D");
        Faculty faculty3 = new Faculty(0L, "C", "D");
        facultyList.add(faculty1);
        facultyList.add(faculty2);
        facultyList.add(faculty3);
        facultyService.createFaculty(faculty1);
        facultyService.createFaculty(faculty2);
        facultyService.createFaculty(faculty3);
        Faculty faculty = facultyService.get(1L);
        System.out.println(faculty);
        boolean flag = false;
        for (Faculty f : facultyList) {
            if (f.equals(faculty)) {
                System.out.println(f.equals(faculty));
                flag = true;
                break;
            }
        }
        System.out.println("flag " + flag);
        assertThat(flag).isEqualTo(true);
        Faculty faculty4 = new Faculty(1L, "New", "New");
        facultyService.editFaculty(1, faculty4);
        assertThat(facultyService.get(1L).equals(faculty4)).isEqualTo(true);
        System.out.println(facultyService.get(1L));
    }

    @Test
    void deleteFacultyTest() {
        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(() -> facultyService.get(1L));
        List<Faculty> facultyList = new ArrayList<>();
        Faculty faculty1 = new Faculty(0L, "A", "D");
        Faculty faculty2 = new Faculty(0L, "B", "D");
        Faculty faculty3 = new Faculty(0L, "C", "D");
        facultyList.add(faculty1);
        facultyList.add(faculty2);
        facultyList.add(faculty3);
        facultyService.createFaculty(faculty1);
        facultyService.createFaculty(faculty2);
        facultyService.createFaculty(faculty3);
        Faculty faculty = facultyService.get(1L);
        System.out.println(faculty);
        boolean flag = false;
        for (Faculty f : facultyList) {
            if (f.equals(faculty)) {
                System.out.println(f.equals(faculty));
                flag = true;
                break;
            }
        }
        assertThat(flag).isEqualTo(true);
        facultyService.deleteFaculty(1L);
        assertThatExceptionOfType(FacultyNotFoundException.class).isThrownBy(() -> facultyService.get(1L));
    }
}