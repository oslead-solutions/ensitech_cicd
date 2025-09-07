-- Ce fichier sera exécuté au démarrage du course-service

-- Création de deux cours
INSERT INTO course (title, coefficient, hours, teacher_id) VALUES ('Physique Quantique', 5, 40, 1);
INSERT INTO course (title, coefficient, hours) VALUES ('Chimie Organique', 4, 35); -- On omet teacher_id, il sera NULL par défaut

-- Inscription de l'étudiant 2 (Marie Curie) au cours 1 (Physique Quantique)
INSERT INTO course_students (course_id, student_id) VALUES (1, 2);