-- ══════════════════════════════════════════════════════════
-- DATOS DE DEMO — FoodLink Feria
-- Password de todos los usuarios: FoodLink2025!
-- BCrypt hash de FoodLink2025! (generado con BCryptPasswordEncoder(12)
-- y verificado con encoder.matches("FoodLink2025!", hash) == true):
-- $2a$12$Tnw7OFP0f/gmE8pAyUxbJ.avWXSl7sArAPwg27hiHjFSujRO97zcu
-- ══════════════════════════════════════════════════════════

-- ══════════════════════════════════════════════════════════
-- ADMINISTRADOR
-- ══════════════════════════════════════════════════════════

-- El administrador no tiene tabla propia — vive en Spring Security
-- con ROLE_ADMIN. En tu sistema actual los roles vienen del JWT.
-- Crea el admin directamente en la tabla que uses para auth.
-- Si no tienes tabla de admins, omite esta sección y usa
-- un usuario hardcodeado en SecurityConfig para la demo.

-- ══════════════════════════════════════════════════════════
-- COMERCIOS (3 establecimientos verificados)
-- ══════════════════════════════════════════════════════════

INSERT INTO comercios (
    id, ruc, nombre, telefono, email,
    estado, provincia, ciudad,
    calle_principal, calle_secundaria, referencia,
    password_hash, activo, fecha_registro
) VALUES
(
    'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
    '1791000005001',
    'Supermaxi La Carolina',
    '0991234567',
    'supermaxi.carolina@foodlink.ec',
    'VERIFICADO',
    'Pichincha', 'Quito',
    'Av. Naciones Unidas', 'Av. Amazonas',
    'Frente al Parque La Carolina',
    '$2a$12$Tnw7OFP0f/gmE8pAyUxbJ.avWXSl7sArAPwg27hiHjFSujRO97zcu',
    true,
    NOW() - INTERVAL '30 days'
),
(
    'a1b2c3d4-e5f6-7890-abcd-ef1234567802',
    '1791000013001',
    'Panadería El Horno Quiteño',
    '0987654321',
    'elhorno@foodlink.ec',
    'VERIFICADO',
    'Pichincha', 'Quito',
    'Calle Mejía', 'Calle Venezuela',
    'Centro Histórico, local 12',
    '$2a$12$Tnw7OFP0f/gmE8pAyUxbJ.avWXSl7sArAPwg27hiHjFSujRO97zcu',
    true,
    NOW() - INTERVAL '25 days'
),
(
    'a1b2c3d4-e5f6-7890-abcd-ef1234567803',
    '1791000021001',
    'Restaurante La Choza',
    '0976543210',
    'lachoza@foodlink.ec',
    'PENDIENTE_VERIFICACION',
    'Pichincha', 'Quito',
    'Av. 6 de Diciembre', 'Av. Colón',
    'Edificio azul, planta baja',
    '$2a$12$Tnw7OFP0f/gmE8pAyUxbJ.avWXSl7sArAPwg27hiHjFSujRO97zcu',
    true,
    NOW() - INTERVAL '2 days'
);

-- ══════════════════════════════════════════════════════════
-- BENEFICIARIOS (2 organizaciones)
-- ══════════════════════════════════════════════════════════

INSERT INTO beneficiarios (
    id, nombre, ruc, email, telefono,
    estado_verificacion, provincia, ciudad,
    calle_principal, calle_secundaria, referencia,
    password_hash, activo, fecha_registro
) VALUES
(
    'b1b2c3d4-e5f6-7890-abcd-ef1234567801',
    'Fundación Alimentando Quito',
    '1791000048001',
    'contacto@alimentandoquito.org',
    '0965432109',
    'VERIFICADO',
    'Pichincha', 'Quito',
    'Calle Ambato', 'Calle Cuenca',
    'Sector La Mariscal, casa verde',
    '$2a$12$Tnw7OFP0f/gmE8pAyUxbJ.avWXSl7sArAPwg27hiHjFSujRO97zcu',
    true,
    NOW() - INTERVAL '20 days'
),
(
    'b1b2c3d4-e5f6-7890-abcd-ef1234567802',
    'ONG Manos Solidarias',
    '1791000056001',
    'info@manossolidarias.org',
    '0967891234',
    'PENDIENTE',
    'Pichincha', 'Quito',
    'Av. Pichincha', 'Calle Manabí',
    'Sector Centro, segundo piso',
    '$2a$12$Tnw7OFP0f/gmE8pAyUxbJ.avWXSl7sArAPwg27hiHjFSujRO97zcu',
    true,
    NOW() - INTERVAL '1 days'
);

-- ══════════════════════════════════════════════════════════
-- COMPRADORES (2 personas naturales)
-- ══════════════════════════════════════════════════════════

INSERT INTO compradores (
    id, cedula, nombre, apellido, email,
    telefono, activo, password_hash, fecha_registro
) VALUES
(
    'c1b2c3d4-e5f6-7890-abcd-ef1234567801',
    '1710034065',
    'Carlos',
    'Mendoza',
    'carlos.mendoza@gmail.com',
    '0991122334',
    true,
    '$2a$12$Tnw7OFP0f/gmE8pAyUxbJ.avWXSl7sArAPwg27hiHjFSujRO97zcu',
    NOW() - INTERVAL '15 days'
),
(
    'c1b2c3d4-e5f6-7890-abcd-ef1234567802',
    '1720045176',
    'María',
    'González',
    'maria.gonzalez@gmail.com',
    '0982233445',
    true,
    '$2a$12$Tnw7OFP0f/gmE8pAyUxbJ.avWXSl7sArAPwg27hiHjFSujRO97zcu',
    NOW() - INTERVAL '10 days'
);

-- ══════════════════════════════════════════════════════════
-- LOTES DE EXCEDENTES (5 lotes en diferentes estados)
-- ══════════════════════════════════════════════════════════

INSERT INTO lotes_excedentes (
    id, comercio_id, modalidad, estado,
    cantidad_kg, precio_monto, precio_moneda,
    fecha_caducidad, fecha_publicacion,
    descripcion,
    beneficiario_reserva_id, inicio_reserva,
    creado_en, actualizado_en
) VALUES
(
    'd1b2c3d4-e5f6-7890-abcd-ef1234567801',
    'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
    'VENTA', 'DISPONIBLE',
    15.5, 3.50, 'USD',
    NOW() + INTERVAL '2 days',
    NOW() - INTERVAL '2 hours',
    'Pan integral, baguettes y croissants del día. Excelente calidad.',
    NULL, NULL,
    NOW() - INTERVAL '2 hours', NOW() - INTERVAL '2 hours'
),
(
    'd1b2c3d4-e5f6-7890-abcd-ef1234567802',
    'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
    'DONACION', 'DISPONIBLE',
    25.0, NULL, NULL,
    NOW() + INTERVAL '1 days',
    NOW() - INTERVAL '3 hours',
    'Frutas y verduras frescas: manzanas, plátanos, zanahorias y tomates.',
    NULL, NULL,
    NOW() - INTERVAL '3 hours', NOW() - INTERVAL '3 hours'
),
(
    'd1b2c3d4-e5f6-7890-abcd-ef1234567803',
    'a1b2c3d4-e5f6-7890-abcd-ef1234567802',
    'VENTA', 'DISPONIBLE',
    8.0, 2.00, 'USD',
    NOW() + INTERVAL '3 days',
    NOW() - INTERVAL '1 hours',
    'Pan de yuca, empanadas y pan de leche recién horneados.',
    NULL, NULL,
    NOW() - INTERVAL '1 hours', NOW() - INTERVAL '1 hours'
),
(
    'd1b2c3d4-e5f6-7890-abcd-ef1234567804',
    'a1b2c3d4-e5f6-7890-abcd-ef1234567801',
    'DONACION', 'RESERVADO',
    12.0, NULL, NULL,
    NOW() + INTERVAL '1 days',
    NOW() - INTERVAL '5 hours',
    'Lácteos: leche, queso fresco y yogur próximos a vencer.',
    'b1b2c3d4-e5f6-7890-abcd-ef1234567801',
    NOW() - INTERVAL '20 minutes',
    NOW() - INTERVAL '5 hours', NOW() - INTERVAL '20 minutes'
),
(
    'd1b2c3d4-e5f6-7890-abcd-ef1234567805',
    'a1b2c3d4-e5f6-7890-abcd-ef1234567802',
    'VENTA', 'ENTREGADO',
    5.0, 1.50, 'USD',
    NOW() + INTERVAL '4 days',
    NOW() - INTERVAL '2 days',
    'Galletas artesanales y bizcochos surtidos.',
    NULL, NULL,
    NOW() - INTERVAL '2 days', NOW() - INTERVAL '1 days'
);

-- ══════════════════════════════════════════════════════════
-- FOTOS DE LOTES
-- ══════════════════════════════════════════════════════════

INSERT INTO lote_fotos (lote_id, url) VALUES
('d1b2c3d4-e5f6-7890-abcd-ef1234567801', 'https://images.unsplash.com/photo-1549931319-a545dcf3bc73?w=400'),
('d1b2c3d4-e5f6-7890-abcd-ef1234567802', 'https://images.unsplash.com/photo-1610348725531-843dff563e2c?w=400'),
('d1b2c3d4-e5f6-7890-abcd-ef1234567803', 'https://images.unsplash.com/photo-1509440159596-0249088772ff?w=400'),
('d1b2c3d4-e5f6-7890-abcd-ef1234567804', 'https://images.unsplash.com/photo-1550583724-b2692b85b150?w=400'),
('d1b2c3d4-e5f6-7890-abcd-ef1234567805', 'https://images.unsplash.com/photo-1558961363-fa8fdf82db35?w=400');

-- ══════════════════════════════════════════════════════════
-- MÉTRICAS DE IMPACTO (lote entregado + histórico)
-- ══════════════════════════════════════════════════════════

INSERT INTO impacto_metricas (
    lote_id, cantidad_kg, co2_evitado_kg,
    personas_beneficiadas, fecha_entrega
) VALUES
(
    'd1b2c3d4-e5f6-7890-abcd-ef1234567805',
    5.0, 12.5, 10,
    NOW() - INTERVAL '1 days'
),
(
    uuid_generate_v4(), 18.5, 46.25, 37,
    NOW() - INTERVAL '5 days'
),
(
    uuid_generate_v4(), 30.0, 75.0, 60,
    NOW() - INTERVAL '10 days'
),
(
    uuid_generate_v4(), 22.3, 55.75, 45,
    NOW() - INTERVAL '15 days'
),
(
    uuid_generate_v4(), 45.0, 112.5, 90,
    NOW() - INTERVAL '20 days'
);

-- ══════════════════════════════════════════════════════════
-- VERIFICACIÓN FINAL
-- ══════════════════════════════════════════════════════════
-- Después de ejecutar este archivo deberías ver:
-- SELECT COUNT(*) FROM comercios;           → 3
-- SELECT COUNT(*) FROM beneficiarios;       → 2
-- SELECT COUNT(*) FROM compradores;         → 2
-- SELECT COUNT(*) FROM lotes_excedentes;    → 5
-- SELECT COUNT(*) FROM impacto_metricas;    → 5
-- GET /api/v1/impacto/dashboard → totalKgRescatados: 120.8
--                                  totalPersonasBeneficiadas: 242
--                                  totalCo2EvitadoKg: 302.0