-- Course data
INSERT INTO course (id, name, status) VALUES (1, 'Introduction to Spring Boot', 'ACTIVE');
INSERT INTO course (id, name, status) VALUES (2, 'Advanced Java Programming', 'ACTIVE');
INSERT INTO course (id, name, status) VALUES (3, 'Database Design', 'DORMANT');
INSERT INTO course (id, name, status) VALUES (4, 'Web Development Fundamentals', 'ENDED');
INSERT INTO course (id, name, status) VALUES (5, 'Cloud Computing', 'ACTIVE');

-- Student data
INSERT INTO student (id, name, status) VALUES (1, 'John Doe', 'ACTIVE');
INSERT INTO student (id, name, status) VALUES (2, 'Jane Smith', 'ACTIVE');
INSERT INTO student (id, name, status) VALUES (3, 'Bob Johnson', 'INACTIVE');
INSERT INTO student (id, name, status) VALUES (4, 'Alice Williams', 'ACTIVE');
INSERT INTO student (id, name, status) VALUES (5, 'Charlie Brown', 'ACTIVE');

-- Subscription data
INSERT INTO subscription (id, student_id, course_id, status) VALUES (1, 1, 1, 'ACTIVE');
INSERT INTO subscription (id, student_id, course_id, status) VALUES (2, 1, 2, 'ACTIVE');
INSERT INTO subscription (id, student_id, course_id, status) VALUES (3, 2, 1, 'COMPLETED');
INSERT INTO subscription (id, student_id, course_id, status) VALUES (4, 2, 3, 'DORMANT');
INSERT INTO subscription (id, student_id, course_id, status) VALUES (5, 3, 4, 'CANCELLED');
INSERT INTO subscription (id, student_id, course_id, status) VALUES (6, 4, 5, 'ACTIVE');
INSERT INTO subscription (id, student_id, course_id, status) VALUES (7, 5, 2, 'ACTIVE');
INSERT INTO subscription (id, student_id, course_id, status) VALUES (8, 5, 5, 'DORMANT');
