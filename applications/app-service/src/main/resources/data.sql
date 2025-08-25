INSERT INTO request_type (name, min_amount, max_amount, interest_rate, automatic_validation)
VALUES 
    ('Crédito Personal', 500000, 20000000, 0.18, TRUE),
    ('Crédito Empresarial', 1000000, 50000000, 0.15, FALSE),
    ('Microcrédito', 100000, 5000000, 0.22, TRUE)
ON CONFLICT (name) DO NOTHING;

INSERT INTO request_status (name, description)
VALUES 
    ('Pendiente por revisión', 'La solicitud está pendiente de ser revisada por un asesor'),
    ('Aprobada', 'La solicitud ha sido aprobada'),
    ('Rechazada', 'La solicitud ha sido rechazada'),
    ('En proceso', 'La solicitud está siendo procesada')
ON CONFLICT (name) DO NOTHING;

