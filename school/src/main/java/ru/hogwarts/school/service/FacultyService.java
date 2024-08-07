package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;

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

    public Collection<Faculty> findAllByColor(String color) {
        /*if (facultyRepository.findAllByColor(color).isEmpty()) {
            throw new FacultyNotFoundExceptionForColor(color);
        }*/
        return facultyRepository.findAllByColor(color);
    }

    public Collection<Faculty> findByNameOrColor(String nameOrColor) {
       /* if (facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(nameOrColor, nameOrColor).isEmpty()){
            throw new FacultyNotFoundExceptionForNameOrColor(nameOrColor);
        }*/
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(nameOrColor, nameOrColor);
    }

    public void updateFaculty(long id, Faculty faculty) {
        Faculty oldfaculty = facultyRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException(id));
        oldfaculty.setName(faculty.getName());
        oldfaculty.setColor(faculty.getColor());
        facultyRepository.save(oldfaculty);

    }

    public void deleteFaculty(long id) {
        if (!facultyRepository.existsById(id)) {
            throw new FacultyNotFoundException(id);
        }

        facultyRepository.deleteById(id);

    }

    public List<Student> findStudentsByFacultyId(long id) {
        return studentRepository.findAllByFaculty_Id(id);
    }
}
