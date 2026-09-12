# DICCIONARIO DE DATOS — URBAN STYLE

## 1. Información general

**Proyecto:** Urban Style  
**Sistema:** Sistema de gestión de catálogo, pedidos e inventario  
**Base de datos:** urban_style  
**Motor:** MySQL  
**Versión:** 1.0  

---

## 2. Objetivo

El presente diccionario de datos describe la estructura de la base de datos
del sistema Urban Style, incluyendo las tablas, campos, tipos de datos,
claves primarias, claves foráneas, obligatoriedad y reglas de integridad.

Este documento sirve como referencia técnica para el desarrollo,
mantenimiento y evolución del sistema.

---

# 3. Estructura de la base de datos

La base de datos Urban Style está compuesta por 15 tablas:

1. categoria
2. producto
3. producto_imagen
4. talla
5. color
6. variante_producto
7. inventario
8. rol
9. usuario
10. cliente
11. pedido
12. detalle_pedido
13. movimiento_inventario
14. devolucion
15. historial_precio

---

# 4. Diccionario de datos

## 4.1 Tabla: categoria

### Descripción

Almacena las categorías utilizadas para clasificar los productos
disponibles en Urban Style.

| Campo | Tipo de dato | Clave | Obligatorio | Descripción |
|---|---|---|---|---|
| id_categoria | INT | PK | Sí | Identificador único de la categoría. |
| nombre | VARCHAR(100) | UNIQUE | Sí | Nombre de la categoría. |
| descripcion | VARCHAR(255) | — | No | Descripción de la categoría. |
| estado | BOOLEAN | — | Sí | Indica si la categoría está activa. |

### Restricciones

- `id_categoria` es la clave primaria.
- `nombre` debe ser único.
- `estado` permite activar o desactivar una categoría.
- Una categoría puede estar asociada a múltiples productos.

### Relación

```text
CATEGORIA 1 ─────── N PRODUCTO


Una categoría puede tener muchos productos, mientras que cada producto
pertenece a una categoría.


---

## PASO 3 — Guardar

Guarda el archivo con:

**Ctrl + S**

Y por ahora **no escribas las demás tablas**.

Quiero que primero tengamos perfectamente definido el formato que utilizaremos para todo el diccionario.

### Lo que acabamos de hacer

La primera entrada documenta:

**`categoria`**

y deja claro:

- qué almacena;
- qué campos tiene;
- cuáles son obligatorios;
- cuál es su clave primaria;
- qué restricciones posee;
- y cómo se relaciona con `producto`.

Después continuaremos con:

**4.2 `producto`**

y ahí empezaremos a documentar uno de los elementos centrales del sistema.

## 4.2 Tabla: producto

### Descripción

Almacena la información principal de los productos comercializados por
Urban Style. Cada producto pertenece a una categoría y puede tener
múltiples imágenes y variantes de talla y color.

| Campo | Tipo de dato | Clave | Obligatorio | Descripción |
|---|---|---|---|---|
| id_producto | INT | PK | Sí | Identificador único del producto. |
| referencia | VARCHAR(30) | UNIQUE | Sí | Código interno que identifica el producto. |
| nombre | VARCHAR(150) | — | Sí | Nombre comercial del producto. |
| descripcion | TEXT | — | No | Descripción detallada del producto. |
| precio | DECIMAL(10,2) | — | Sí | Precio actual de venta del producto. |
| estado | BOOLEAN | — | Sí | Indica si el producto está activo o inactivo. |
| es_nuevo | BOOLEAN | — | Sí | Indica si el producto se marca como nuevo. |
| es_oferta | BOOLEAN | — | Sí | Indica si el producto está marcado como oferta. |
| es_destacado | BOOLEAN | — | Sí | Indica si el producto aparece como destacado. |
| id_categoria | INT | FK | Sí | Identificador de la categoría a la que pertenece el producto. |

### Restricciones

- `id_producto` es la clave primaria.
- `referencia` debe ser única.
- `precio` no puede ser negativo.
- `id_categoria` debe corresponder a una categoría existente.
- `estado` permite realizar una desactivación lógica del producto.
- Un producto puede tener múltiples imágenes.
- Un producto puede tener múltiples variantes.
- Los cambios de precio deben registrarse en `historial_precio`.
- Se recomienda desactivar un producto en lugar de eliminarlo físicamente
  cuando existan registros relacionados o historial comercial.

### Relaciones

```text
CATEGORIA 1 ─────── N PRODUCTO

PRODUCTO 1 ─────── N PRODUCTO_IMAGEN

PRODUCTO 1 ─────── N VARIANTE_PRODUCTO

PRODUCTO 1 ─────── N HISTORIAL_PRECIO

Regla de negocio

El precio almacenado en producto representa el precio actual de venta.
Cuando este precio sea modificado por un usuario autorizado, debe registrarse
el precio anterior, el nuevo precio, el usuario responsable, la fecha y el
motivo del cambio en la tabla historial_precio.


### ¿Qué estamos documentando aquí?

Hay una decisión importante que quiero que quede clara en nuestro proyecto:

**`producto` no representa una camiseta específica talla M y color negro.**

Representa el producto general:

> **CAM-001 — Camiseta Oversize Urban — $65.000**

Las combinaciones:

> M + Negro  
> L + Negro  
> M + Blanco

son **variantes**, y las manejaremos en `variante_producto`.

Esto es precisamente lo que permitirá que Urban Style controle el inventario **por talla y color**.

---

### Después de pegarlo

Guarda con **Ctrl + S**.

No avances todavía a la siguiente tabla. Cuando me digas **"listo"**, hacemos:

**4.3 `producto_imagen`**.

## 4.3 Tabla: producto_imagen

### Descripción

Almacena las imágenes asociadas a cada producto de Urban Style. Se utiliza
una tabla independiente para permitir que un mismo producto tenga múltiples
imágenes y para identificar cuál de ellas corresponde a la imagen principal
del producto.

| Campo | Tipo de dato | Clave | Obligatorio | Descripción |
|---|---|---|---|---|
| id_imagen | INT | PK | Sí | Identificador único de la imagen. |
| id_producto | INT | FK | Sí | Identificador del producto al que pertenece la imagen. |
| url_imagen | VARCHAR(500) | — | Sí | Dirección donde se encuentra almacenada la imagen. |
| texto_alternativo | VARCHAR(255) | — | No | Texto descriptivo utilizado como alternativa para la imagen. |
| orden | INT | — | Sí | Posición en la que se mostrará la imagen. |
| principal | BOOLEAN | — | Sí | Indica si la imagen corresponde a la imagen principal del producto. |

### Restricciones

- `id_imagen` es la clave primaria.
- `id_producto` es una clave foránea que referencia a `producto`.
- Una imagen debe estar asociada a un producto existente.
- Un producto puede tener múltiples imágenes.
- `orden` permite controlar el orden de presentación de las imágenes.
- `principal` permite identificar la imagen principal del producto.
- Si un producto es eliminado físicamente, sus imágenes asociadas pueden
  eliminarse mediante la eliminación en cascada definida en la relación.

### Relación

```text
PRODUCTO 1 ─────── N PRODUCTO_IMAGEN

Un producto puede tener una o varias imágenes, mientras que cada imagen
pertenece a un único producto.

Regla de negocio

Las imágenes deben estar asociadas al producto correspondiente. La aplicación
debe procurar que cada producto tenga una imagen principal para garantizar una
correcta presentación en el catálogo.


### ¿Por qué tenemos esta tabla?

Podríamos haber puesto algo como:

```text
producto
 └── imagen1

directamente dentro de producto, pero eso nos limitaría a una sola imagen.

Con nuestro diseño podemos tener:

CAM-001
   ├── Imagen frontal
   ├── Imagen posterior
   ├── Imagen lateral
   └── Imagen detalle

## 4.4 Tabla: talla

### Descripción

Almacena las tallas disponibles para los productos de Urban Style. Esta
información se administra de manera independiente para poder reutilizar las
mismas tallas en diferentes productos y variantes.

| Campo | Tipo de dato | Clave | Obligatorio | Descripción |
|---|---|---|---|---|
| id_talla | INT | PK | Sí | Identificador único de la talla. |
| nombre | VARCHAR(20) | UNIQUE | Sí | Nombre o código de la talla. |
| estado | BOOLEAN | — | Sí | Indica si la talla está disponible para su utilización. |

### Restricciones

- `id_talla` es la clave primaria.
- `nombre` debe ser único.
- Una talla puede estar asociada a múltiples variantes de productos.
- `estado` permite desactivar una talla sin eliminarla físicamente.
- Una talla utilizada por variantes existentes no debería eliminarse
  físicamente.

### Relación

```text
TALLA 1 ─────── N VARIANTE_PRODUCTO

Una talla puede utilizarse en múltiples variantes, mientras que cada variante
utiliza una única talla.

Datos iniciales

El sistema contempla inicialmente las siguientes tallas:

S
M
L
XL
Regla de negocio

Las tallas se administran como datos independientes para evitar duplicidad y
permitir que diferentes productos utilicen las mismas opciones de talla.


### ¿Qué estamos consiguiendo?

En lugar de escribir directamente `"M"` dentro de cada producto, tenemos:

```text
talla
│
├── S
├── M
├── L
└── XL

## 4.5 Tabla: color

### Descripción

Almacena los colores disponibles para las variantes de los productos de
Urban Style. La información se mantiene en una tabla independiente para
evitar duplicidad y permitir que diferentes productos utilicen los mismos
colores.

| Campo | Tipo de dato | Clave | Obligatorio | Descripción |
|---|---|---|---|---|
| id_color | INT | PK | Sí | Identificador único del color. |
| nombre | VARCHAR(50) | UNIQUE | Sí | Nombre del color. |
| codigo_hex | VARCHAR(7) | — | No | Código hexadecimal utilizado para representar visualmente el color. |
| estado | BOOLEAN | — | Sí | Indica si el color está disponible para su utilización. |

### Restricciones

- `id_color` es la clave primaria.
- `nombre` debe ser único.
- Un color puede estar asociado a múltiples variantes de productos.
- `codigo_hex` permite representar visualmente el color en la interfaz.
- `estado` permite desactivar un color sin eliminarlo físicamente.
- Un color utilizado por variantes existentes no debería eliminarse
  físicamente.

### Relación

```text
COLOR 1 ─────── N VARIANTE_PRODUCTO
Un color puede utilizarse en múltiples variantes, mientras que cada variante
utiliza un único color.

Datos iniciales

El sistema contempla inicialmente los siguientes colores:

Negro
Blanco
Rojo
Azul
Regla de negocio

Los colores se administran como datos independientes para evitar duplicidad
y permitir que diferentes productos compartan las mismas opciones de color.

El código hexadecimal es opcional y puede utilizarse posteriormente para
mostrar visualmente cada color en el catálogo web.


### Una idea importante del modelo

Ahora tenemos:

```text
             VARIANTE_PRODUCTO
              /             \
             /               \
        PRODUCTO            TALLA
             \               
              \              
              COLOR


## 4.6 Tabla: variante_producto

### Descripción

Almacena las diferentes combinaciones de producto, talla y color disponibles
en Urban Style. Cada variante representa una presentación específica de un
producto y posee un código SKU único para facilitar su identificación y
gestión de inventario.

Por ejemplo, un mismo producto puede tener las siguientes variantes:

- CAM-001 + S + Negro
- CAM-001 + M + Negro
- CAM-001 + L + Negro
- CAM-001 + M + Blanco

Cada una de estas combinaciones puede tener una cantidad de inventario
diferente.

| Campo | Tipo de dato | Clave | Obligatorio | Descripción |
|---|---|---|---|---|
| id_variante | INT | PK | Sí | Identificador único de la variante. |
| id_producto | INT | FK | Sí | Identificador del producto al que pertenece la variante. |
| id_talla | INT | FK | Sí | Identificador de la talla correspondiente. |
| id_color | INT | FK | Sí | Identificador del color correspondiente. |
| sku | VARCHAR(50) | UNIQUE | Sí | Código único de identificación de la variante. |
| estado | BOOLEAN | — | Sí | Indica si la variante está activa o disponible. |

### Restricciones

- `id_variante` es la clave primaria.
- `id_producto` debe corresponder a un producto existente.
- `id_talla` debe corresponder a una talla existente.
- `id_color` debe corresponder a un color existente.
- `sku` debe ser único.
- No puede existir más de una variante con la misma combinación de
  producto, talla y color.
- La combinación `(id_producto, id_talla, id_color)` posee una restricción
  de unicidad.
- Una variante puede tener un único registro de inventario.
- Una variante puede aparecer en múltiples detalles de pedido.
- Una variante puede tener múltiples movimientos de inventario.

### Relaciones

```text
PRODUCTO 1 ─────── N VARIANTE_PRODUCTO

TALLA 1 ────────── N VARIANTE_PRODUCTO

COLOR 1 ────────── N VARIANTE_PRODUCTO

VARIANTE_PRODUCTO 1 ─────── 1 INVENTARIO

VARIANTE_PRODUCTO 1 ─────── N DETALLE_PEDIDO

VARIANTE_PRODUCTO 1 ─────── N MOVIMIENTO_INVENTARIO

Ejemplo

Para el producto:

Referencia: CAM-001
Nombre: Camiseta Oversize Urban

pueden existir las siguientes variantes:

| SKU            | Talla | Color  |
| -------------- | ----- | ------ |
| CAM-001-S-NEG  | S     | Negro  |
| CAM-001-M-NEG  | M     | Negro  |
| CAM-001-L-NEG  | L     | Negro  |
| CAM-001-XL-NEG | XL    | Negro  |
| CAM-001-S-BLA  | S     | Blanco |
| CAM-001-M-BLA  | M     | Blanco |
| CAM-001-L-BLA  | L     | Blanco |

Regla de negocio

El inventario se controla por variante y no directamente por producto.

Por lo tanto, el stock de:

CAM-001 + M + Negro

es independiente del stock de:

CAM-001 + L + Negro

Si una variante tiene stock igual a cero, dicha combinación debe mostrarse
como no disponible, aunque el producto general continúe disponible mediante
otras variantes.

La aplicación debe permitir seleccionar talla y color antes de agregar un
producto al carrito cuando existan variantes disponibles.


### Esta tabla es fundamental

Mira la diferencia:

```text
PRODUCTO
CAM-001
Camiseta Oversize Urban
$65.000

Eso no nos dice cuánto tenemos de cada combinación.

En cambio:

VARIANTE_PRODUCTO

M ─ Negro ──→ 15 unidades
L ─ Negro ──→ 8 unidades
XL ─ Negro ─→ 3 unidades
S ─ Blanco ─→ 12 unidades
M ─ Blanco ─→ 18 unidades

Ahí sí podemos controlar el inventario correctamente.

Y justamente por eso la prueba que hicimos anteriormente:

intentar crear dos veces CAM-001 + S + Negro

dio error. La base de datos está protegiendo esta regla mediante:

## 4.7 Tabla: inventario

### Descripción

Almacena el estado actual del inventario de cada variante de producto.
Permite conocer cuántas unidades están disponibles y cuál es el nivel mínimo
establecido para generar alertas de bajo stock.

El inventario se administra por variante, por lo que una combinación
específica de producto, talla y color posee su propio registro de existencias.

| Campo | Tipo de dato | Clave | Obligatorio | Descripción |
|---|---|---|---|---|
| id_inventario | INT | PK | Sí | Identificador único del registro de inventario. |
| id_variante | INT | FK / UNIQUE | Sí | Identificador de la variante a la que pertenece el inventario. |
| stock_actual | INT | — | Sí | Cantidad actual de unidades disponibles. |
| stock_minimo | INT | — | Sí | Cantidad mínima establecida para generar una alerta de bajo stock. |
| fecha_actualizacion | DATETIME | — | Sí | Fecha y hora de la última actualización del registro. |

### Restricciones

- `id_inventario` es la clave primaria.
- `id_variante` debe corresponder a una variante existente.
- Una variante solo puede tener un registro de inventario.
- `stock_actual` no puede ser negativo.
- `stock_minimo` no puede ser negativo.
- La fecha de actualización se actualiza automáticamente cuando cambia el
  registro de inventario.
- El inventario actual representa el estado disponible en un momento
  determinado.
- Los cambios realizados sobre el stock deben quedar registrados en
  `movimiento_inventario`.

### Relación

```text
VARIANTE_PRODUCTO 1 ─────── 1 INVENTARIO

Cada variante posee un único registro de inventario y cada registro de
inventario corresponde a una única variante.
Ejemplo

Para la variante:

CAM-001-M-NEG

podemos tener:

stock_actual = 15
stock_minimo = 3

Esto significa que existen actualmente 15 unidades disponibles y que el
sistema debe considerar la variante en nivel de alerta cuando el stock sea
igual o inferior a 3 unidades.

Reglas de negocio
El stock se controla individualmente para cada variante.
Cuando stock_actual = 0, la variante debe considerarse agotada.
Cuando stock_actual <= stock_minimo, debe generarse una alerta de bajo
stock.
El stock no se descuenta cuando un producto solamente se agrega al carrito.
El stock se descuenta cuando un pedido pasa de PENDIENTE a CONFIRMADO.
Una devolución de un producto en estado RESALABLE puede incrementar el
stock disponible.
Las operaciones que modifiquen el inventario deben realizarse mediante
transacciones para evitar inconsistencias.
Nunca se debe permitir que una operación deje el stock por debajo de cero.

### Una distinción importante

Tenemos dos conceptos diferentes:

**`inventario`**

```text
¿Cuánto tengo AHORA?
        ↓
stock_actual = 15

movimiento_inventario

¿Qué pasó con ese stock?
        ↓
ENTRADA +10
SALIDA -2
DEVOLUCION +1
AJUSTE ...

Esto nos permite saber tanto el estado actual como la trazabilidad histórica.

Por ejemplo:

Stock actual: 14

Historial:
ENTRADA       10
SALIDA         2
DEVOLUCION     1
...

## 4.8 Tabla: rol

### Descripción

Almacena los diferentes roles de acceso definidos para los usuarios
internos del sistema Urban Style. Los roles permiten establecer qué
operaciones puede realizar cada usuario dentro de la aplicación.

| Campo | Tipo de dato | Clave | Obligatorio | Descripción |
|---|---|---|---|---|
| id_rol | INT | PK | Sí | Identificador único del rol. |
| nombre | VARCHAR(50) | UNIQUE | Sí | Nombre del rol asignado al usuario. |
| descripcion | VARCHAR(255) | — | No | Descripción de las responsabilidades y permisos generales del rol. |

### Restricciones

- `id_rol` es la clave primaria.
- `nombre` debe ser único.
- Un rol puede estar asociado a múltiples usuarios.
- Los roles permiten controlar el acceso a las funcionalidades del sistema.

### Relación

```text
ROL 1 ─────── N USUARIO

Un rol puede ser asignado a múltiples usuarios, mientras que cada usuario
posee un único rol.

Roles iniciales

Urban Style contempla inicialmente tres roles:

Rol	Responsabilidad general
Administrador	Gestiona usuarios, productos, precios, pedidos, inventario y reportes.
Vendedor	Consulta productos y gestiona las operaciones relacionadas con pedidos y ventas según sus permisos.
Inventario	Gestiona existencias y movimientos de inventario.
Reglas de negocio
Cada usuario interno debe tener un rol asignado.
El Administrador posee el nivel de acceso más alto.
El Vendedor no puede eliminar productos ni modificar precios.
El usuario de Inventario puede actualizar existencias y registrar
movimientos de inventario.
Las operaciones que modifiquen información sensible deben estar
restringidas según el rol del usuario autenticado.
Los permisos definitivos serán implementados posteriormente en el backend
mediante Spring Security.

### ¿Por qué `rol` es una tabla independiente?

En vez de guardar directamente:

```text
usuario
rol = "Administrador"

reutilizamos un registro:

ROL
│
├── Administrador
├── Vendedor
└── Inventario

Y usuario.id_rol apunta al rol correspondiente.

Por ejemplo:

Ana Martínez
     ↓
id_rol = 1
     ↓
Administrador

Esto nos permitirá posteriormente implementar autenticación y autorización en Spring Security sin tener que modificar el diseño de la base de datos.

## 4.9 Tabla: usuario

### Descripción

Almacena las cuentas de los usuarios internos que tienen acceso al sistema
Urban Style. Cada usuario posee credenciales de acceso y un rol que determina
las funcionalidades que puede utilizar.

Los usuarios internos corresponden principalmente al Administrador, los
Vendedores y el encargado de Inventario.

| Campo | Tipo de dato | Clave | Obligatorio | Descripción |
|---|---|---|---|---|
| id_usuario | INT | PK | Sí | Identificador único del usuario. |
| nombre | VARCHAR(100) | — | Sí | Nombre del usuario. |
| apellido | VARCHAR(100) | — | Sí | Apellido del usuario. |
| correo | VARCHAR(150) | UNIQUE | Sí | Correo utilizado como identificador de acceso. |
| password_hash | VARCHAR(255) | — | Sí | Contraseña almacenada mediante un algoritmo de hash seguro. |
| estado | BOOLEAN | — | Sí | Indica si la cuenta está activa o inactiva. |
| id_rol | INT | FK | Sí | Identificador del rol asignado al usuario. |

### Restricciones

- `id_usuario` es la clave primaria.
- `correo` debe ser único.
- `id_rol` debe corresponder a un rol existente.
- Cada usuario debe tener exactamente un rol.
- Una cuenta puede desactivarse mediante el campo `estado`.
- La contraseña nunca debe almacenarse en texto plano.
- `password_hash` debe almacenar únicamente el resultado de un algoritmo de
  hash seguro.
- El acceso y los permisos serán controlados posteriormente desde el backend.

### Relación

```text
ROL 1 ─────── N USUARIO

Un rol puede tener múltiples usuarios, mientras que cada usuario pertenece a
un único rol.

Además, un usuario puede participar en diferentes operaciones del sistema:

USUARIO
   │
   ├── Confirmación de pedidos
   │
   ├── Movimientos de inventario
   │
   ├── Devoluciones
   │
   └── Historial de cambios de precio
Usuarios iniciales

El entorno de prueba contempla inicialmente:

Usuario	Rol
Ana Martínez	Administrador
Carlos Pérez	Vendedor
Laura Gómez	Vendedor
Andrés Ruiz	Vendedor
Pedro Martínez	Inventario
Reglas de negocio
Solo usuarios autenticados pueden acceder a las funciones administrativas
del sistema.
El Administrador puede gestionar usuarios y asignar roles.
El Vendedor puede realizar las operaciones permitidas para su rol.
El usuario de Inventario puede gestionar existencias y movimientos.
Un usuario inactivo no debe poder iniciar sesión.
Los cambios realizados por usuarios sobre operaciones sensibles deben
conservar la identificación del usuario responsable.
Las contraseñas reales serán protegidas mediante BCrypt u otro mecanismo
criptográfico seguro implementado en Spring Security.


###Algo importante sobre las contraseñas

En nuestro SQL actual pusimos valores como:

```text
HASH_DE_PRUEBA_ADMIN

Eso es intencional.

No vamos a poner contraseñas reales en el archivo SQL ni en GitHub.

Cuando construyamos Spring Boot, generaremos hashes BCrypt reales y configuraremos la autenticación correctamente.

Así evitamos una práctica muy peligrosa: guardar credenciales en texto plano dentro del repositorio.

## 4.10 Tabla: cliente

### Descripción

Almacena la información de los clientes que realizan pedidos en Urban Style.
La información registrada permite identificar al comprador y disponer de los
datos necesarios para gestionar la entrega de los productos.

En la versión inicial del sistema, el cliente no necesita crear una cuenta
para realizar un pedido. Sus datos se registran cuando genera el pedido.

| Campo | Tipo de dato | Clave | Obligatorio | Descripción |
|---|---|---|---|---|
| id_cliente | INT | PK | Sí | Identificador único del cliente. |
| nombre | VARCHAR(100) | — | Sí | Nombre completo del cliente. |
| telefono | VARCHAR(20) | — | Sí | Número telefónico utilizado para contacto. |
| correo | VARCHAR(150) | — | No | Correo electrónico del cliente. |
| direccion | VARCHAR(200) | — | Sí | Dirección de entrega. |
| barrio | VARCHAR(100) | — | Sí | Barrio donde se realizará la entrega. |
| referencia_direccion | VARCHAR(255) | — | No | Información adicional para facilitar la ubicación del domicilio. |

### Restricciones

- `id_cliente` es la clave primaria.
- El nombre del cliente es obligatorio.
- El teléfono es obligatorio porque permite contactar al cliente para
  confirmar o coordinar el pedido.
- El correo electrónico es opcional.
- La dirección y el barrio son obligatorios para gestionar la entrega.
- Un cliente puede realizar múltiples pedidos.
- La información del cliente debe tratarse como información de carácter
  privado y utilizarse únicamente para las operaciones necesarias del
  sistema.

### Relación

```text
CLIENTE 1 ─────── N PEDIDO

Un cliente puede realizar múltiples pedidos, mientras que cada pedido
corresponde a un único cliente.

Datos de entrega

Para el MVP, el sistema requiere como mínimo:

Nombre
Teléfono
Dirección
Barrio
Referencia de dirección

Estos datos serán utilizados posteriormente cuando el pedido sea enviado a
WhatsApp para su confirmación.

Regla de negocio

El cliente puede realizar un pedido sin disponer de una cuenta de usuario
interna. Las cuentas de acceso al sistema corresponden a los usuarios
internos definidos mediante la tabla usuario.

La aplicación debe validar que los datos necesarios para la entrega estén
completos antes de permitir la generación del pedido.


### Una diferencia importante

Tenemos dos conceptos separados:

**`usuario`**

```text
Personas que trabajan dentro de Urban Style
        ↓
Administrador
Vendedor
Inventario

cliente

Persona que compra productos
        ↓
Nombre
Teléfono
Dirección
Barrio
...

No mezclamos ambos conceptos, y eso es importante para el diseño del sistema.

Además, el cliente no necesita iniciar sesión en nuestro MVP. Puede entrar al catálogo, seleccionar sus productos, llenar sus datos y generar el pedido.

## 4.11 pedido

### Descripción

Almacena los pedidos realizados por los clientes. Registra la información
general de la venta, su estado, el vendedor responsable y el usuario que
realizó la confirmación del pedido.

### Campos

| Campo | Tipo | Nulo | Clave | Descripción |
|---|---|---|---|---|
| id_pedido | INT | No | PK | Identificador único del pedido. |
| id_cliente | INT | No | FK | Cliente que realizó el pedido. |
| id_usuario_vendedor | INT | Sí | FK | Usuario responsable de la venta. |
| fecha_pedido | DATETIME | No | | Fecha y hora de creación del pedido. |
| estado | VARCHAR(30) | No | | Estado actual del pedido. |
| total | DECIMAL(10,2) | No | | Valor total del pedido. |
| id_usuario_confirmacion | INT | Sí | FK | Usuario que confirmó el pedido. |
| fecha_confirmacion | DATETIME | Sí | | Fecha y hora de confirmación del pedido. |

### Claves foráneas

- `id_cliente` → `cliente.id_cliente`
- `id_usuario_vendedor` → `usuario.id_usuario`
- `id_usuario_confirmacion` → `usuario.id_usuario`

### Estados permitidos

El pedido puede encontrarse en los siguientes estados:

- `PENDIENTE`
- `CONFIRMADO`
- `EN_PREPARACION`
- `ENVIADO`
- `ENTREGADO`
- `CANCELADO`

### Reglas de negocio

- Todo pedido debe estar asociado a un cliente.
- Un pedido puede tener un vendedor responsable.
- El vendedor responsable corresponde al usuario que gestiona la venta.
- El usuario que confirma el pedido puede ser diferente al vendedor.
- Un pedido se crea inicialmente en estado `PENDIENTE`.
- El inventario no se descuenta mientras el pedido permanezca pendiente.
- Al confirmar el pedido se debe verificar nuevamente la disponibilidad del inventario.
- La confirmación y el descuento de inventario deben ejecutarse dentro de una transacción.
- La fecha y el usuario de confirmación se registran al confirmar el pedido.
- Un pedido puede cancelarse mientras se encuentre en estado `PENDIENTE` o `CONFIRMADO`, de acuerdo con las reglas del sistema.
- Cuando se cancela un pedido confirmado, las unidades descontadas deben ser restauradas al inventario mediante un movimiento de tipo `DEVOLUCION`.
- El total del pedido debe corresponder a la suma de los subtotales de sus detalles.
- El precio utilizado en cada venta se conserva en `detalle_pedido.precio_unitario`, permitiendo mantener el valor histórico de la venta.
- El campo `id_usuario_confirmacion` permite identificar quién realizó la confirmación, sin confundirlo con el vendedor responsable.

### Importancia del vendedor responsable

La separación entre vendedor y usuario confirmador permite generar posteriormente
reportes de ventas por vendedor.

Por ejemplo:

**Vendedor:** Carlos Pérez  
**Confirmado por:** Ana Martínez

De esta manera, el sistema puede determinar quién realizó la venta y quién
autorizó o confirmó el pedido.

```text
PENDIENTE
CONFIRMADO
EN_PREPARACION
ENVIADO
ENTREGADO
CANCELADO

Flujo de estados
PENDIENTE
    │
    ├──────────────→ CANCELADO
    │
    ↓
CONFIRMADO
    │
    ├──────────────→ CANCELADO
    │
    ↓
EN_PREPARACION
    │
    ↓
ENVIADO
    │
    ↓
ENTREGADO

Relaciones
CLIENTE 1 ─────── N PEDIDO

USUARIO 1 ─────── N PEDIDO
                 (usuario que confirma)

PEDIDO 1 ─────── N DETALLE_PEDIDO

PEDIDO 1 ─────── N DEVOLUCION
Reglas de negocio
Todo pedido nuevo comienza en estado PENDIENTE.
Mientras está pendiente, el pedido no descuenta inventario.
La confirmación del pedido debe verificar nuevamente la disponibilidad
del inventario.
Al confirmar un pedido, el sistema debe descontar las unidades de las
variantes correspondientes.
La confirmación debe registrar el usuario responsable y la fecha de
confirmación.
El descuento de inventario y el cambio de estado deben ejecutarse dentro
de una transacción para evitar inconsistencias.
Un pedido pendiente o confirmado puede ser cancelado de acuerdo con las
reglas del negocio.
Cuando se cancela un pedido confirmado, las unidades previamente
descontadas deben regresar al inventario.
Un pedido no debe poder confirmarse si no existe stock suficiente.
Un pedido enviado o entregado no debe tratarse como una simple cancelación.
El total del pedido debe corresponder a la suma de los subtotales de sus
detalles.

### 🔎 Una decisión importante del diseño

Observa que `pedido` **no contiene directamente**:

```text
producto
talla
color
cantidad

Eso pertenece a detalle_pedido.

La estructura queda:

CLIENTE
   │
   ↓
PEDIDO
   │
   ├── Detalle 1 → Variante A → 2 unidades
   ├── Detalle 2 → Variante B → 1 unidad
   └── Detalle 3 → Variante C → 3 unidades

Por ejemplo, un solo pedido podría contener:

Pedido #10
Cliente: Santiago Pérez

2 × CAM-001 / M / Negro
1 × CAM-002 / L / Blanco
1 × GOR-001

El pedido guarda la información general de la compra, mientras que detalle_pedido guardará exactamente qué se compró.

## 4.12 Tabla: detalle_pedido

### Descripción

Almacena los productos específicos incluidos en cada pedido. Cada registro
representa una línea del pedido y relaciona el pedido con una variante
determinada de producto.

La variante permite identificar exactamente el producto, talla y color
seleccionados por el cliente.

| Campo | Tipo de dato | Clave | Obligatorio | Descripción |
|---|---|---|---|---|
| id_detalle | INT | PK | Sí | Identificador único del detalle. |
| id_pedido | INT | FK | Sí | Identificador del pedido al que pertenece el detalle. |
| id_variante | INT | FK | Sí | Identificador de la variante adquirida. |
| cantidad | INT | — | Sí | Número de unidades de la variante solicitadas. |
| precio_unitario | DECIMAL(10,2) | — | Sí | Precio de una unidad en el momento de la compra. |
| subtotal | DECIMAL(10,2) | — | Sí | Valor total de la línea del pedido. |

### Restricciones

- `id_detalle` es la clave primaria.
- `id_pedido` debe corresponder a un pedido existente.
- `id_variante` debe corresponder a una variante existente.
- `cantidad` debe ser mayor que cero.
- `precio_unitario` no puede ser negativo.
- `subtotal` no puede ser negativo.
- El subtotal debe corresponder a:

```text
cantidad × precio_unitario

Si un pedido es eliminado en el contexto permitido por el sistema,
sus detalles pueden eliminarse mediante la relación en cascada definida
en la base de datos.
Relaciones
PEDIDO 1 ─────── N DETALLE_PEDIDO

VARIANTE_PRODUCTO 1 ─────── N DETALLE_PEDIDO

Un pedido puede contener múltiples detalles y una misma variante puede
aparecer en múltiples pedidos.

Ejemplo

Un pedido puede contener:

Producto	Talla	Color	Cantidad	Precio unitario	Subtotal
CAM-001	M	Negro	2	$65.000	$130.000
CAM-002	L	Blanco	1	$55.000	$55.000

El pedido tendría:

Total = $185.000
Regla de negocio

El detalle debe conservar el precio_unitario utilizado en el momento de
la venta, independientemente del precio actual almacenado en producto.

Esto permite conservar el valor histórico de las ventas.

Por ejemplo:

Precio actual del producto:
$70.000

Precio al momento de una venta anterior:
$65.000

El detalle de la venta anterior debe conservar:

precio_unitario = $65.000

aunque posteriormente el precio del producto cambie a $70.000.

La aplicación debe calcular el subtotal a partir de la cantidad y el precio
unitario y posteriormente utilizar la suma de los subtotales para determinar
el total del pedido.

Durante la confirmación del pedido, el sistema debe verificar que exista
stock suficiente para cada variante antes de realizar el descuento del
inventario.


###Esta parte es muy importante

Aquí tenemos una decisión de diseño que protege el **historial financiero**.

Supongamos:

```text
10 de septiembre
CAM-001 = $65.000

Un cliente compra 2:

2 × $65.000 = $130.000

Luego el administrador cambia el precio:

CAM-001 = $70.000

Si nosotros simplemente consultáramos el precio actual del producto para
mostrar la venta antigua, parecería que el cliente pagó:

2 × $70.000 = $140.000 ❌

Por eso detalle_pedido guarda:

precio_unitario = 65000

Así el pedido histórico permanece correcto:

Pedido antiguo
2 × $65.000
= $130.000

Esto, junto con historial_precio, nos da una trazabilidad correcta de los precios.

## 4.13 Tabla: movimiento_inventario

### Descripción

Registra el historial de operaciones que modifican las existencias de las
variantes de productos de Urban Style. Permite mantener trazabilidad sobre
las entradas, salidas, devoluciones y ajustes realizados en el inventario.

Cada movimiento identifica la variante afectada, el usuario responsable,
la cantidad modificada y el estado del stock antes y después de la operación.

| Campo | Tipo de dato | Clave | Obligatorio | Descripción |
|---|---|---|---|---|
| id_movimiento | INT | PK | Sí | Identificador único del movimiento. |
| id_variante | INT | FK | Sí | Identificador de la variante cuyo inventario fue modificado. |
| id_usuario | INT | FK | Sí | Usuario responsable de realizar la operación. |
| tipo_movimiento | VARCHAR(30) | — | Sí | Tipo de operación realizada sobre el inventario. |
| cantidad | INT | — | Sí | Cantidad de unidades afectadas por el movimiento. |
| stock_anterior | INT | — | Sí | Cantidad de stock disponible antes de la operación. |
| stock_posterior | INT | — | Sí | Cantidad de stock disponible después de la operación. |
| motivo | VARCHAR(255) | — | No | Explicación o motivo asociado al movimiento. |
| fecha | DATETIME | — | Sí | Fecha y hora en que se registró el movimiento. |

### Restricciones

- `id_movimiento` es la clave primaria.
- `id_variante` debe corresponder a una variante existente.
- `id_usuario` debe corresponder a un usuario existente.
- `cantidad` debe ser mayor que cero.
- `stock_anterior` no puede ser negativo.
- `stock_posterior` no puede ser negativo.
- `tipo_movimiento` solamente puede utilizar los valores establecidos por el
  sistema.

### Tipos de movimiento

| Tipo | Descripción |
|---|---|
| ENTRADA | Incremento de existencias por ingreso de mercancía. |
| SALIDA | Disminución de existencias por una venta u otra operación autorizada. |
| DEVOLUCION | Incremento de existencias debido a la devolución de un producto resalable. |
| AJUSTE | Modificación manual del inventario por corrección o regularización. |

### Relaciones

```text
VARIANTE_PRODUCTO 1 ─────── N MOVIMIENTO_INVENTARIO

USUARIO 1 ─────── N MOVIMIENTO_INVENTARIO

Una variante puede tener múltiples movimientos durante su ciclo de vida y un
usuario puede registrar múltiples operaciones de inventario.

Ejemplo

Supongamos que una variante tiene:

Stock anterior = 15

Se confirma un pedido de 2 unidades:

Tipo de movimiento = SALIDA
Cantidad = 2
Stock anterior = 15
Stock posterior = 13

Posteriormente se devuelve una unidad en condiciones adecuadas:

Tipo de movimiento = DEVOLUCION
Cantidad = 1
Stock anterior = 13
Stock posterior = 14
Regla de negocio

Cada modificación del inventario debe generar un movimiento que permita
identificar qué ocurrió, cuándo ocurrió y qué usuario realizó la operación.

El valor de stock_anterior debe representar el inventario inmediatamente
antes del movimiento y stock_posterior debe representar el inventario
resultante.

Las operaciones de inventario asociadas a la confirmación, cancelación o
devolución de pedidos deben ejecutarse dentro de una transacción para evitar
que el stock actual y su historial queden inconsistentes.

El sistema nunca debe permitir que stock_posterior sea inferior a cero.

Auditoría

Esta tabla funciona como historial de inventario. No reemplaza a la tabla
inventario, sino que complementa su información:

INVENTARIO
   ↓
Estado actual del stock

MOVIMIENTO_INVENTARIO
   ↓
Historial de cambios del stock

### Una diferencia fundamental

Ahora tenemos claramente separados:

```text
inventario
    ↓
¿Cuánto hay ahora?

movimiento_inventario
    ↓
¿Qué cambios ocurrieron?

Por ejemplo, si mañana el administrador pregunta:

"¿Por qué CAM-001-M-NEG tiene 14 unidades?"

podremos consultar los movimientos y encontrar algo como:

15 → 13    SALIDA       pedido #1
13 → 14    DEVOLUCION   devolución #1

Eso es trazabilidad de inventario.

## 4.14 Tabla: devolucion

### Descripción

Registra las devoluciones realizadas por los clientes sobre productos
incluidos en pedidos de Urban Style. Permite identificar el pedido y detalle
relacionados, el usuario que gestionó la devolución, la cantidad devuelta,
el motivo y el estado físico del producto.

La información registrada permite determinar si el producto devuelto puede
ser reincorporado al inventario.

| Campo | Tipo de dato | Clave | Obligatorio | Descripción |
|---|---|---|---|---|
| id_devolucion | INT | PK | Sí | Identificador único de la devolución. |
| id_pedido | INT | FK | Sí | Identificador del pedido al que pertenece la devolución. |
| id_detalle | INT | FK | Sí | Identificador del detalle del pedido que se está devolviendo. |
| id_usuario | INT | FK | Sí | Usuario interno responsable de gestionar la devolución. |
| cantidad | INT | — | Sí | Cantidad de unidades devueltas. |
| motivo | VARCHAR(100) | — | Sí | Motivo informado para la devolución. |
| estado_producto | VARCHAR(50) | — | Sí | Estado físico en el que se encuentra el producto devuelto. |
| vuelve_inventario | BOOLEAN | — | Sí | Indica si el producto puede regresar al inventario. |
| observaciones | TEXT | — | No | Información adicional sobre la devolución. |
| fecha | DATETIME | — | Sí | Fecha y hora en que se registró la devolución. |

### Restricciones

- `id_devolucion` es la clave primaria.
- `id_pedido` debe corresponder a un pedido existente.
- `id_detalle` debe corresponder a un detalle de pedido existente.
- `id_usuario` debe corresponder a un usuario existente.
- `cantidad` debe ser mayor que cero.
- `estado_producto` solamente puede utilizar los estados establecidos por el
  sistema.
- `vuelve_inventario` determina si las unidades devueltas pueden
  reincorporarse a las existencias.

### Estados del producto

| Estado | Descripción |
|---|---|
| RESALABLE | El producto está en condiciones adecuadas para volver a venderse. |
| NO_RESALABLE | El producto no puede volver a venderse. |
| DEFECTUOSO | El producto presenta un defecto que impide su venta. |
| DANADO | El producto presenta daños que impiden su reincorporación al inventario. |

### Relaciones

```text
PEDIDO 1 ─────── N DEVOLUCION

DETALLE_PEDIDO 1 ─────── N DEVOLUCION

USUARIO 1 ─────── N DEVOLUCION

Una devolución pertenece a un pedido y a un detalle específico de ese
pedido. Un usuario puede gestionar múltiples devoluciones.

Ejemplo

Un cliente devuelve una camiseta del pedido #1 porque la talla no era la
correcta.

Pedido: #1
Producto: CAM-001
Talla: M
Color: Negro
Cantidad devuelta: 1
Motivo: Talla incorrecta
Estado: RESALABLE
Vuelve al inventario: Sí

Si la devolución es resalable, el sistema debe incrementar el stock de la
variante correspondiente.

Regla de negocio

Una devolución debe estar relacionada con un detalle existente del pedido.

La cantidad devuelta no puede superar la cantidad adquirida ni la cantidad
pendiente de devolución de dicho detalle. Esta validación deberá ser
controlada por la lógica de negocio del backend.

Cuando estado_producto = RESALABLE y vuelve_inventario = TRUE, las
unidades deben reincorporarse al inventario y debe registrarse un movimiento
de tipo DEVOLUCION.

Cuando el producto sea NO_RESALABLE, DEFECTUOSO o DANADO, las unidades
no deben reincorporarse al stock disponible.

La creación de la devolución y la actualización del inventario deben
realizarse dentro de una transacción para mantener la consistencia de los
datos.


### Una precisión importante

Aquí estamos manejando **dos niveles de información**:

```text
DEVOLUCION
    ↓
¿Qué devolvió el cliente y por qué?

MOVIMIENTO_INVENTARIO
    ↓
¿Qué ocurrió con el stock como consecuencia?

Por ejemplo:

DEVOLUCIÓN #1
1 unidad
RESALABLE
vuelve_inventario = TRUE
        ↓
MOVIMIENTO_INVENTARIO
DEVOLUCION
13 → 14

Así queda registrada tanto la devolución comercial como su efecto sobre el inventario.

############Esta tabla es la que nos permite cumplir una regla de negocio que definimos desde el principio:

Todo cambio de precio debe quedar auditado.

## 4.15 Tabla: historial_precio

### Descripción

Almacena el historial de modificaciones realizadas sobre el precio de los
productos de Urban Style. Permite identificar el precio anterior, el nuevo
precio, el usuario responsable, la fecha y el motivo del cambio.

Esta tabla proporciona trazabilidad sobre las modificaciones de precios y
permite conservar información histórica aunque el precio actual del producto
cambie posteriormente.

| Campo | Tipo de dato | Clave | Obligatorio | Descripción |
|---|---|---|---|---|
| id_historial_precio | INT | PK | Sí | Identificador único del registro histórico. |
| id_producto | INT | FK | Sí | Identificador del producto cuyo precio fue modificado. |
| id_usuario | INT | FK | Sí | Usuario que realizó el cambio de precio. |
| precio_anterior | DECIMAL(10,2) | — | Sí | Precio que tenía el producto antes de la modificación. |
| precio_nuevo | DECIMAL(10,2) | — | Sí | Nuevo precio asignado al producto. |
| motivo | VARCHAR(255) | — | No | Motivo por el cual se realizó el cambio. |
| fecha | DATETIME | — | Sí | Fecha y hora en que se realizó la modificación. |

### Restricciones

- `id_historial_precio` es la clave primaria.
- `id_producto` debe corresponder a un producto existente.
- `id_usuario` debe corresponder a un usuario existente.
- `precio_anterior` no puede ser negativo.
- `precio_nuevo` no puede ser negativo.
- El precio anterior y el precio nuevo deben ser diferentes.
- Cada modificación de precio debe generar un nuevo registro histórico.
- Los registros históricos no deben modificarse para representar cambios
  posteriores; cada nuevo cambio debe generar un nuevo registro.

### Relaciones

```text
PRODUCTO 1 ─────── N HISTORIAL_PRECIO

USUARIO 1 ─────── N HISTORIAL_PRECIO

Un producto puede tener múltiples cambios de precio a lo largo del tiempo y
un usuario puede realizar múltiples modificaciones autorizadas.

Ejemplo

Supongamos que el producto:

CAM-001
Camiseta Oversize Urban

tenía un precio de:

$65.000

y el Administrador lo cambia a:

$70.000

El sistema registra:

Producto	Precio anterior	Precio nuevo	Usuario	Motivo
CAM-001	$65.000	$70.000	Administrador	Actualización de precio
Regla de negocio

Solamente los usuarios con permisos de Administrador pueden modificar los
precios de los productos.

Cada modificación debe registrar:

Producto
Precio anterior
Precio nuevo
Usuario responsable
Fecha y hora
Motivo

La modificación del precio del producto y la creación del registro de
historial deben realizarse dentro de una transacción para garantizar que
ambas operaciones se completen correctamente.

El precio almacenado en producto representa siempre el precio actual,
mientras que historial_precio conserva los cambios realizados a lo largo
del tiempo.

Los precios utilizados en ventas anteriores se conservan adicionalmente en
detalle_pedido.precio_unitario, garantizando que una modificación posterior
del precio no altere el valor histórico de una venta.


###############Con esto completamos las 15 tablas

Tu diccionario ahora tiene:

```text
1.  categoria
2.  producto
3.  producto_imagen
4.  talla
5.  color
6.  variante_producto
7.  inventario
8.  rol
9.  usuario
10. cliente
11. pedido
12. detalle_pedido
13. movimiento_inventario
14. devolucion
15. historial_precio

