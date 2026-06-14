CREATE SEQUENCE teachers_seq    START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE students_seq    START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE courses_seq     START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE enrollments_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE teachers (
    id            BIGINT PRIMARY KEY DEFAULT NEXTVAL('teachers_seq'),
    first_name    VARCHAR(100) NOT NULL,
    last_name     VARCHAR(100) NOT NULL,
    email         VARCHAR(150) NOT NULL UNIQUE,
    date_of_birth DATE,
    position      VARCHAR(20)  NOT NULL
);

CREATE TABLE students (
    id               BIGINT PRIMARY KEY DEFAULT NEXTVAL('students_seq'),
    first_name       VARCHAR(100) NOT NULL,
    last_name        VARCHAR(100) NOT NULL,
    email            VARCHAR(150) NOT NULL UNIQUE,
    enrollment_year  INTEGER NOT NULL,
    status           VARCHAR(20)  NOT NULL
);

CREATE TABLE courses (
    id          BIGINT PRIMARY KEY DEFAULT NEXTVAL('courses_seq'),
    name        VARCHAR(200) NOT NULL,
    description TEXT,
    credits     INTEGER NOT NULL,
    teacher_id  BIGINT  NOT NULL REFERENCES teachers(id)
);

CREATE TABLE enrollments (
    id               BIGINT PRIMARY KEY DEFAULT NEXTVAL('enrollments_seq'),
    student_id       BIGINT      NOT NULL REFERENCES students(id),
    course_id        BIGINT      NOT NULL REFERENCES courses(id),
    semester         VARCHAR(20) NOT NULL,
    enrollment_year  INTEGER     NOT NULL,
    grade            VARCHAR(5)  NOT NULL DEFAULT 'NA',
    paid             BOOLEAN     NOT NULL DEFAULT FALSE,
    CONSTRAINT uq_student_course_semester_year UNIQUE (student_id, course_id, semester, enrollment_year)
);
