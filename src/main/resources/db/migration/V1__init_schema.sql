CREATE SEQUENCE teachers_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE students_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE courses_seq  START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE enrollments_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE teachers (
    id          BIGINT PRIMARY KEY DEFAULT NEXTVAL('teachers_seq'),
    first_name  VARCHAR(100) NOT NULL,
    last_name   VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    department  VARCHAR(100) NOT NULL
);

CREATE TABLE students (
    id               BIGINT PRIMARY KEY DEFAULT NEXTVAL('students_seq'),
    first_name       VARCHAR(100) NOT NULL,
    last_name        VARCHAR(100) NOT NULL,
    email            VARCHAR(150) NOT NULL UNIQUE,
    enrollment_date  DATE NOT NULL,
    status           VARCHAR(20)  NOT NULL,
    study_year       INTEGER NOT NULL
);

CREATE TABLE courses (
    id          BIGINT PRIMARY KEY DEFAULT NEXTVAL('courses_seq'),
    title       VARCHAR(200) NOT NULL,
    description TEXT,
    credits     INTEGER NOT NULL,
    teacher_id  BIGINT  NOT NULL REFERENCES teachers(id)
);

CREATE TABLE enrollments (
    id               BIGINT PRIMARY KEY DEFAULT NEXTVAL('enrollments_seq'),
    student_id       BIGINT NOT NULL REFERENCES students(id),
    course_id        BIGINT NOT NULL REFERENCES courses(id),
    enrollment_date  DATE NOT NULL,
    grade            NUMERIC(4,2),
    paid             BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT uq_student_course UNIQUE (student_id, course_id)
);
