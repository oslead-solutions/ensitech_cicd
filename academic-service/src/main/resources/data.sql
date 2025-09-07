-- Insert sample data for academic_year
INSERT INTO academic_year (id, label, start_date, ended_date, status, created_at, updated_at)
VALUES
    (1, '2024-2025', '2024-09-01', '2025-09-30', 'EN_COURS', '2025-08-21 10:00:00', '2025-08-21 10:00:00');
--    (3, '2023-2024', '2023-09-01', '2024-06-30', 'EN_COURS', '2025-08-21 10:00:00', '2025-08-21 10:00:00'),
--    (2, '2022-2023', '2022-09-01', '2023-06-30', 'TERMINEE', '2025-08-21 10:00:00', '2025-08-21 10:00:00'),

-- Insert sample data for period, linked to academic_year
-- Insert sample data for period, linked to academic_year
INSERT INTO period (id, entitled, started_at, ended_at, type_period, updated, created_at, academic_year_id)
VALUES
    (1, 'Inscription Period 3', '2024-09-01', '2025-09-15', 'INSCRIPTION_PERIOD', '2025-08-21 10:00:00', '2025-08-21 10:00:00', 1),
    (2, 'Course Period 3', '2025-09-16', '2025-09-25', 'COURS_PERIOD', '2025-08-21 10:00:00', '2025-08-21 10:00:00', 1),
    (3, 'Exam Period 3', '2025-09-23', '2025-09-30', 'EXAMENS_PERIOD', '2025-08-21 10:00:00', '2025-08-21 10:00:00', 1);
--    (4, 'Inscription Period 1', '2023-09-01', '2023-09-15', 'INSCRIPTION_PERIOD', '2025-08-21 10:00:00', '2025-08-21 10:00:00', 1),
--    (5, 'Course Period 1', '2023-09-16', '2023-12-20', 'COURS_PERIOD', '2025-08-21 10:00:00', '2025-08-21 10:00:00', 1),
--    (6, 'Exam Period 1', '2024-01-08', '2024-01-20', 'EXAMENS_PERIOD', '2025-08-21 10:00:00', '2025-08-21 10:00:00', 1),
--    (7, 'Inscription Period 2', '2022-09-01', '2022-09-15', 'INSCRIPTION_PERIOD', '2025-08-21 10:00:00', '2025-08-21 10:00:00', 2),
--    (8, 'Course Period 2', '2022-09-16', '2022-12-20', 'COURS_PERIOD', '2025-08-21 10:00:00', '2025-08-21 10:00:00', 2),
--    (9, 'Exam Period 2', '2023-01-08', '2023-01-20', 'EXAMENS_PERIOD', '2025-08-21 10:00:00', '2025-08-21 10:00:00', 2),
