package ru.hogwarts.school.exception;

public abstract class NotFoundException extends RuntimeException {
    private long id;
    private String nameOrColor;
    private String color;

    public NotFoundException(long id) {
        this.id = id;
    }
    public NotFoundException(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public String getNameOrColor() {
        return nameOrColor;
    }

    public long getId() {
        return id;
    }
}

