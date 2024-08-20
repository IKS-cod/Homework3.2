package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Service
public class FacultyService {
    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for \"createFaculty\"");
        return facultyRepository.save(faculty);
    }

    public Faculty getFaculty(long id) {
        logger.info("Was invoked method for \"getFaculty\"");
        return facultyRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not faculty with id = " + id);
            return new FacultyNotFoundException(id);
        });
    }

    public Collection<Faculty> findAllByColor(String color) {
        logger.info("Was invoked method for \"findAllByColor\"");
        /*if (facultyRepository.findAllByColor(color).isEmpty()) {
            throw new FacultyNotFoundExceptionForColor(color);
        }*/
        return facultyRepository.findAllByColor(color);
    }

    public Collection<Faculty> findByNameOrColor(String nameOrColor) {
        logger.info("Was invoked method for \"findByNameOrColor\"");
       /* if (facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(nameOrColor, nameOrColor).isEmpty()){
            throw new FacultyNotFoundExceptionForNameOrColor(nameOrColor);
        }*/
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(nameOrColor, nameOrColor);
    }

    public void updateFaculty(long id, Faculty faculty) {
        logger.info("Was invoked method for \"updateFaculty\"");
        Faculty oldfaculty = facultyRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not faculty with id = " + id);
            return new FacultyNotFoundException(id);
        });
        oldfaculty.setName(faculty.getName());
        oldfaculty.setColor(faculty.getColor());
        facultyRepository.save(oldfaculty);

    }

    public void deleteFaculty(long id) {
        logger.info("Was invoked method for \"deleteFaculty\"");
        if (!facultyRepository.existsById(id)) {
            logger.error("There is not faculty with id = " + id);
            throw new FacultyNotFoundException(id);
        }

        facultyRepository.deleteById(id);

    }

    public List<Student> findStudentsByFacultyId(long id) {
        logger.info("Was invoked method for \"findStudentsByFacultyId\"");
        return studentRepository.findAllByFaculty_Id(id);
    }

    public String getFacultyWithMaxName() {
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparing(String::length)).orElseThrow();
    }
}
