-- ============================================================
-- V13__seed_comercios_y_lotes.sql
-- Seed: comercios Supermaxi Carolina + 30 lotes con fotos
-- Columnas reales de lotes_excedentes confirmadas
-- ============================================================

-- ============================================================
-- COMERCIOS ADICIONALES (3 sucursales de Supermaxi Carolina)
-- ============================================================
INSERT INTO comercios (id, ruc, nombre, telefono, email, estado, provincia, ciudad,
    calle_principal, calle_secundaria, referencia, fecha_registro, password_hash, activo)
VALUES
    ('b1c2d3e4-f5a6-7890-bcde-f12345678901',
     '1791000031001', 'Supermaxi Carolina Sur',
     '0991234568', 'supermaxi.carolina.sur@foodlink.ec',
     'VERIFICADO', 'Pichincha', 'Quito',
     'Av. Naciones Unidas', 'Av. 10 de Agosto',
     'Junto al CC Iñaquito',
     NOW(), '$2a$12$Tnw7OFP0f/gmE8pAyUxbJ.avWXSl7sArAPwg27hiHjFSujRO97zcu', true),

    ('b1c2d3e4-f5a6-7890-bcde-f12345678902',
     '1791000049001', 'Supermaxi Carolina Express',
     '0991234569', 'supermaxi.carolina.express@foodlink.ec',
     'VERIFICADO', 'Pichincha', 'Quito',
     'Av. República', 'Av. Eloy Alfaro',
     'Frente al Parque El Ejido',
     NOW(), '$2a$12$Tnw7OFP0f/gmE8pAyUxbJ.avWXSl7sArAPwg27hiHjFSujRO97zcu', true),

    ('b1c2d3e4-f5a6-7890-bcde-f12345678903',
     '1791000057001', 'Supermaxi Carolina Premium',
     '0991234570', 'supermaxi.carolina.premium@foodlink.ec',
     'VERIFICADO', 'Pichincha', 'Quito',
     'Av. Gaspar de Villarroel', 'Av. 6 de Diciembre',
     'Sector La Pradera, planta baja',
     NOW(), '$2a$12$Tnw7OFP0f/gmE8pAyUxbJ.avWXSl7sArAPwg27hiHjFSujRO97zcu', true)
ON CONFLICT (id) DO NOTHING;

-- ============================================================
-- 30 LOTES — comercio_id: a1b2c3d4-e5f6-7890-abcd-ef1234567801
-- (Supermaxi La Carolina — VERIFICADO)
-- Columnas: id, comercio_id, modalidad, estado, cantidad_kg,
--           precio_monto, precio_moneda, fecha_caducidad,
--           fecha_publicacion, descripcion, latitud, longitud,
--           categoria, creado_en, actualizado_en
-- ============================================================
INSERT INTO lotes_excedentes (
    id, comercio_id, modalidad, estado,
    cantidad_kg, precio_monto, precio_moneda,
    fecha_caducidad, fecha_publicacion, descripcion,
    latitud, longitud, categoria, creado_en, actualizado_en
) VALUES

-- FRUTAS_VERDURAS (6)
('d0000001-0000-0000-0000-000000000001',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 20.0, 1.50, 'USD',
 NOW() + INTERVAL '3 days', NOW(),
 'Bananas de exportación maduras, ideales para consumo inmediato o postres. Lote de excedente.',
 -0.1866, -78.4868, 'FRUTAS_VERDURAS', NOW(), NOW()),

('d0000001-0000-0000-0000-000000000002',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 15.0, 1.80, 'USD',
 NOW() + INTERVAL '2 days', NOW(),
 'Tomates cherry en bandeja, listos para ensaladas. Cosecha local fresca.',
 -0.1866, -78.4868, 'FRUTAS_VERDURAS', NOW(), NOW()),

('d0000001-0000-0000-0000-000000000003',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 10.0, 2.00, 'USD',
 NOW() + INTERVAL '4 days', NOW(),
 'Mix de pimientos rojos, amarillos y verdes. Perfectos para saltear o ensaladas.',
 -0.1866, -78.4868, 'FRUTAS_VERDURAS', NOW(), NOW()),

('d0000001-0000-0000-0000-000000000004',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 25.0, 2.50, 'USD',
 NOW() + INTERVAL '5 days', NOW(),
 'Manzanas importadas Fuji, dulces y crujientes. Lote con pequeñas imperfecciones estéticas.',
 -0.1866, -78.4868, 'FRUTAS_VERDURAS', NOW(), NOW()),

('d0000001-0000-0000-0000-000000000005',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 8.0, 1.20, 'USD',
 NOW() + INTERVAL '2 days', NOW(),
 'Espinaca fresca lavada y lista para usar. Alta en hierro y vitaminas.',
 -0.1866, -78.4868, 'FRUTAS_VERDURAS', NOW(), NOW()),

('d0000001-0000-0000-0000-000000000006',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 12.0, 3.00, 'USD',
 NOW() + INTERVAL '3 days', NOW(),
 'Aguacates Hass en punto de maduración ideal. Excedente de importación.',
 -0.1866, -78.4868, 'FRUTAS_VERDURAS', NOW(), NOW()),

-- LACTEOS (5)
('d0000001-0000-0000-0000-000000000007',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 50.0, 0.70, 'USD',
 NOW() + INTERVAL '2 days', NOW(),
 'Leche entera pasteurizada 1L, próxima a vencer. Ideal para cocinar o consumo inmediato.',
 -0.1866, -78.4868, 'LACTEOS', NOW(), NOW()),

('d0000001-0000-0000-0000-000000000008',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 20.0, 1.00, 'USD',
 NOW() + INTERVAL '3 days', NOW(),
 'Yogur natural sin azúcar añadida 500g. Próximo a vencer, en perfectas condiciones.',
 -0.1866, -78.4868, 'LACTEOS', NOW(), NOW()),

('d0000001-0000-0000-0000-000000000009',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 10.0, 1.50, 'USD',
 NOW() + INTERVAL '4 days', NOW(),
 'Queso fresco ecuatoriano artesanal 250g. Excedente de producción propia.',
 -0.1866, -78.4868, 'LACTEOS', NOW(), NOW()),

('d0000001-0000-0000-0000-000000000010',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 15.0, 0.90, 'USD',
 NOW() + INTERVAL '2 days', NOW(),
 'Crema de leche 250ml para cocina y repostería. Lote próximo a vencer.',
 -0.1866, -78.4868, 'LACTEOS', NOW(), NOW()),

('d0000001-0000-0000-0000-000000000011',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 5.0, 1.80, 'USD',
 NOW() + INTERVAL '7 days', NOW(),
 'Mantequilla sin sal 200g, calidad superior. Excedente de stock.',
 -0.1866, -78.4868, 'LACTEOS', NOW(), NOW()),

-- PANADERIA (4)
('d0000001-0000-0000-0000-000000000012',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 12.0, 1.20, 'USD',
 NOW() + INTERVAL '1 days', NOW(),
 'Pan de molde integral de producción diaria. Lote del día con descuento de cierre.',
 -0.1866, -78.4868, 'PANADERIA', NOW(), NOW()),

('d0000001-0000-0000-0000-000000000013',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 5.0, 2.50, 'USD',
 NOW() + INTERVAL '1 days', NOW(),
 'Croissants de mantequilla recién horneados del turno de la mañana. 6 unidades por caja.',
 -0.1866, -78.4868, 'PANADERIA', NOW(), NOW()),

('d0000001-0000-0000-0000-000000000014',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 6.0, 2.00, 'USD',
 NOW() + INTERVAL '1 days', NOW(),
 'Magdalenas de vainilla artesanales x12. Producción del día, perfectas para el desayuno.',
 -0.1866, -78.4868, 'PANADERIA', NOW(), NOW()),

('d0000001-0000-0000-0000-000000000015',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 8.0, 5.00, 'USD',
 NOW() + INTERVAL '2 days', NOW(),
 'Torta de chocolate 20cm. Excedente de producción del día, sin decorar.',
 -0.1866, -78.4868, 'PANADERIA', NOW(), NOW()),

-- CARNES (4)
('d0000001-0000-0000-0000-000000000016',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 8.0, 4.00, 'USD',
 NOW() + INTERVAL '2 days', NOW(),
 'Pechuga de pollo fresca sin hueso. Excedente del día, refrigerada.',
 -0.1866, -78.4868, 'CARNES', NOW(), NOW()),

('d0000001-0000-0000-0000-000000000017',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 10.0, 5.50, 'USD',
 NOW() + INTERVAL '2 days', NOW(),
 'Carne molida de res 80/20. Ideal para hamburguesas o salsas. Excedente refrigerado.',
 -0.1866, -78.4868, 'CARNES', NOW(), NOW()),

('d0000001-0000-0000-0000-000000000018',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 6.0, 4.50, 'USD',
 NOW() + INTERVAL '1 days', NOW(),
 'Filetes de tilapia frescos. Pesca del día, excedente de stock.',
 -0.1866, -78.4868, 'CARNES', NOW(), NOW()),

('d0000001-0000-0000-0000-000000000019',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 5.0, 3.00, 'USD',
 NOW() + INTERVAL '3 days', NOW(),
 'Salchichas artesanales de cerdo x10. Producción local, excedente de semana.',
 -0.1866, -78.4868, 'CARNES', NOW(), NOW()),

-- ABARROTES (5)
('d0000001-0000-0000-0000-000000000020',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 75.0, 4.00, 'USD',
 NOW() + INTERVAL '180 days', NOW(),
 'Arroz blanco grano largo 5kg. Primera calidad, excedente de bodega.',
 -0.1866, -78.4868, 'ABARROTES', NOW(), NOW()),

('d0000001-0000-0000-0000-000000000021',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 30.0, 1.50, 'USD',
 NOW() + INTERVAL '120 days', NOW(),
 'Lentejas verdes secas 1kg. Alta proteína vegetal, excedente de importación.',
 -0.1866, -78.4868, 'ABARROTES', NOW(), NOW()),

('d0000001-0000-0000-0000-000000000022',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 25.0, 0.80, 'USD',
 NOW() + INTERVAL '90 days', NOW(),
 'Espagueti de sémola de trigo duro 500g. Excedente de stock.',
 -0.1866, -78.4868, 'ABARROTES', NOW(), NOW()),

('d0000001-0000-0000-0000-000000000023',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 25.0, 2.20, 'USD',
 NOW() + INTERVAL '60 days', NOW(),
 'Aceite vegetal de girasol refinado 1L. Excedente de bodega.',
 -0.1866, -78.4868, 'ABARROTES', NOW(), NOW()),

('d0000001-0000-0000-0000-000000000024',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 20.0, 1.00, 'USD',
 NOW() + INTERVAL '60 days', NOW(),
 'Avena tradicional en hojuelas 500g. Sin azúcar, 100% natural.',
 -0.1866, -78.4868, 'ABARROTES', NOW(), NOW()),

-- COMIDA_PREPARADA (3)
('d0000001-0000-0000-0000-000000000025',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 4.0, 5.00, 'USD',
 NOW() + INTERVAL '1 days', NOW(),
 'Sushi fresco del día x12 piezas: 6 California rolls + 6 nigiri. Elaborado esta mañana.',
 -0.1866, -78.4868, 'COMIDA_PREPARADA', NOW(), NOW()),

('d0000001-0000-0000-0000-000000000026',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 3.0, 3.50, 'USD',
 NOW() + INTERVAL '1 days', NOW(),
 'Ensalada César con pollo grillado, crutones y aderezo. Porción para 2 personas.',
 -0.1866, -78.4868, 'COMIDA_PREPARADA', NOW(), NOW()),

('d0000001-0000-0000-0000-000000000027',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 1.8, 6.00, 'USD',
 NOW() + INTERVAL '1 days', NOW(),
 'Pollo asado al horno con hierbas. Lote del turno tarde con descuento de cierre.',
 -0.1866, -78.4868, 'COMIDA_PREPARADA', NOW(), NOW()),

-- BEBIDAS (3)
('d0000001-0000-0000-0000-000000000028',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 20.0, 1.50, 'USD',
 NOW() + INTERVAL '3 days', NOW(),
 'Jugo de naranja 100% natural 1L, recién exprimido. Sin conservantes.',
 -0.1866, -78.4868, 'BEBIDAS', NOW(), NOW()),

('d0000001-0000-0000-0000-000000000029',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 18.0, 2.00, 'USD',
 NOW() + INTERVAL '30 days', NOW(),
 'Six pack de agua con gas 500ml. Excedente de fin de semana.',
 -0.1866, -78.4868, 'BEBIDAS', NOW(), NOW()),

('d0000001-0000-0000-0000-000000000030',
 'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
 'VENTA', 'DISPONIBLE', 6.0, 3.00, 'USD',
 NOW() + INTERVAL '30 days', NOW(),
 'Pack de 4 latas de té helado sabor durazno 473ml. Sin azúcar añadida.',
 -0.1866, -78.4868, 'BEBIDAS', NOW(), NOW())

ON CONFLICT (id) DO NOTHING;

-- ============================================================
-- FOTOS — tabla lote_fotos
-- ============================================================
INSERT INTO lote_fotos (lote_id, url) VALUES
('d0000001-0000-0000-0000-000000000001', 'https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=800&q=80'),
('d0000001-0000-0000-0000-000000000002', 'https://images.unsplash.com/photo-1592841200221-a6898f307baa?w=800&q=80'),
('d0000001-0000-0000-0000-000000000003', 'https://images.unsplash.com/photo-1506807803488-8eafc15316c7?w=800&q=80'),
('d0000001-0000-0000-0000-000000000004', 'https://images.unsplash.com/photo-1567306226416-28f0efdc88ce?w=800&q=80'),
('d0000001-0000-0000-0000-000000000005', 'https://images.unsplash.com/photo-1576045057995-568f588f82fb?w=800&q=80'),
('d0000001-0000-0000-0000-000000000006', 'https://images.unsplash.com/photo-1523049673857-eb18f1d7b578?w=800&q=80'),
('d0000001-0000-0000-0000-000000000007', 'https://images.unsplash.com/photo-1550583724-b2692b85b150?w=800&q=80'),
('d0000001-0000-0000-0000-000000000008', 'https://images.unsplash.com/photo-1488477181946-6428a0291777?w=800&q=80'),
('d0000001-0000-0000-0000-000000000009', 'https://images.unsplash.com/photo-1486297678162-eb2a19b0a32d?w=800&q=80'),
('d0000001-0000-0000-0000-000000000010', 'https://images.unsplash.com/photo-1559598467-f8b76c8155d0?w=800&q=80'),
('d0000001-0000-0000-0000-000000000011', 'https://images.unsplash.com/photo-1589985270826-4b7bb135bc9d?w=800&q=80'),
('d0000001-0000-0000-0000-000000000012', 'https://images.unsplash.com/photo-1549931319-a545dcf3bc7c?w=800&q=80'),
('d0000001-0000-0000-0000-000000000013', 'https://images.unsplash.com/photo-1555507036-ab1f4038808a?w=800&q=80'),
('d0000001-0000-0000-0000-000000000014', 'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=800&q=80'),
('d0000001-0000-0000-0000-000000000015', 'https://images.unsplash.com/photo-1578985545062-69928b1d9587?w=800&q=80'),
('d0000001-0000-0000-0000-000000000016', 'https://images.unsplash.com/photo-1604503468506-a8da13d11d36?w=800&q=80'),
('d0000001-0000-0000-0000-000000000017', 'https://images.unsplash.com/photo-1603048588665-791ca8aea617?w=800&q=80'),
('d0000001-0000-0000-0000-000000000018', 'https://images.unsplash.com/photo-1519708227418-c8fd9a32b7a2?w=800&q=80'),
('d0000001-0000-0000-0000-000000000019', 'https://images.unsplash.com/photo-1615361200141-f45040f367be?w=800&q=80'),
('d0000001-0000-0000-0000-000000000020', 'https://images.unsplash.com/photo-1536304993881-ff86e0c9ef1d?w=800&q=80'),
('d0000001-0000-0000-0000-000000000021', 'https://images.unsplash.com/photo-1600423115367-87ea7661688f?w=800&q=80'),
('d0000001-0000-0000-0000-000000000022', 'https://images.unsplash.com/photo-1551462147-37885acc36f1?w=800&q=80'),
('d0000001-0000-0000-0000-000000000023', 'https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=800&q=80'),
('d0000001-0000-0000-0000-000000000024', 'https://images.unsplash.com/photo-1614961233913-a5113a4a34ed?w=800&q=80'),
('d0000001-0000-0000-0000-000000000025', 'https://images.unsplash.com/photo-1579871494447-9811cf80d66c?w=800&q=80'),
('d0000001-0000-0000-0000-000000000026', 'https://images.unsplash.com/photo-1540420773420-3366772f4999?w=800&q=80'),
('d0000001-0000-0000-0000-000000000027', 'https://images.unsplash.com/photo-1598103442097-8b74394b95c3?w=800&q=80'),
('d0000001-0000-0000-0000-000000000028', 'https://images.unsplash.com/photo-1600271886742-f049cd451bba?w=800&q=80'),
('d0000001-0000-0000-0000-000000000029', 'https://images.unsplash.com/photo-1523362628745-0c100150b504?w=800&q=80'),
('d0000001-0000-0000-0000-000000000030', 'https://images.unsplash.com/photo-1556679343-c7306c1976bc?w=800&q=80')
ON CONFLICT DO NOTHING;
