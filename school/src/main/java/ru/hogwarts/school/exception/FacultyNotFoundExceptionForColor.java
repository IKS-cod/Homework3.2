package ru.hogwarts.school.exception;

public class FacultyNotFoundExceptionForColor extends NotFoundException{
    public FacultyNotFoundExceptionForColor(String color) {
        super(color);
    }


    @Override
    public String getMessage() {
        return "Факультет с color = %s не найден!".formatted(getColor());
    }





}
