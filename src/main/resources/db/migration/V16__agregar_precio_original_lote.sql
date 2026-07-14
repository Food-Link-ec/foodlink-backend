-- ============================================================
-- V16__agregar_precio_original_lote.sql
-- Agrega precio_original a lotes_excedentes para poder mostrar
-- el precio de mercado (antes del descuento) en cualquier
-- lectura del lote, no solo en la respuesta inmediata al publicar.
--
-- NOTA: V13 y V14 (seeds) ya están aplicados en Supabase y no se
-- modifican aquí para no romper la validación de checksum de
-- Flyway. El backfill de las filas existentes se hace con el
-- UPDATE de abajo usando una proporción sobre precio_monto.
-- ============================================================

ALTER TABLE lotes_excedentes
ADD COLUMN IF NOT EXISTS precio_original DECIMAL(10,2);

UPDATE lotes_excedentes
SET precio_original = precio_monto * 1.75
WHERE precio_original IS NULL;
