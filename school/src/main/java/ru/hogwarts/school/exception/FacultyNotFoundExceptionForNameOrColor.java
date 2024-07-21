package ru.hogwarts.school.exception;

public class FacultyNotFoundExceptionForNameOrColor extends NotFoundException{
    public FacultyNotFoundExceptionForNameOrColor(String nameOrColor) {
        super(nameOrColor);
    }


@Override
    public String getMessage() {
        return "Факультет с color или name = %s не найден!".formatted(getNameOrColor());
    }
}
