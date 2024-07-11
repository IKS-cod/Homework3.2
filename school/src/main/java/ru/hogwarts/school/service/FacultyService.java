package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.*;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(long id) {
        if(facultyRepository.findById(id).isEmpty()){
            throw new FacultyNotFoundException(id);
        }
        return facultyRepository.findById(id).get();
    }

    public Collection<Faculty> findAllFacultyForColor(String color) {
        return facultyRepository.findByColor(color);
    }

    public void updateFaculty(long id, Faculty faculty) {
        if(facultyRepository.findById(id).isEmpty()){
            throw new FacultyNotFoundException(id);
        }
        facultyRepository.save(faculty);

    }

    public void deleteFaculty(long id) {
        if(facultyRepository.findById(id).isEmpty()){
            throw new FacultyNotFoundException(id);
        }
        facultyRepository.deleteById(id);
    }

}
