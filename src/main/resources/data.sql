-- Registros de muestra insertados solo cuando aún no existen.
-- Se omite la columna ID para dejar que H2 gestione el autoincremento.
INSERT INTO drones (name, model, serial_number, status, battery_level, last_maintenance)
SELECT 'Aquila 01', 'DJI Mavic 3', 'SR-DR-001', 'Disponible', 94, DATE '2026-09-15'
WHERE NOT EXISTS (SELECT 1 FROM drones WHERE serial_number = 'SR-DR-001');

INSERT INTO drones (name, model, serial_number, status, battery_level, last_maintenance)
SELECT 'Cóndor 02', 'Autel EVO II', 'SR-DR-002', 'En mantenimiento', 42, DATE '2026-08-30'
WHERE NOT EXISTS (SELECT 1 FROM drones WHERE serial_number = 'SR-DR-002');

INSERT INTO drones (name, model, serial_number, status, battery_level, last_maintenance)
SELECT 'Halcón 03', 'DJI Air 3', 'SR-DR-003', 'Disponible', 78, DATE '2026-10-01'
WHERE NOT EXISTS (SELECT 1 FROM drones WHERE serial_number = 'SR-DR-003');

INSERT INTO flights (drone_id, origin, destination, scheduled_at, duration_minutes, status, notes)
SELECT d.id, 'Base central', 'Zona norte', TIMESTAMP '2026-10-24 09:00:00', 45, 'Programado', 'Inspección preventiva de infraestructura.'
FROM drones d
WHERE d.serial_number = 'SR-DR-001'
  AND NOT EXISTS (SELECT 1 FROM flights WHERE origin = 'Base central' AND destination = 'Zona norte');

INSERT INTO flights (drone_id, origin, destination, scheduled_at, duration_minutes, status, notes)
SELECT d.id, 'Base central', 'Zona industrial', TIMESTAMP '2026-10-25 14:30:00', 60, 'Programado', 'Vuelo de reconocimiento.'
FROM drones d
WHERE d.serial_number = 'SR-DR-003'
  AND NOT EXISTS (SELECT 1 FROM flights WHERE origin = 'Base central' AND destination = 'Zona industrial');
