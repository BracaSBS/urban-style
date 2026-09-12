# Pruebas de Base de Datos — Urban Style

## 1. Información general

**Proyecto:** Urban Style  
**Sistema:** Plataforma web para gestión de catálogo, pedidos e inventario  
**Base de datos utilizada para pruebas:** urban_style_prueba2  
**Motor:** MySQL  
**Versión del modelo:** 2.0  

---

# 2. Objetivo de las pruebas

Las pruebas de base de datos tienen como objetivo verificar que la estructura,
restricciones, relaciones y reglas de negocio definidas para Urban Style
funcionen correctamente.

Se realizaron pruebas sobre:

- Integridad de datos.
- Integridad referencial.
- Restricciones de valores.
- Variantes de productos.
- Control de inventario.
- Creación de pedidos.
- Confirmación de pedidos.
- Cancelación de pedidos.
- Devoluciones.
- Control de transacciones.

---

# 3. Resultados de las pruebas

## PRUEBA BD-01 — Evitar variantes duplicadas

**Objetivo:**  
Verificar que un producto no pueda tener dos variantes con la misma talla y
color.

**Regla validada:**  
Un producto no puede repetir la combinación:

Producto + Talla + Color.

**Resultado esperado:**  
La base de datos debe rechazar la inserción de una variante duplicada.

**Resultado obtenido:**  
La inserción fue rechazada correctamente.

**Estado:** APROBADA

---

## PRUEBA BD-02 — Evitar inventario negativo

**Objetivo:**  
Verificar que el stock de una variante nunca pueda ser menor que cero.

**Regla validada:**  
El stock actual debe ser igual o mayor que 0.

**Resultado esperado:**  
Una actualización que produzca stock negativo debe ser rechazada.

**Resultado obtenido:**  
La operación fue rechazada correctamente.

**Estado:** APROBADA

---

## PRUEBA BD-03 — Integridad referencial de productos

**Objetivo:**  
Verificar que una variante no pueda asociarse a un producto inexistente.

**Regla validada:**  
Toda variante debe pertenecer a un producto existente.

**Resultado esperado:**  
La inserción debe ser rechazada cuando el producto relacionado no existe.

**Resultado obtenido:**  
La operación fue rechazada correctamente por la restricción de clave foránea.

**Estado:** APROBADA

---

## PRUEBA BD-04 — Protección de productos con variantes

**Objetivo:**  
Verificar que no se pueda eliminar físicamente un producto que tenga
variantes asociadas.

**Regla validada:**  
Los productos relacionados con información histórica deben conservarse.

**Resultado esperado:**  
La eliminación física debe ser rechazada.

**Resultado obtenido:**  
La eliminación fue rechazada correctamente.

**Estado:** APROBADA

---

## PRUEBA BD-05 — Desactivación lógica de productos

**Objetivo:**  
Verificar que un producto pueda desactivarse sin eliminarlo de la base de
datos.

**Regla validada:**  
Se debe preferir la desactivación lógica cuando el producto tiene
información histórica.

**Resultado esperado:**  
El campo `estado` debe cambiar a FALSE sin eliminar el registro.

**Resultado obtenido:**  
El producto fue desactivado correctamente.

Posteriormente se verificó que podía volver a activarse.

**Estado:** APROBADA

---

## PRUEBA BD-06 — Creación de pedido pendiente

**Objetivo:**  
Verificar que un cliente pueda generar un pedido y que este quede inicialmente
en estado PENDIENTE.

**Datos utilizados:**

- Pedido: #1
- Variante: CAM-001-M-NEG
- Cantidad: 2 unidades
- Precio unitario: $65.000
- Total: $130.000
- Stock disponible inicialmente: 15 unidades

**Resultado esperado:**

El pedido debe quedar en estado:

PENDIENTE

El inventario no debe modificarse mientras el pedido permanezca pendiente.

**Resultado obtenido:**

El pedido fue creado correctamente.

Estado:

PENDIENTE

Stock antes:

15 unidades

Stock después:

15 unidades

**Estado:** APROBADA

---

## PRUEBA BD-07 — Confirmación de pedido y descuento de inventario

**Objetivo:**  
Verificar que al confirmar un pedido se descuente correctamente el inventario
y se registre el movimiento correspondiente.

**Datos utilizados:**

- Pedido: #1
- Variante: CAM-001-M-NEG
- Cantidad: 2 unidades
- Stock antes: 15
- Stock después: 13
- Tipo de movimiento: SALIDA

**Resultado esperado:**

1. Verificar disponibilidad.
2. Descontar las unidades.
3. Registrar el movimiento de inventario.
4. Cambiar el pedido a CONFIRMADO.
5. Registrar usuario y fecha de confirmación.
6. Ejecutar las operaciones dentro de una transacción.

**Resultado obtenido:**

Stock:

15 → 13

Movimiento de inventario registrado correctamente como:

SALIDA

Pedido actualizado a:

CONFIRMADO

**Estado:** APROBADA

---

## PRUEBA BD-08 — Evitar confirmación sin stock suficiente

**Objetivo:**  
Verificar que un pedido no pueda confirmarse cuando la cantidad solicitada
supera el inventario disponible.

**Datos utilizados:**

- Pedido: #2
- Cantidad solicitada: 20 unidades
- Stock disponible: 13 unidades

**Resultado esperado:**  

La operación de descuento debe afectar 0 registros y no debe permitir que el
inventario quede negativo.

La transacción debe revertirse.

**Resultado obtenido:**

La actualización no permitió descontar las 20 unidades.

Stock:

13 → 13

La transacción fue revertida mediante ROLLBACK.

**Estado:** APROBADA

---

## PRUEBA BD-09 — Cancelación de pedido confirmado

**Objetivo:**  
Verificar que la cancelación de un pedido confirmado pueda restaurar el
inventario.

**Datos utilizados:**

- Pedido: #3
- Cantidad: 2 unidades
- Stock antes de confirmación: 13
- Stock después de confirmación: 11
- Stock después de cancelación: 13

**Resultado esperado:**

Al cancelar el pedido se deben devolver las unidades al inventario y registrar
un movimiento de tipo DEVOLUCION.

**Resultado obtenido:**

Stock:

13 → 11 → 13

Se registró correctamente el movimiento:

DEVOLUCION

**Estado:** APROBADA

---

## PRUEBA BD-10 — Devolución de producto resalable

**Objetivo:**  
Verificar que una devolución cuyo producto se encuentra en condiciones de ser
vendido nuevamente pueda reincorporarse al inventario.

**Datos utilizados:**

- Pedido: #1
- Detalle: #1
- Cantidad devuelta: 1 unidad
- Estado del producto: RESALABLE
- Vuelve al inventario: TRUE

**Resultado esperado:**

El producto debe regresar al inventario y debe registrarse un movimiento de
tipo DEVOLUCION.

**Resultado obtenido:**

Stock:

13 → 14

Movimiento:

DEVOLUCION

La devolución fue registrada correctamente.

**Estado:** APROBADA

---

# 4. Resumen de resultados

| ID | Prueba | Resultado |
|---|---|---|
| BD-01 | Variantes duplicadas | APROBADA |
| BD-02 | Inventario negativo | APROBADA |
| BD-03 | Integridad referencial | APROBADA |
| BD-04 | Protección de productos | APROBADA |
| BD-05 | Desactivación lógica | APROBADA |
| BD-06 | Pedido pendiente | APROBADA |
| BD-07 | Confirmación y descuento | APROBADA |
| BD-08 | Falta de stock | APROBADA |
| BD-09 | Cancelación y devolución | APROBADA |
| BD-10 | Devolución resalable | APROBADA |

**Total de pruebas:** 10  
**Pruebas aprobadas:** 10  
**Pruebas rechazadas:** 0  

---

# 5. Conclusión

Las pruebas realizadas sobre la base de datos de Urban Style permitieron
verificar el correcto funcionamiento de las principales restricciones de
integridad y reglas de negocio relacionadas con productos, variantes,
inventario, pedidos y devoluciones.

Los resultados obtenidos muestran que la base de datos responde
correctamente ante operaciones válidas y rechaza operaciones que podrían
generar inconsistencias, como inventario negativo, variantes duplicadas,
referencias inexistentes o confirmaciones sin stock suficiente.

Las operaciones críticas de confirmación, cancelación y devolución fueron
probadas mediante transacciones y movimientos de inventario, verificando que
los cambios de stock sean coherentes con el estado de los pedidos.

Por lo anterior, la versión actual de la estructura de base de datos se
considera funcionalmente validada para continuar con las siguientes etapas
del desarrollo del proyecto.

