-- liquibase formatted sql

-- changeset ismirnov:1
CREATE INDEX student_name_index ON student (name);
-- changeset ismirnov:2
CREATE INDEX faculty_nc_index ON faculty (name, color);