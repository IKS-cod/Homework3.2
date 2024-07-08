package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.*;

@Service
public class FacultyService {
    Map<Long, Faculty> faculties = new HashMap<>();
    private long countId = 0;

    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(++countId);
        faculties.put(countId, faculty);
        return faculty;
    }

    public Faculty get(long id) {
        if (!faculties.containsKey(id)) {
            throw new FacultyNotFoundException(id);
        }
        return faculties.get(id);
    }

    public Collection<Faculty> findAllFacultyForColor(String color) {
        List<Faculty> facultyList = new ArrayList<>();
        for (Faculty faculty : faculties.values()) {
            if (faculty.getColor().equals(color)) {
                facultyList.add(faculty);
            }
        }
        return Collections.unmodifiableList(facultyList);
    }

    public void editFaculty(long id, Faculty faculty) {
        if (!faculties.containsKey(id)) {
            throw new FacultyNotFoundException(id);
        }
        Faculty oldFaculty = faculties.get(id);
        oldFaculty.setName(faculty.getName());
        oldFaculty.setColor(faculty.getColor());

    }

    public Faculty deleteFaculty(long id) {
        if (!faculties.containsKey(id)) {
            throw new FacultyNotFoundException(id);
        }
        return faculties.remove(id);
    }

}
