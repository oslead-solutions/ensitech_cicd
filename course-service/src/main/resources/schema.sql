-- On s'assure de supprimer les tables si elles existent pour un redémarrage propre
DROP TABLE IF EXISTS course_students CASCADE;
DROP TABLE IF EXISTS course CASCADE;

-- Création de la table 'course'
CREATE TABLE course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    coefficient INT NOT NULL,
    hours INT NOT NULL,
    teacher_id BIGINT
);

-- Création de la table de jointure pour les étudiants
CREATE TABLE course_students (
    course_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    PRIMARY KEY (course_id, student_id),
    FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE
);