-- ============================================================
-- V15__fix_lote_fotos_urls.sql
-- Corrige las URLs de lote_fotos de V13 y V14
--
-- PROBLEMA: las URLs originales de images.unsplash.com devuelven
-- 403 Forbidden cuando son solicitadas sin un referer autorizado
-- (ej: desde la app móvil o servidor backend). Además algunas
-- imágenes no concordaban visualmente con el producto.
--
-- SOLUCIÓN: DELETE + re-INSERT de las 60 filas con photo-IDs
-- curados uno a uno para que la imagen coincida con el lote.
-- ============================================================

-- ============================================================
-- 1. ELIMINAR fotos existentes de los lotes V13 (d000...) y V14 (e000...)
-- ============================================================
DELETE FROM lote_fotos
WHERE lote_id::text LIKE 'd0000001-0000-0000-0000-%'
   OR lote_id::text LIKE 'e0000001-0000-0000-0000-%';

-- ============================================================
-- 2. FOTOS CORREGIDAS — V13 (30 lotes Supermaxi La Carolina)
--    Prefijo: d0000001-0000-0000-0000-
-- ============================================================
INSERT INTO lote_fotos (lote_id, url) VALUES

-- FRUTAS_VERDURAS
('d0000001-0000-0000-0000-000000000001', 'https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=800&q=80'),  -- bananas maduras
('d0000001-0000-0000-0000-000000000002', 'https://images.unsplash.com/photo-1592841200221-a6898f307baa?w=800&q=80'),  -- tomates cherry
('d0000001-0000-0000-0000-000000000003', 'https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=800&q=80'),    -- mix pimientos (CORREGIDO)
('d0000001-0000-0000-0000-000000000004', 'https://images.unsplash.com/photo-1567306226416-28f0efdc88ce?w=800&q=80'),  -- manzanas Fuji
('d0000001-0000-0000-0000-000000000005', 'https://images.unsplash.com/photo-1576045057995-568f588f82fb?w=800&q=80'),  -- espinaca fresca
('d0000001-0000-0000-0000-000000000006', 'https://images.unsplash.com/photo-1523049673857-eb18f1d7b578?w=800&q=80'),  -- aguacates Hass

-- LÁCTEOS
('d0000001-0000-0000-0000-000000000007', 'https://images.unsplash.com/photo-1550583724-b2692b85b150?w=800&q=80'),    -- leche entera
('d0000001-0000-0000-0000-000000000008', 'https://images.unsplash.com/photo-1488477181946-6428a0291777?w=800&q=80'),  -- yogur natural
('d0000001-0000-0000-0000-000000000009', 'https://images.unsplash.com/photo-1486297678162-eb2a19b0a32d?w=800&q=80'),  -- queso fresco
('d0000001-0000-0000-0000-000000000010', 'https://images.unsplash.com/photo-1559598467-f8b76c8155d0?w=800&q=80'),    -- crema de leche
('d0000001-0000-0000-0000-000000000011', 'https://images.unsplash.com/photo-1589985270826-4b7bb135bc9d?w=800&q=80'),  -- mantequilla sin sal

-- PANADERÍA
('d0000001-0000-0000-0000-000000000012', 'https://images.unsplash.com/photo-1549931319-a545dcf3bc7c?w=800&q=80'),    -- pan de molde integral
('d0000001-0000-0000-0000-000000000013', 'https://images.unsplash.com/photo-1555507036-ab1f4038808a?w=800&q=80'),    -- croissants
('d0000001-0000-0000-0000-000000000014', 'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=800&q=80'),    -- magdalenas de vainilla
('d0000001-0000-0000-0000-000000000015', 'https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=800&q=80'),  -- torta de chocolate

-- CARNES
('d0000001-0000-0000-0000-000000000016', 'https://images.unsplash.com/photo-1604503468506-a8da13d11d36?w=800&q=80'),  -- pechuga de pollo
('d0000001-0000-0000-0000-000000000017', 'https://images.unsplash.com/photo-1603048588665-791ca8aea617?w=800&q=80'),  -- carne molida res
('d0000001-0000-0000-0000-000000000018', 'https://images.unsplash.com/photo-1519708227418-c8fd9a32b7a2?w=800&q=80'),  -- filetes tilapia
('d0000001-0000-0000-0000-000000000019', 'https://images.unsplash.com/photo-1615361200141-f45040f367be?w=800&q=80'),  -- salchichas artesanales

-- ABARROTES
('d0000001-0000-0000-0000-000000000020', 'https://images.unsplash.com/photo-1536304993881-ff86e0c9ef1d?w=800&q=80'),  -- arroz blanco
('d0000001-0000-0000-0000-000000000021', 'https://images.unsplash.com/photo-1515543904379-3d757afe72e4?w=800&q=80'),  -- lentejas verdes (CORREGIDO: era foto de papas)
('d0000001-0000-0000-0000-000000000022', 'https://images.unsplash.com/photo-1551462147-37885acc36f1?w=800&q=80'),    -- espagueti
('d0000001-0000-0000-0000-000000000023', 'https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=800&q=80'),  -- aceite girasol
('d0000001-0000-0000-0000-000000000024', 'https://images.unsplash.com/photo-1614961233913-a5113a4a34ed?w=800&q=80'),  -- avena hojuelas

-- COMIDA_PREPARADA
('d0000001-0000-0000-0000-000000000025', 'https://images.unsplash.com/photo-1579871494447-9811cf80d66c?w=800&q=80'),  -- sushi
('d0000001-0000-0000-0000-000000000026', 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=800&q=80'),  -- ensalada César (CORREGIDO: era foto genérica verduras)
('d0000001-0000-0000-0000-000000000027', 'https://images.unsplash.com/photo-1598103442097-8b74394b95c3?w=800&q=80'),  -- pollo asado

-- BEBIDAS
('d0000001-0000-0000-0000-000000000028', 'https://images.unsplash.com/photo-1600271886742-f049cd451bba?w=800&q=80'),  -- jugo de naranja
('d0000001-0000-0000-0000-000000000029', 'https://images.unsplash.com/photo-1523362628745-0c100150b504?w=800&q=80'),  -- agua con gas
('d0000001-0000-0000-0000-000000000030', 'https://images.unsplash.com/photo-1556679343-c7306c1976bc?w=800&q=80')     -- té helado durazno

ON CONFLICT DO NOTHING;

-- ============================================================
-- 3. FOTOS CORREGIDAS — V14 (30 lotes distribuidos Quito)
--    Prefijo: e0000001-0000-0000-0000-
-- ============================================================
INSERT INTO lote_fotos (lote_id, url) VALUES

-- MERCADO EL BATÁN
('e0000001-0000-0000-0000-000000000001', 'https://images.unsplash.com/photo-1602491453631-e2a5ad90a131?w=800&q=80'),  -- choclos tiernos
('e0000001-0000-0000-0000-000000000002', 'https://images.unsplash.com/photo-1486297678162-eb2a19b0a32d?w=800&q=80'),  -- queso de hoja
('e0000001-0000-0000-0000-000000000003', 'https://images.unsplash.com/photo-1518977676601-b53f82aba655?w=800&q=80'),  -- papas chola

-- PANADERÍA LA FLORESTA
('e0000001-0000-0000-0000-000000000004', 'https://images.unsplash.com/photo-1558961363-fa8fdf82db35?w=800&q=80'),    -- pandeyuca / pan
('e0000001-0000-0000-0000-000000000005', 'https://images.unsplash.com/photo-1604908176997-125f25cc6f3d?w=800&q=80'),  -- empanadas morocho
('e0000001-0000-0000-0000-000000000006', 'https://images.unsplash.com/photo-1558961363-fa8fdf82db35?w=800&q=80'),    -- bizcochos Cayambe (CORREGIDO: era pan de molde)

-- MINIMARKET LA MARISCAL
('e0000001-0000-0000-0000-000000000007', 'https://images.unsplash.com/photo-1490474418585-ba9bad8fd0ea?w=800&q=80'),  -- mix frutas tropicales
('e0000001-0000-0000-0000-000000000008', 'https://images.unsplash.com/photo-1539252554935-eeedd081cffa?w=800&q=80'),  -- sándwiches
('e0000001-0000-0000-0000-000000000009', 'https://images.unsplash.com/photo-1556679343-c7306c1976bc?w=800&q=80'),    -- infusión jamaica (CORREGIDO: era foto agua genérica)

-- TIENDA SAN ROQUE
('e0000001-0000-0000-0000-000000000010', 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=800&q=80'),    -- menestra frijol (CORREGIDO: era foto de papas)
('e0000001-0000-0000-0000-000000000011', 'https://images.unsplash.com/photo-1519708227418-c8fd9a32b7a2?w=800&q=80'),  -- tilapia frita (CORREGIDO: era foto sin relación)
('e0000001-0000-0000-0000-000000000012', 'https://images.unsplash.com/photo-1563227812-0ea4c22e6cc8?w=800&q=80'),    -- morochillo bebida caliente (CORREGIDO)

-- SUPERMERCADO CHILLOGALLO
('e0000001-0000-0000-0000-000000000013', 'https://images.unsplash.com/photo-1515543904379-3d757afe72e4?w=800&q=80'),  -- mote/granos cocidos
('e0000001-0000-0000-0000-000000000014', 'https://images.unsplash.com/photo-1551462147-37885acc36f1?w=800&q=80'),    -- fideo de huevo
('e0000001-0000-0000-0000-000000000015', 'https://images.unsplash.com/photo-1550583724-b2692b85b150?w=800&q=80'),    -- bebida de soya

-- DESPENSA SOLANDA
('e0000001-0000-0000-0000-000000000016', 'https://images.unsplash.com/photo-1562802378-063ec186a863?w=800&q=80'),    -- arroz con leche
('e0000001-0000-0000-0000-000000000017', 'https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=800&q=80'),  -- bananas guineo
('e0000001-0000-0000-0000-000000000018', 'https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=800&q=80'),  -- aceite de palma

-- SUPERMARKET SANGOLQUÍ
('e0000001-0000-0000-0000-000000000019', 'https://images.unsplash.com/photo-1555939594-58d7cb561ad1?w=800&q=80'),    -- cuy asado / carne asada
('e0000001-0000-0000-0000-000000000020', 'https://images.unsplash.com/photo-1563227812-0ea4c22e6cc8?w=800&q=80'),    -- morocho caliente
('e0000001-0000-0000-0000-000000000021', 'https://images.unsplash.com/photo-1596591868231-05e808fd131d?w=800&q=80'),  -- uvillas del Valle

-- DELI CUMBAYÁ
('e0000001-0000-0000-0000-000000000022', 'https://images.unsplash.com/photo-1576577445504-6af96477db52?w=800&q=80'),  -- quiche artesanal (CORREGIDO)
('e0000001-0000-0000-0000-000000000023', 'https://images.unsplash.com/photo-1517982040918-b776bff47e50?w=800&q=80'),  -- granola casera
('e0000001-0000-0000-0000-000000000024', 'https://images.unsplash.com/photo-1541014741259-de529411b96a?w=800&q=80'),  -- bebida jengibre/kombucha (CORREGIDO)

-- MERCADO COTOCOLLAO
('e0000001-0000-0000-0000-000000000025', 'https://images.unsplash.com/photo-1536304993881-ff86e0c9ef1d?w=800&q=80'),  -- habas tostadas / granos (CORREGIDO)
('e0000001-0000-0000-0000-000000000026', 'https://images.unsplash.com/photo-1518977676601-b53f82aba655?w=800&q=80'),  -- mellocos andinos / tubérculos
('e0000001-0000-0000-0000-000000000027', 'https://images.unsplash.com/photo-1547592166-23ac45744acd?w=800&q=80'),    -- caldo de gallina / sopa

-- FRESHMART QUITUMBE
('e0000001-0000-0000-0000-000000000028', 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=800&q=80'),    -- fréjol canario (CORREGIDO: era foto de papas)
('e0000001-0000-0000-0000-000000000029', 'https://images.unsplash.com/photo-1519708227418-c8fd9a32b7a2?w=800&q=80'),  -- tilapia entera fresca
('e0000001-0000-0000-0000-000000000030', 'https://images.unsplash.com/photo-1481349518771-20055b2a7b24?w=800&q=80')   -- plátanos verdes

ON CONFLICT DO NOTHING;
