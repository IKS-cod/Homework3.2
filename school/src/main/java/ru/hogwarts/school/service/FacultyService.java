package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(long id) {
        return facultyRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException(id));
    }
    public Collection<Faculty> findAllByColor(String color){
        return facultyRepository.findAllByColor(color);
    }
    public Collection<Faculty> findByNameOrColor(String nameOrColor){
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(nameOrColor,nameOrColor);
    }

    public void updateFaculty(long id, Faculty faculty) {
        if(!facultyRepository.existsById(id)){  //Все просто — findById()возвращает объект, который вы ищете,
                                                // existsById()возвращает true/false независимо от того, существует
                                                //  ли сущность в репозитории.
            throw new FacultyNotFoundException(id);
        }
        Faculty oldfaculty=facultyRepository.findById(id).get();
        oldfaculty.setName(faculty.getName());
        oldfaculty.setColor(faculty.getColor());
        facultyRepository.save(oldfaculty);

    }

    public void deleteFaculty(long id) {
        if(!facultyRepository.existsById(id)){
            throw new FacultyNotFoundException(id);
        }
        facultyRepository.deleteById(id);
    }

    public List<Student> findStudentsByFacultyId(long id) {
        return studentRepository.findAllByFaculty_Id(id);
    }
}
