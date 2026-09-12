-- ============================================================
-- URBAN STYLE
-- Base de datos: urban_style
-- Versión: 2.0
-- ============================================================

-- ============================================================
-- 1. CREACIÓN DE LA BASE DE DATOS
-- ============================================================

CREATE DATABASE IF NOT EXISTS urban_style
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Seleccionar la base de datos
USE urban_style;


-- ============================================================
-- 2. TABLA: CATEGORIA
-- ============================================================

CREATE TABLE categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    estado BOOLEAN DEFAULT TRUE
);


-- ============================================================
-- 3. TABLA: TALLA
-- ============================================================

CREATE TABLE talla (
    id_talla INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(20) NOT NULL UNIQUE,
    estado BOOLEAN DEFAULT TRUE
);


-- ============================================================
-- 4. TABLA: COLOR
-- ============================================================

CREATE TABLE color (
    id_color INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    codigo_hex VARCHAR(7),
    estado BOOLEAN DEFAULT TRUE
);


-- ============================================================
-- 5. TABLA: ROL
-- ============================================================

CREATE TABLE rol (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);


-- ============================================================
-- 6. TABLA: PRODUCTO
-- ============================================================

CREATE TABLE producto (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    referencia VARCHAR(30) NOT NULL UNIQUE,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL,
    estado BOOLEAN DEFAULT TRUE,
    es_nuevo BOOLEAN DEFAULT FALSE,
    es_oferta BOOLEAN DEFAULT FALSE,
    es_destacado BOOLEAN DEFAULT FALSE,
    id_categoria INT NOT NULL,

    CONSTRAINT fk_producto_categoria
        FOREIGN KEY (id_categoria)
        REFERENCES categoria(id_categoria),

    CONSTRAINT chk_producto_precio
        CHECK (precio >= 0)
);


-- ============================================================
-- 7. TABLA: PRODUCTO_IMAGEN
-- ============================================================

CREATE TABLE producto_imagen (
    id_imagen INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    url_imagen VARCHAR(500) NOT NULL,
    texto_alternativo VARCHAR(255),
    orden INT DEFAULT 1,
    principal BOOLEAN DEFAULT FALSE,

    CONSTRAINT fk_imagen_producto
        FOREIGN KEY (id_producto)
        REFERENCES producto(id_producto)
        ON DELETE CASCADE
);


-- ============================================================
-- 8. TABLA: VARIANTE_PRODUCTO
-- ============================================================

CREATE TABLE variante_producto (
    id_variante INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    id_talla INT NOT NULL,
    id_color INT NOT NULL,
    sku VARCHAR(50) NOT NULL UNIQUE,
    estado BOOLEAN DEFAULT TRUE,

    CONSTRAINT fk_variante_producto
        FOREIGN KEY (id_producto)
        REFERENCES producto(id_producto),

    CONSTRAINT fk_variante_talla
        FOREIGN KEY (id_talla)
        REFERENCES talla(id_talla),

    CONSTRAINT fk_variante_color
        FOREIGN KEY (id_color)
        REFERENCES color(id_color),

    CONSTRAINT uq_producto_talla_color
        UNIQUE (id_producto, id_talla, id_color)
);


-- ============================================================
-- 9. TABLA: INVENTARIO
-- ============================================================

CREATE TABLE inventario (
    id_inventario INT AUTO_INCREMENT PRIMARY KEY,
    id_variante INT NOT NULL UNIQUE,
    stock_actual INT NOT NULL DEFAULT 0,
    stock_minimo INT NOT NULL DEFAULT 3,
    fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_inventario_variante
        FOREIGN KEY (id_variante)
        REFERENCES variante_producto(id_variante),

    CONSTRAINT chk_stock_actual
        CHECK (stock_actual >= 0),

    CONSTRAINT chk_stock_minimo
        CHECK (stock_minimo >= 0)
);


-- ============================================================
-- 10. TABLA: USUARIO
-- ============================================================

CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    estado BOOLEAN DEFAULT TRUE,
    id_rol INT NOT NULL,

    CONSTRAINT fk_usuario_rol
        FOREIGN KEY (id_rol)
        REFERENCES rol(id_rol)
);


-- ============================================================
-- 11. TABLA: CLIENTE
-- ============================================================

CREATE TABLE cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    correo VARCHAR(150),
    direccion VARCHAR(200) NOT NULL,
    barrio VARCHAR(100) NOT NULL,
    referencia_direccion VARCHAR(255)
);


-- ============================================================
-- 12. TABLA: PEDIDO
-- ============================================================

CREATE TABLE pedido (
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    id_usuario_vendedor INT NULL,
    fecha_pedido DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
    total DECIMAL(10,2) NOT NULL DEFAULT 0,
    id_usuario_confirmacion INT NULL,
    fecha_confirmacion DATETIME NULL,

CONSTRAINT fk_pedido_cliente
    FOREIGN KEY (id_cliente)
    REFERENCES cliente(id_cliente),
CONSTRAINT fk_pedido_usuario_vendedor
    FOREIGN KEY (id_usuario_vendedor)
    REFERENCES usuario(id_usuario),
CONSTRAINT fk_pedido_usuario_confirmacion
    FOREIGN KEY (id_usuario_confirmacion)
    REFERENCES usuario(id_usuario),

    CONSTRAINT chk_pedido_estado
        CHECK (
            estado IN (
                'PENDIENTE',
                'CONFIRMADO',
                'EN_PREPARACION',
                'ENVIADO',
                'ENTREGADO',
                'CANCELADO'
            )
        ),

    CONSTRAINT chk_pedido_total
        CHECK (total >= 0)
);


-- ============================================================
-- 13. TABLA: DETALLE_PEDIDO
-- ============================================================

CREATE TABLE detalle_pedido (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_variante INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,

    CONSTRAINT fk_detalle_pedido
        FOREIGN KEY (id_pedido)
        REFERENCES pedido(id_pedido)
        ON DELETE CASCADE,

    CONSTRAINT fk_detalle_variante
        FOREIGN KEY (id_variante)
        REFERENCES variante_producto(id_variante),

    CONSTRAINT chk_detalle_cantidad
        CHECK (cantidad > 0),

    CONSTRAINT chk_detalle_precio
        CHECK (precio_unitario >= 0),

    CONSTRAINT chk_detalle_subtotal
        CHECK (subtotal >= 0)
);


-- ============================================================
-- 14. TABLA: MOVIMIENTO_INVENTARIO
-- ============================================================

CREATE TABLE movimiento_inventario (
    id_movimiento INT AUTO_INCREMENT PRIMARY KEY,
    id_variante INT NOT NULL,
    id_usuario INT NOT NULL,
    tipo_movimiento VARCHAR(30) NOT NULL,
    cantidad INT NOT NULL,
    stock_anterior INT NOT NULL,
    stock_posterior INT NOT NULL,
    motivo VARCHAR(255),
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_movimiento_variante
        FOREIGN KEY (id_variante)
        REFERENCES variante_producto(id_variante),

    CONSTRAINT fk_movimiento_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario),

    CONSTRAINT chk_tipo_movimiento
        CHECK (
            tipo_movimiento IN (
                'ENTRADA',
                'SALIDA',
                'DEVOLUCION',
                'AJUSTE'
            )
        ),

    CONSTRAINT chk_movimiento_cantidad
        CHECK (cantidad > 0),

    CONSTRAINT chk_stock_anterior
        CHECK (stock_anterior >= 0),

    CONSTRAINT chk_stock_posterior
        CHECK (stock_posterior >= 0)
);


-- ============================================================
-- 15. TABLA: DEVOLUCION
-- ============================================================

CREATE TABLE devolucion (
    id_devolucion INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_detalle INT NOT NULL,
    id_usuario INT NOT NULL,
    cantidad INT NOT NULL,
    motivo VARCHAR(100) NOT NULL,
    estado_producto VARCHAR(50) NOT NULL,
    vuelve_inventario BOOLEAN DEFAULT FALSE,
    observaciones TEXT,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_devolucion_pedido
        FOREIGN KEY (id_pedido)
        REFERENCES pedido(id_pedido),

    CONSTRAINT fk_devolucion_detalle
        FOREIGN KEY (id_detalle)
        REFERENCES detalle_pedido(id_detalle),

    CONSTRAINT fk_devolucion_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario),

    CONSTRAINT chk_devolucion_cantidad
        CHECK (cantidad > 0),

    CONSTRAINT chk_estado_producto
        CHECK (
            estado_producto IN (
                'RESALABLE',
                'NO_RESALABLE',
                'DEFECTUOSO',
                'DANADO'
            )
        )
);


-- ============================================================
-- 16. TABLA: HISTORIAL_PRECIO
-- ============================================================

CREATE TABLE historial_precio (
    id_historial_precio INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    id_usuario INT NOT NULL,
    precio_anterior DECIMAL(10,2) NOT NULL,
    precio_nuevo DECIMAL(10,2) NOT NULL,
    motivo VARCHAR(255),
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_historial_precio_producto
        FOREIGN KEY (id_producto)
        REFERENCES producto(id_producto),

    CONSTRAINT fk_historial_precio_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario(id_usuario),

    CONSTRAINT chk_precio_anterior
        CHECK (precio_anterior >= 0),

    CONSTRAINT chk_precio_nuevo
        CHECK (precio_nuevo >= 0),

    CONSTRAINT chk_precio_diferente
        CHECK (precio_anterior <> precio_nuevo)
);


-- ============================================================
-- 17. DATOS INICIALES: CATEGORIAS
-- ============================================================

INSERT INTO categoria (nombre, descripcion)
VALUES
('Camisetas', 'Camisetas para hombre y mujer'),
('Jeans', 'Jeans y pantalones de mezclilla'),
('Vestidos', 'Vestidos para diferentes ocasiones'),
('Accesorios', 'Gorras, bolsos y otros accesorios');


-- ============================================================
-- 18. DATOS INICIALES: TALLAS
-- ============================================================

INSERT INTO talla (nombre)
VALUES
('S'),
('M'),
('L'),
('XL');


-- ============================================================
-- 19. DATOS INICIALES: COLORES
-- ============================================================

INSERT INTO color (nombre, codigo_hex)
VALUES
('Negro', '#000000'),
('Blanco', '#FFFFFF'),
('Rojo', '#FF0000'),
('Azul', '#0000FF');


-- ============================================================
-- 20. DATOS INICIALES: ROLES
-- ============================================================

INSERT INTO rol (nombre, descripcion)
VALUES
('Administrador', 'Gestiona completamente el sistema'),
('Vendedor', 'Gestiona y consulta pedidos y ventas'),
('Inventario', 'Gestiona existencias y movimientos de inventario');


-- ============================================================
-- 21. DATOS INICIALES: PRODUCTOS
-- ============================================================

INSERT INTO producto (
    referencia,
    nombre,
    descripcion,
    precio,
    estado,
    es_nuevo,
    es_oferta,
    es_destacado,
    id_categoria
)
VALUES
(
    'CAM-001',
    'Camiseta Oversize Urban',
    'Camiseta oversize de estilo urbano.',
    65000,
    TRUE,
    TRUE,
    FALSE,
    TRUE,
    1
),
(
    'CAM-002',
    'Camiseta Basic Street',
    'Camiseta básica de estilo casual.',
    55000,
    TRUE,
    FALSE,
    FALSE,
    TRUE,
    1
),
(
    'JEA-001',
    'Jean Classic Blue',
    'Jean clásico de corte moderno.',
    120000,
    TRUE,
    FALSE,
    FALSE,
    TRUE,
    2
),
(
    'VES-001',
    'Vestido Urban Night',
    'Vestido casual para diferentes ocasiones.',
    95000,
    TRUE,
    TRUE,
    FALSE,
    TRUE,
    3
),
(
    'GOR-001',
    'Gorra Urban Style',
    'Gorra urbana ajustable.',
    45000,
    TRUE,
    FALSE,
    TRUE,
    FALSE,
    4
);


-- ============================================================
-- 22. IMAGENES DE PRODUCTOS
-- ============================================================

INSERT INTO producto_imagen (
    id_producto,
    url_imagen,
    texto_alternativo,
    orden,
    principal
)
VALUES
(
    1,
    'https://placehold.co/600x600?text=CAM-001-Frontal',
    'Camiseta Oversize Urban vista frontal',
    1,
    TRUE
),
(
    1,
    'https://placehold.co/600x600?text=CAM-001-Posterior',
    'Camiseta Oversize Urban vista posterior',
    2,
    FALSE
),
(
    2,
    'https://placehold.co/600x600?text=CAM-002',
    'Camiseta Basic Street',
    1,
    TRUE
);


-- ============================================================
-- 23. VARIANTES DEL PRODUCTO CAM-001
-- ============================================================

INSERT INTO variante_producto (
    id_producto,
    id_talla,
    id_color,
    sku
)
VALUES
(1, 1, 1, 'CAM-001-S-NEG'),
(1, 2, 1, 'CAM-001-M-NEG'),
(1, 3, 1, 'CAM-001-L-NEG'),
(1, 4, 1, 'CAM-001-XL-NEG'),
(1, 1, 2, 'CAM-001-S-BLA'),
(1, 2, 2, 'CAM-001-M-BLA'),
(1, 3, 2, 'CAM-001-L-BLA');


-- ============================================================
-- 24. VARIANTES DEL PRODUCTO CAM-002
-- ============================================================

INSERT INTO variante_producto (
    id_producto,
    id_talla,
    id_color,
    sku
)
VALUES
(2, 1, 1, 'CAM-002-S-NEG'),
(2, 2, 1, 'CAM-002-M-NEG'),
(2, 3, 1, 'CAM-002-L-NEG'),
(2, 4, 1, 'CAM-002-XL-NEG'),
(2, 1, 2, 'CAM-002-S-BLA'),
(2, 2, 2, 'CAM-002-M-BLA'),
(2, 3, 2, 'CAM-002-L-BLA');


-- ============================================================
-- 25. INVENTARIO INICIAL
-- ============================================================

INSERT INTO inventario (
    id_variante,
    stock_actual,
    stock_minimo
)
VALUES
(1, 10, 3),
(2, 15, 3),
(3, 8, 3),
(4, 3, 3),
(5, 12, 3),
(6, 18, 3),
(7, 7, 3),
(8, 10, 3),
(9, 14, 3),
(10, 9, 3),
(11, 4, 3),
(12, 8, 3),
(13, 11, 3),
(14, 6, 3);


-- ============================================================
-- 26. USUARIOS INICIALES
-- ============================================================
-- Las contraseñas son valores de prueba.
-- Posteriormente Spring Security utilizará BCrypt.

INSERT INTO usuario (
    nombre,
    apellido,
    correo,
    password_hash,
    estado,
    id_rol
)
VALUES
(
    'Ana',
    'Martínez',
    'ana@urbanstyle.com',
    'HASH_DE_PRUEBA_ADMIN',
    TRUE,
    1
),
(
    'Carlos',
    'Pérez',
    'carlos@urbanstyle.com',
    'HASH_DE_PRUEBA_VENDEDOR',
    TRUE,
    2
),
(
    'Laura',
    'Gómez',
    'laura@urbanstyle.com',
    'HASH_DE_PRUEBA_VENDEDOR',
    TRUE,
    2
),
(
    'Andrés',
    'Ruiz',
    'andres@urbanstyle.com',
    'HASH_DE_PRUEBA_VENDEDOR',
    TRUE,
    2
),
(
    'Pedro',
    'Martínez',
    'pedro@urbanstyle.com',
    'HASH_DE_PRUEBA_INVENTARIO',
    TRUE,
    3
);


-- ============================================================
-- 27. CLIENTE DE PRUEBA
-- ============================================================

INSERT INTO cliente (
    nombre,
    telefono,
    correo,
    direccion,
    barrio,
    referencia_direccion
)
VALUES
(
    'Santiago Pérez',
    '3001234567',
    'santiago@email.com',
    'Carrera 20 # 15-30',
    'Centro',
    'Casa blanca frente al parque'
);


-- ============================================================
-- FIN DEL SCRIPT
-- ============================================================