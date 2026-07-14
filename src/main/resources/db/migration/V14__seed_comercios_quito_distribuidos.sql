-- ============================================================
-- V14__seed_comercios_quito_distribuidos.sql
-- 10 comercios nuevos distribuidos por sectores reales de Quito
-- + 30 lotes con coordenadas geográficas reales
-- ============================================================

INSERT INTO comercios (id, ruc, nombre, telefono, email, estado, provincia, ciudad,
    calle_principal, calle_secundaria, referencia, fecha_registro, password_hash, activo)
VALUES
('c0000001-0000-0000-0000-000000000001',
 '1791100001001', 'Mercado El Batán', '0998001001', 'mercado.batan@foodlink.ec',
 'VERIFICADO', 'Pichincha', 'Quito', 'Av. Eloy Alfaro', 'Calle de los Eucaliptos',
 'Sector El Batán, frente al mercado municipal',
 NOW(), '$2a$12$Tnw7OFP0f/gmE8pAyUxbJ.avWXSl7sArAPwg27hiHjFSujRO97zcu', true),

('c0000001-0000-0000-0000-000000000002',
 '1791100009001', 'Panadería La Floresta', '0998001002', 'panaderia.floresta@foodlink.ec',
 'VERIFICADO', 'Pichincha', 'Quito', 'Av. Isabela Católica', 'Madrid',
 'Sector La Floresta, local esquinero',
 NOW(), '$2a$12$Tnw7OFP0f/gmE8pAyUxbJ.avWXSl7sArAPwg27hiHjFSujRO97zcu', true),

('c0000001-0000-0000-0000-000000000003',
 '1791100017001', 'Minimarket La Mariscal', '0998001003', 'minimarket.mariscal@foodlink.ec',
 'VERIFICADO', 'Pichincha', 'Quito', 'Av. Amazonas', 'Av. Santa María',
 'La Mariscal, junto a parque El Ejido norte',
 NOW(), '$2a$12$Tnw7OFP0f/gmE8pAyUxbJ.avWXSl7sArAPwg27hiHjFSujRO97zcu', true),

('c0000001-0000-0000-0000-000000000004',
 '1791100025001', 'Tienda San Roque', '0998001004', 'tienda.sanroque@foodlink.ec',
 'VERIFICADO', 'Pichincha', 'Quito', 'Av. 24 de Mayo', 'Calle Chimborazo',
 'Mercado San Roque, puerta norte',
 NOW(), '$2a$12$Tnw7OFP0f/gmE8pAyUxbJ.avWXSl7sArAPwg27hiHjFSujRO97zcu', true),

('c0000001-0000-0000-0000-000000000005',
 '1791100033001', 'Supermercado Chillogallo', '0998001005', 'super.chillogallo@foodlink.ec',
 'VERIFICADO', 'Pichincha', 'Quito', 'Av. Mariscal Sucre', 'Calle Inti Ñan',
 'Sector Chillogallo, frente al parque central',
 NOW(), '$2a$12$Tnw7OFP0f/gmE8pAyUxbJ.avWXSl7sArAPwg27hiHjFSujRO97zcu', true),

('c0000001-0000-0000-0000-000000000006',
 '1791100041001', 'Despensa Solanda', '0998001006', 'despensa.solanda@foodlink.ec',
 'VERIFICADO', 'Pichincha', 'Quito', 'Av. Teniente Hugo Ortiz', 'Calle Cusubamba',
 'Sector Solanda, local comercial planta baja',
 NOW(), '$2a$12$Tnw7OFP0f/gmE8pAyUxbJ.avWXSl7sArAPwg27hiHjFSujRO97zcu', true),

('c0000001-0000-0000-0000-000000000007',
 '1791100059001', 'Supermarket Sangolquí', '0998001007', 'super.sangolqui@foodlink.ec',
 'VERIFICADO', 'Pichincha', 'Sangolquí', 'Av. General Enríquez', 'Calle Luis Cordero',
 'Centro de Sangolquí, junto al mercado',
 NOW(), '$2a$12$Tnw7OFP0f/gmE8pAyUxbJ.avWXSl7sArAPwg27hiHjFSujRO97zcu', true),

('c0000001-0000-0000-0000-000000000008',
 '1791100067001', 'Deli Cumbayá', '0998001008', 'deli.cumbaya@foodlink.ec',
 'VERIFICADO', 'Pichincha', 'Cumbayá', 'Av. Interoceánica', 'Calle Francisco de Orellana',
 'Cumbayá, frente al CC Cumbayá',
 NOW(), '$2a$12$Tnw7OFP0f/gmE8pAyUxbJ.avWXSl7sArAPwg27hiHjFSujRO97zcu', true),

('c0000001-0000-0000-0000-000000000009',
 '1791100075001', 'Mercado Cotocollao', '0998001009', 'mercado.cotocollao@foodlink.ec',
 'VERIFICADO', 'Pichincha', 'Quito', 'Av. La Prensa', 'Calle Fernández Salvador',
 'Sector Cotocollao, junto a la iglesia',
 NOW(), '$2a$12$Tnw7OFP0f/gmE8pAyUxbJ.avWXSl7sArAPwg27hiHjFSujRO97zcu', true),

('c0000001-0000-0000-0000-000000000010',
 '1791100083001', 'FreshMart Quitumbe', '0998001010', 'freshmart.quitumbe@foodlink.ec',
 'VERIFICADO', 'Pichincha', 'Quito', 'Av. Quitumbe Ñan', 'Calle Guamaní',
 'Sector Quitumbe, frente al terminal',
 NOW(), '$2a$12$Tnw7OFP0f/gmE8pAyUxbJ.avWXSl7sArAPwg27hiHjFSujRO97zcu', true)

ON CONFLICT (id) DO NOTHING;

-- ============================================================
-- LOTES — 3 por comercio = 30 lotes
-- ============================================================
INSERT INTO lotes_excedentes (
    id, comercio_id, modalidad, estado,
    cantidad_kg, precio_monto, precio_moneda,
    fecha_caducidad, fecha_publicacion, descripcion,
    latitud, longitud, categoria, creado_en, actualizado_en
) VALUES

-- MERCADO EL BATÁN (-0.1750, -78.4800)
('e0000001-0000-0000-0000-000000000001', 'c0000001-0000-0000-0000-000000000001',
 'VENTA', 'DISPONIBLE', 30.0, 1.00, 'USD',
 NOW() + INTERVAL '2 days', NOW(),
 'Choclos tiernos de la Sierra, cosecha directa del productor. Ideales para sopa o en choclo con queso.',
 -0.1750, -78.4800, 'FRUTAS_VERDURAS', NOW(), NOW()),

('e0000001-0000-0000-0000-000000000002', 'c0000001-0000-0000-0000-000000000001',
 'VENTA', 'DISPONIBLE', 8.0, 1.80, 'USD',
 NOW() + INTERVAL '4 days', NOW(),
 'Queso de hoja tradicional serrano. Producción local, excedente de la semana.',
 -0.1750, -78.4800, 'LACTEOS', NOW(), NOW()),

('e0000001-0000-0000-0000-000000000003', 'c0000001-0000-0000-0000-000000000001',
 'VENTA', 'DISPONIBLE', 100.0, 2.50, 'USD',
 NOW() + INTERVAL '7 days', NOW(),
 'Papas chola frescas de Carchi. Saco de 5kg, perfectas para cualquier preparación.',
 -0.1750, -78.4800, 'FRUTAS_VERDURAS', NOW(), NOW()),

-- PANADERÍA LA FLORESTA (-0.2100, -78.4780)
('e0000001-0000-0000-0000-000000000004', 'c0000001-0000-0000-0000-000000000002',
 'VENTA', 'DISPONIBLE', 4.0, 1.50, 'USD',
 NOW() + INTERVAL '1 days', NOW(),
 'Pandeyuca recién horneado, esponjoso y crujiente. 8 unidades por bolsa.',
 -0.2100, -78.4780, 'PANADERIA', NOW(), NOW()),

('e0000001-0000-0000-0000-000000000005', 'c0000001-0000-0000-0000-000000000002',
 'VENTA', 'DISPONIBLE', 3.0, 2.00, 'USD',
 NOW() + INTERVAL '1 days', NOW(),
 'Empanadas de morocho rellenas de carne y papas. Horneadas esta mañana.',
 -0.2100, -78.4780, 'COMIDA_PREPARADA', NOW(), NOW()),

('e0000001-0000-0000-0000-000000000006', 'c0000001-0000-0000-0000-000000000002',
 'VENTA', 'DISPONIBLE', 5.0, 1.80, 'USD',
 NOW() + INTERVAL '2 days', NOW(),
 'Bizcochos artesanales de Cayambe. Dorados y crocantes, ideales para el café.',
 -0.2100, -78.4780, 'PANADERIA', NOW(), NOW()),

-- MINIMARKET LA MARISCAL (-0.2100, -78.4978)
('e0000001-0000-0000-0000-000000000007', 'c0000001-0000-0000-0000-000000000003',
 'VENTA', 'DISPONIBLE', 6.0, 2.50, 'USD',
 NOW() + INTERVAL '1 days', NOW(),
 'Mix de frutas tropicales: mango, papaya, piña y maracuyá. Cortada y lista para servir.',
 -0.2100, -78.4978, 'COMIDA_PREPARADA', NOW(), NOW()),

('e0000001-0000-0000-0000-000000000008', 'c0000001-0000-0000-0000-000000000003',
 'VENTA', 'DISPONIBLE', 4.0, 3.50, 'USD',
 NOW() + INTERVAL '1 days', NOW(),
 'Pack de 4 sándwiches en pan integral con jamón y queso. Elaborados esta mañana, refrigerados.',
 -0.2100, -78.4978, 'COMIDA_PREPARADA', NOW(), NOW()),

('e0000001-0000-0000-0000-000000000009', 'c0000001-0000-0000-0000-000000000003',
 'VENTA', 'DISPONIBLE', 20.0, 1.50, 'USD',
 NOW() + INTERVAL '2 days', NOW(),
 'Infusión fría de jamaica sin azúcar 1L. Producción artesanal del día.',
 -0.2100, -78.4978, 'BEBIDAS', NOW(), NOW()),

-- TIENDA SAN ROQUE — CENTRO HISTÓRICO (-0.2296, -78.5199)
('e0000001-0000-0000-0000-000000000010', 'c0000001-0000-0000-0000-000000000004',
 'VENTA', 'DISPONIBLE', 15.0, 2.00, 'USD',
 NOW() + INTERVAL '2 days', NOW(),
 'Menestra de frijol negro cocinada, lista para calentar. Porción familiar 1kg.',
 -0.2296, -78.5199, 'COMIDA_PREPARADA', NOW(), NOW()),

('e0000001-0000-0000-0000-000000000011', 'c0000001-0000-0000-0000-000000000004',
 'VENTA', 'DISPONIBLE', 8.0, 4.00, 'USD',
 NOW() + INTERVAL '1 days', NOW(),
 'Fritada tradicional quiteña con mote y tostado. Lista para servir.',
 -0.2296, -78.5199, 'COMIDA_PREPARADA', NOW(), NOW()),

('e0000001-0000-0000-0000-000000000012', 'c0000001-0000-0000-0000-000000000004',
 'VENTA', 'DISPONIBLE', 25.0, 1.50, 'USD',
 NOW() + INTERVAL '3 days', NOW(),
 'Naranjillas de la Costa, maduras y jugosas. Perfectas para jugo o colada.',
 -0.2296, -78.5199, 'FRUTAS_VERDURAS', NOW(), NOW()),

-- SUPERMERCADO CHILLOGALLO (-0.3000, -78.5300)
('e0000001-0000-0000-0000-000000000013', 'c0000001-0000-0000-0000-000000000005',
 'VENTA', 'DISPONIBLE', 1.8, 5.50, 'USD',
 NOW() + INTERVAL '2 days', NOW(),
 'Pollo entero fresco de granja 1.8kg. Excedente del día, refrigerado.',
 -0.3000, -78.5300, 'CARNES', NOW(), NOW()),

('e0000001-0000-0000-0000-000000000014', 'c0000001-0000-0000-0000-000000000005',
 'VENTA', 'DISPONIBLE', 17.5, 0.90, 'USD',
 NOW() + INTERVAL '60 days', NOW(),
 'Fideo de huevo artesanal 500g. Excedente de producción.',
 -0.3000, -78.5300, 'ABARROTES', NOW(), NOW()),

('e0000001-0000-0000-0000-000000000015', 'c0000001-0000-0000-0000-000000000005',
 'VENTA', 'DISPONIBLE', 25.0, 1.00, 'USD',
 NOW() + INTERVAL '3 days', NOW(),
 'Bebida de soya sin lactosa 1L. Lote próximo a vencer, en perfectas condiciones.',
 -0.3000, -78.5300, 'LACTEOS', NOW(), NOW()),

-- DESPENSA SOLANDA (-0.3200, -78.5400)
('e0000001-0000-0000-0000-000000000016', 'c0000001-0000-0000-0000-000000000006',
 'VENTA', 'DISPONIBLE', 7.5, 0.80, 'USD',
 NOW() + INTERVAL '2 days', NOW(),
 'Arroz con leche casero en envase individual 250g. Producción del día.',
 -0.3200, -78.5400, 'COMIDA_PREPARADA', NOW(), NOW()),

('e0000001-0000-0000-0000-000000000017', 'c0000001-0000-0000-0000-000000000006',
 'VENTA', 'DISPONIBLE', 40.0, 0.80, 'USD',
 NOW() + INTERVAL '2 days', NOW(),
 'Bananas guineo bien maduras, ideales para maduros fritos o colada morada.',
 -0.3200, -78.5400, 'FRUTAS_VERDURAS', NOW(), NOW()),

('e0000001-0000-0000-0000-000000000018', 'c0000001-0000-0000-0000-000000000006',
 'VENTA', 'DISPONIBLE', 30.0, 1.80, 'USD',
 NOW() + INTERVAL '90 days', NOW(),
 'Aceite de palma refinado 1L. Botella de 1 litro, excedente de stock.',
 -0.3200, -78.5400, 'ABARROTES', NOW(), NOW()),

-- SUPERMARKET SANGOLQUÍ (-0.3340, -78.4480)
('e0000001-0000-0000-0000-000000000019', 'c0000001-0000-0000-0000-000000000007',
 'VENTA', 'DISPONIBLE', 1.5, 12.00, 'USD',
 NOW() + INTERVAL '1 days', NOW(),
 'Cuy asado tradicional de Sangolquí con papas y salsa de maní.',
 -0.3340, -78.4480, 'COMIDA_PREPARADA', NOW(), NOW()),

('e0000001-0000-0000-0000-000000000020', 'c0000001-0000-0000-0000-000000000007',
 'VENTA', 'DISPONIBLE', 10.0, 1.00, 'USD',
 NOW() + INTERVAL '1 days', NOW(),
 'Morocho caliente con canela y leche 500ml. Producción artesanal del día.',
 -0.3340, -78.4480, 'BEBIDAS', NOW(), NOW()),

('e0000001-0000-0000-0000-000000000021', 'c0000001-0000-0000-0000-000000000007',
 'VENTA', 'DISPONIBLE', 9.0, 1.50, 'USD',
 NOW() + INTERVAL '4 days', NOW(),
 'Uvillas de cosecha local del Valle. Dulces y ácidas, ricas en vitamina C.',
 -0.3340, -78.4480, 'FRUTAS_VERDURAS', NOW(), NOW()),

-- DELI CUMBAYÁ (-0.2000, -78.4380)
('e0000001-0000-0000-0000-000000000022', 'c0000001-0000-0000-0000-000000000008',
 'VENTA', 'DISPONIBLE', 3.0, 3.50, 'USD',
 NOW() + INTERVAL '1 days', NOW(),
 'Quiche artesanal de vegetales y queso. Porción individual, horneada hoy.',
 -0.2000, -78.4380, 'COMIDA_PREPARADA', NOW(), NOW()),

('e0000001-0000-0000-0000-000000000023', 'c0000001-0000-0000-0000-000000000008',
 'VENTA', 'DISPONIBLE', 4.8, 3.00, 'USD',
 NOW() + INTERVAL '30 days', NOW(),
 'Granola casera 400g con avena, miel, nueces y frutos rojos. Sin conservantes.',
 -0.2000, -78.4380, 'ABARROTES', NOW(), NOW()),

('e0000001-0000-0000-0000-000000000024', 'c0000001-0000-0000-0000-000000000008',
 'VENTA', 'DISPONIBLE', 5.25, 2.50, 'USD',
 NOW() + INTERVAL '7 days', NOW(),
 'Bebida fermentada artesanal de jengibre y limón 350ml. Lote de producción semanal.',
 -0.2000, -78.4380, 'BEBIDAS', NOW(), NOW()),

-- MERCADO COTOCOLLAO (-0.1067, -78.5020)
('e0000001-0000-0000-0000-000000000025', 'c0000001-0000-0000-0000-000000000009',
 'VENTA', 'DISPONIBLE', 12.5, 1.20, 'USD',
 NOW() + INTERVAL '30 days', NOW(),
 'Habas tostadas con sal 500g. Snack andino tradicional, producción local.',
 -0.1067, -78.5020, 'ABARROTES', NOW(), NOW()),

('e0000001-0000-0000-0000-000000000026', 'c0000001-0000-0000-0000-000000000009',
 'VENTA', 'DISPONIBLE', 30.0, 1.00, 'USD',
 NOW() + INTERVAL '5 days', NOW(),
 'Mellocos andinos frescos 1kg. Excelentes para sopa o como guarnición.',
 -0.1067, -78.5020, 'FRUTAS_VERDURAS', NOW(), NOW()),

('e0000001-0000-0000-0000-000000000027', 'c0000001-0000-0000-0000-000000000009',
 'VENTA', 'DISPONIBLE', 12.0, 2.50, 'USD',
 NOW() + INTERVAL '2 days', NOW(),
 'Caldo de gallina criolla cocinado a fuego lento 1L. Listo para calentar y servir.',
 -0.1067, -78.5020, 'COMIDA_PREPARADA', NOW(), NOW()),

-- FRESHMART QUITUMBE (-0.3500, -78.5500)
('e0000001-0000-0000-0000-000000000028', 'c0000001-0000-0000-0000-000000000010',
 'VENTA', 'DISPONIBLE', 40.0, 2.00, 'USD',
 NOW() + INTERVAL '120 days', NOW(),
 'Fréjol canario seco 2kg de excelente calidad. Excedente de importación.',
 -0.3500, -78.5500, 'ABARROTES', NOW(), NOW()),

('e0000001-0000-0000-0000-000000000029', 'c0000001-0000-0000-0000-000000000010',
 'VENTA', 'DISPONIBLE', 10.0, 3.50, 'USD',
 NOW() + INTERVAL '1 days', NOW(),
 'Tilapia entera fresca 1kg sin eviscerar. Pesca del día de los viveros del sur.',
 -0.3500, -78.5500, 'CARNES', NOW(), NOW()),

('e0000001-0000-0000-0000-000000000030', 'c0000001-0000-0000-0000-000000000010',
 'VENTA', 'DISPONIBLE', 25.0, 0.80, 'USD',
 NOW() + INTERVAL '5 days', NOW(),
 'Plátanos verdes de la Costa x5. Perfectos para patacones, chifles o seco.',
 -0.3500, -78.5500, 'FRUTAS_VERDURAS', NOW(), NOW())

ON CONFLICT (id) DO NOTHING;

-- ============================================================
-- FOTOS
-- ============================================================
INSERT INTO lote_fotos (lote_id, url) VALUES
('e0000001-0000-0000-0000-000000000001', 'https://images.unsplash.com/photo-1602491453631-e2a5ad90a131?w=800&q=80'),
('e0000001-0000-0000-0000-000000000002', 'https://images.unsplash.com/photo-1486297678162-eb2a19b0a32d?w=800&q=80'),
('e0000001-0000-0000-0000-000000000003', 'https://images.unsplash.com/photo-1518977676601-b53f82aba655?w=800&q=80'),
('e0000001-0000-0000-0000-000000000004', 'https://images.unsplash.com/photo-1558961363-fa8fdf82db35?w=800&q=80'),
('e0000001-0000-0000-0000-000000000005', 'https://images.unsplash.com/photo-1604908176997-125f25cc6f3d?w=800&q=80'),
('e0000001-0000-0000-0000-000000000006', 'https://images.unsplash.com/photo-1549931319-a545dcf3bc7c?w=800&q=80'),
('e0000001-0000-0000-0000-000000000007', 'https://images.unsplash.com/photo-1490474418585-ba9bad8fd0ea?w=800&q=80'),
('e0000001-0000-0000-0000-000000000008', 'https://images.unsplash.com/photo-1539252554935-eeedd081cffa?w=800&q=80'),
('e0000001-0000-0000-0000-000000000009', 'https://images.unsplash.com/photo-1595981234058-a9302fb97229?w=800&q=80'),
('e0000001-0000-0000-0000-000000000010', 'https://images.unsplash.com/photo-1515543904379-3d757afe72e4?w=800&q=80'),
('e0000001-0000-0000-0000-000000000011', 'https://images.unsplash.com/photo-1529042410759-befb1204b468?w=800&q=80'),
('e0000001-0000-0000-0000-000000000012', 'https://images.unsplash.com/photo-1551018612-9715965d33f4?w=800&q=80'),
('e0000001-0000-0000-0000-000000000013', 'https://images.unsplash.com/photo-1587593810167-a84920ea0781?w=800&q=80'),
('e0000001-0000-0000-0000-000000000014', 'https://images.unsplash.com/photo-1551462147-37885acc36f1?w=800&q=80'),
('e0000001-0000-0000-0000-000000000015', 'https://images.unsplash.com/photo-1550583724-b2692b85b150?w=800&q=80'),
('e0000001-0000-0000-0000-000000000016', 'https://images.unsplash.com/photo-1562802378-063ec186a863?w=800&q=80'),
('e0000001-0000-0000-0000-000000000017', 'https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=800&q=80'),
('e0000001-0000-0000-0000-000000000018', 'https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=800&q=80'),
('e0000001-0000-0000-0000-000000000019', 'https://images.unsplash.com/photo-1555939594-58d7cb561ad1?w=800&q=80'),
('e0000001-0000-0000-0000-000000000020', 'https://images.unsplash.com/photo-1563227812-0ea4c22e6cc8?w=800&q=80'),
('e0000001-0000-0000-0000-000000000021', 'https://images.unsplash.com/photo-1596591868231-05e808fd131d?w=800&q=80'),
('e0000001-0000-0000-0000-000000000022', 'https://images.unsplash.com/photo-1490645935967-10de6ba17061?w=800&q=80'),
('e0000001-0000-0000-0000-000000000023', 'https://images.unsplash.com/photo-1517982040918-b776bff47e50?w=800&q=80'),
('e0000001-0000-0000-0000-000000000024', 'https://images.unsplash.com/photo-1557142046-c704a3adf364?w=800&q=80'),
('e0000001-0000-0000-0000-000000000025', 'https://images.unsplash.com/photo-1577003833619-76bbd7f82948?w=800&q=80'),
('e0000001-0000-0000-0000-000000000026', 'https://images.unsplash.com/photo-1518977676601-b53f82aba655?w=800&q=80'),
('e0000001-0000-0000-0000-000000000027', 'https://images.unsplash.com/photo-1547592166-23ac45744acd?w=800&q=80'),
('e0000001-0000-0000-0000-000000000028', 'https://images.unsplash.com/photo-1515543904379-3d757afe72e4?w=800&q=80'),
('e0000001-0000-0000-0000-000000000029', 'https://images.unsplash.com/photo-1519708227418-c8fd9a32b7a2?w=800&q=80'),
('e0000001-0000-0000-0000-000000000030', 'https://images.unsplash.com/photo-1481349518771-20055b2a7b24?w=800&q=80')
ON CONFLICT DO NOTHING;
