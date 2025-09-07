-- src/main/resources/data.sql

-- Insertion d'étudiants (simulée pour le user-service)
-- Dans un vrai cas, ces données existeraient déjà dans le microservice user-service.
-- Ici, nous les insérons pour que les appels Feign ne échouent pas.
--INSERT INTO student (id, matricule, first_name, last_name, email, address, telephone, birthday, gender) VALUES
--(1, '2022-ST-101', 'Alice', 'Dupont', 'alice.dupont@email.com', '123 Rue de la Paix', '0123456789', '2000-05-15', 'F'),
--(2, '2022-ST-102', 'Bob', 'Martin', 'bob.martin@email.com', '456 Avenue des Champs', '9876543210', '2001-08-22', 'M');


-- Insertion d'inscriptions
-- Les matricules correspondent à celles des étudiants ci-dessus
INSERT INTO registration (id, registration_number, level, date_of_registration, matricule, academic_year_label, speciality_label) VALUES
(1, 1001, 'L1', '2025-08-14 10:00:00', '2022-ST-101','2020-2021','Génie logiciel');


--(2, 1002, 'L2', '2025-08-14 10:05:00', '2022-ST-101','2021-2022','Cybersécurité'),
--(3, 1003, 'L3', '2025-08-14 10:10:00', '2022-ST-102','2022-2023','Genie logiciel');