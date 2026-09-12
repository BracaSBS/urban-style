# Modelo Relacional — Urban Style

## 1. Información general

**Proyecto:** Urban Style  
**Sistema:** Plataforma web para gestión de catálogo, pedidos e inventario  
**Motor de base de datos:** MySQL  
**Versión del modelo:** 2.0  

---

# 2. Modelo relacional

El modelo relacional representa la estructura lógica de la base de datos
Urban Style mediante tablas, claves primarias y claves foráneas.

## 2.1 CATEGORIA

```text
CATEGORIA
--------------------------------
PK id_categoria
nombre
descripcion
estado
Relación:

CATEGORIA 1 ───────── N PRODUCTO

2.2 PRODUCTO
PRODUCTO
--------------------------------
PK id_producto
referencia
nombre
descripcion
precio
estado
es_nuevo
es_oferta
es_destacado
FK id_categoria

Relaciones:

CATEGORIA 1 ───────── N PRODUCTO

PRODUCTO 1 ───────── N PRODUCTO_IMAGEN

PRODUCTO 1 ───────── N VARIANTE_PRODUCTO

PRODUCTO 1 ───────── N HISTORIAL_PRECIO
2.3 PRODUCTO_IMAGEN
PRODUCTO_IMAGEN
--------------------------------
PK id_imagen
FK id_producto
url_imagen
texto_alternativo
orden
principal

Relación:

PRODUCTO 1 ───────── N PRODUCTO_IMAGEN
2.4 TALLA
TALLA
--------------------------------
PK id_talla
nombre
estado

Relación:

TALLA 1 ───────── N VARIANTE_PRODUCTO
2.5 COLOR
COLOR
--------------------------------
PK id_color
nombre
codigo_hex
estado

Relación:

COLOR 1 ───────── N VARIANTE_PRODUCTO
2.6 VARIANTE_PRODUCTO
VARIANTE_PRODUCTO
--------------------------------
PK id_variante
FK id_producto
FK id_talla
FK id_color
sku
estado

Relaciones:

PRODUCTO 1 ───────── N VARIANTE_PRODUCTO

TALLA 1 ───────── N VARIANTE_PRODUCTO

COLOR 1 ───────── N VARIANTE_PRODUCTO

VARIANTE_PRODUCTO 1 ───────── 1 INVENTARIO

VARIANTE_PRODUCTO 1 ───────── N DETALLE_PEDIDO

VARIANTE_PRODUCTO 1 ───────── N MOVIMIENTO_INVENTARIO

Regla importante:

PRODUCTO + TALLA + COLOR = combinación única
2.7 INVENTARIO
INVENTARIO
--------------------------------
PK id_inventario
FK id_variante
stock_actual
stock_minimo
fecha_actualizacion

Relación:

VARIANTE_PRODUCTO 1 ───────── 1 INVENTARIO

Cada variante posee un registro de inventario independiente.

2.8 ROL
ROL
--------------------------------
PK id_rol
nombre
descripcion

Relación:

ROL 1 ───────── N USUARIO
2.9 USUARIO
USUARIO
--------------------------------
PK id_usuario
nombre
apellido
correo
password_hash
estado
FK id_rol

Relaciones:

ROL 1 ───────── N USUARIO

USUARIO 1 ───────── N PEDIDO
        vendedor

USUARIO 1 ───────── N PEDIDO
        confirmador

USUARIO 1 ───────── N MOVIMIENTO_INVENTARIO

USUARIO 1 ───────── N DEVOLUCION

USUARIO 1 ───────── N HISTORIAL_PRECIO
2.10 CLIENTE
CLIENTE
--------------------------------
PK id_cliente
nombre
telefono
correo
direccion
barrio
referencia_direccion

Relación:

CLIENTE 1 ───────── N PEDIDO

El cliente no requiere una cuenta de usuario para realizar una compra en
el MVP.

2.11 PEDIDO
PEDIDO
--------------------------------
PK id_pedido
FK id_cliente
FK id_usuario_vendedor
fecha_pedido
estado
total
FK id_usuario_confirmacion
fecha_confirmacion

Relaciones:

CLIENTE 1 ───────── N PEDIDO

USUARIO 1 ───────── N PEDIDO
        vendedor

USUARIO 1 ───────── N PEDIDO
        confirmador

PEDIDO 1 ───────── N DETALLE_PEDIDO

PEDIDO 1 ───────── N DEVOLUCION

Estados permitidos:

PENDIENTE
CONFIRMADO
EN_PREPARACION
ENVIADO
ENTREGADO
CANCELADO
2.12 DETALLE_PEDIDO
DETALLE_PEDIDO
--------------------------------
PK id_detalle
FK id_pedido
FK id_variante
cantidad
precio_unitario
subtotal

Relaciones:

PEDIDO 1 ───────── N DETALLE_PEDIDO

VARIANTE_PRODUCTO 1 ───────── N DETALLE_PEDIDO

El campo precio_unitario conserva el precio aplicado en el momento de
la venta.

2.13 MOVIMIENTO_INVENTARIO
MOVIMIENTO_INVENTARIO
--------------------------------
PK id_movimiento
FK id_variante
FK id_usuario
tipo_movimiento
cantidad
stock_anterior
stock_posterior
motivo
fecha

Relaciones:

VARIANTE_PRODUCTO 1 ───────── N MOVIMIENTO_INVENTARIO

USUARIO 1 ───────── N MOVIMIENTO_INVENTARIO

Tipos de movimiento:

ENTRADA
SALIDA
DEVOLUCION
AJUSTE
2.14 DEVOLUCION
DEVOLUCION
--------------------------------
PK id_devolucion
FK id_pedido
FK id_detalle
FK id_usuario
cantidad
motivo
estado_producto
vuelve_inventario
observaciones
fecha

Relaciones:

PEDIDO 1 ───────── N DEVOLUCION

DETALLE_PEDIDO 1 ───────── N DEVOLUCION

USUARIO 1 ───────── N DEVOLUCION
2.15 HISTORIAL_PRECIO
HISTORIAL_PRECIO
--------------------------------
PK id_historial_precio
FK id_producto
FK id_usuario
precio_anterior
precio_nuevo
motivo
fecha

Relaciones:

PRODUCTO 1 ───────── N HISTORIAL_PRECIO

USUARIO 1 ───────── N HISTORIAL_PRECIO

Permite conocer quién modificó el precio, cuándo lo modificó y cuál era el
precio anterior y el nuevo precio.

3. Resumen general de relaciones
CATEGORIA
    │
    └──< PRODUCTO
             │
             ├──< PRODUCTO_IMAGEN
             │
             ├──< VARIANTE_PRODUCTO
             │       │
             │       └── INVENTARIO
             │
             └──< HISTORIAL_PRECIO >── USUARIO
             
TALLA
    │
    └──< VARIANTE_PRODUCTO

COLOR
    │
    └──< VARIANTE_PRODUCTO

ROL
    │
    └──< USUARIO
             │
             ├──< MOVIMIENTO_INVENTARIO
             ├──< DEVOLUCION
             ├──< HISTORIAL_PRECIO
             │
             └──< PEDIDO
                  │
                  ├── vendedor
                  ├── confirmador
                  │
                  ├──< DETALLE_PEDIDO
                  │       │
                  │       └── VARIANTE_PRODUCTO
                  │
                  └──< DEVOLUCION

CLIENTE
    │
    └──< PEDIDO
4. Reglas principales representadas
Cada producto pertenece a una categoría.
Un producto puede tener múltiples imágenes.
Un producto puede tener múltiples variantes.
Una variante está determinada por producto, talla y color.
Cada variante posee inventario independiente.
Un cliente puede realizar múltiples pedidos.
Un pedido puede tener múltiples detalles.
Cada detalle corresponde a una variante específica.
Los movimientos de inventario quedan asociados a una variante y a un usuario.
Las devoluciones quedan asociadas al pedido y a su detalle.
Los cambios de precio quedan registrados en un historial.
El vendedor responsable y el usuario que confirma un pedido pueden ser diferentes.
El cliente puede realizar pedidos sin tener una cuenta de usuario.
