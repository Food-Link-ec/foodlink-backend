ALTER TABLE lotes_excedentes
ADD COLUMN IF NOT EXISTS latitud  DECIMAL(10,8),
ADD COLUMN IF NOT EXISTS longitud DECIMAL(11,8);

CREATE INDEX IF NOT EXISTS idx_lote_latitud_longitud
ON lotes_excedentes(latitud, longitud)
WHERE latitud IS NOT NULL AND longitud IS NOT NULL;

UPDATE lotes_excedentes SET latitud = -0.1807, longitud = -78.4897
WHERE id = 'd1b2c3d4-e5f6-7890-abcd-ef1234567801';

UPDATE lotes_excedentes SET latitud = -0.1807, longitud = -78.4897
WHERE id = 'd1b2c3d4-e5f6-7890-abcd-ef1234567802';

UPDATE lotes_excedentes SET latitud = -0.2201, longitud = -78.5123
WHERE id = 'd1b2c3d4-e5f6-7890-abcd-ef1234567803';

UPDATE lotes_excedentes SET latitud = -0.1807, longitud = -78.4897
WHERE id = 'd1b2c3d4-e5f6-7890-abcd-ef1234567804';

UPDATE lotes_excedentes SET latitud = -0.2201, longitud = -78.5123
WHERE id = 'd1b2c3d4-e5f6-7890-abcd-ef1234567805';
