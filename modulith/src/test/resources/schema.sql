-- Course table
CREATE TABLE course (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    status VARCHAR(20) NOT NULL
);

-- Student table
CREATE TABLE student (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    status VARCHAR(20) NOT NULL
);

-- Subscription table
CREATE TABLE subscription (
    id SERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    UNIQUE (student_id, course_id),
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (course_id) REFERENCES course(id)
);
