# Manual de Usuario

**Sistema**: Control de Inventario y Ventas  
**Tipo**: Aplicación de Escritorio (Java Swing)  

---

## 1. Primeros Pasos y Navegación

Al iniciar la aplicación verás una ventana principal moderna dividida en dos secciones:

1. **Barra Lateral de Navegación (Izquierda)**: Contiene los accesos directos a los 7 módulos del sistema:
   * **Dashboard (Panel General)**: Resumen en vivo de ingresos, productos, bodegas y ventas.
   * **Productos**: Catálogo comercial y precios.
   * **Clientes**: Directorio de compradores (Regulares y VIP).
   * **Empleados**: Registro de trabajadores y encargados de bodegas.
   * **Bodegas**: Sedes físicas de almacenamiento y capacidades máximas.
   * **Inventario**: Abastecimiento de existencias por bodega.
   * **Órdenes de Compra**: Facturación, carrito de ventas e historial de pedidos.
2. **Área de Trabajo (Centro y Derecha)**: Muestra el formulario para ingresar o editar datos y la tabla con los registros actuales y buscador.

---

## 2. Orden Recomendado para Ingresar Datos por Primera Vez

Para que el sistema funcione fluidamente y las listas desplegables tengan información, el orden lógico para cargar los datos es:

```text
1. Empleados  ──➔  2. Bodegas (asigna encargado)  ──➔  3. Productos
                                                               │
                                                               ▼
6. Facturar Orden  ◄──  5. Clientes  ◄──  4. Inventario (abastecer stock)
```

---

## 3. Uso Módulo por Módulo

### 3.1. Productos
* **Para agregar un producto**: Escribe el código (ej. `PROD-01`), nombre, marca, precio unitario en Quetzales y una descripción breve. Presiona **Guardar**.
* **Para editar**: Haz clic sobre cualquier fila de la tabla; los datos se pasarán al formulario automáticamente. Modifica los campos que quieras y presiona **Guardar**.
* **Buscador**: Puedes escribir parte del código o nombre y hacer clic en **Buscar** para filtrar la tabla.

---

### 3.2. Clientes
* **Registro**: Solicita el DPI (13 dígitos), nombre completo, dirección, fecha de nacimiento (`AAAA-MM-DD`) y si es cliente `REGULAR` o `VIP`.
* **Regla**: El DPI debe ser único en el sistema.

---

### 3.3. Empleados
* **Registro**: Solicita ID de empleado (ej. `EMP-01`), nombre, DPI, puesto y salario mensual.
* **Uso en el sistema**: Los empleados dados de alta aquí podrán ser seleccionados como encargados de una bodega.

---

### 3.4. Bodegas
* **Registro**: Asigna un ID (ej. `BOD-01`), dirección, capacidad máxima (en unidades totales que soporta el almacén) y selecciona al empleado responsable del combo desplegable.

---

### 3.5. Inventario (Abastecimiento de Stock)
1. Selecciona la bodega que deseas abastecer en el menú desplegable.
2. La barra de estado te mostrará cuántas unidades tiene ocupadas y cuántas le quedan libres.
3. Elige el producto del catálogo y escribe la cantidad a ingresar.
4. Presiona **Ingresar Existencias**:
   * Si la bodega tiene espacio suficiente, el stock se sumará de inmediato.
   * Si intentas meter más unidades de las que soporta la bodega, el sistema te avisará con una alerta y no permitirá el ingreso.

---

### 3.6. Órdenes de Compra y Facturación
Tiene dos pestañas principales:

#### Pestaña 1: "Nueva Orden de Compra" (Crear venta)
1. Ingresa el código de la orden (ej. `ORD-01`).
2. Selecciona al **Cliente** y la **Bodega** desde donde saldrá el producto.
3. Elige el producto en el selector; el sistema te indicará en tiempo real cuánto stock hay disponible en esa bodega específica.
4. Escribe la cantidad y presiona **Agregar Producto** para meterlo al carrito.
5. Puedes agregar varios productos diferentes al mismo pedido.
6. Al finalizar tienes dos opciones:
   * **Completar Venta**: Valida las existencias y descuenta el stock de la bodega automáticamente.
   * **Guardar Pendiente**: Guarda el pedido como cotización o reserva sin descontar inventario aún.

#### Pestaña 2: "Historial de Órdenes"
* Muestra todas las ventas realizadas con su fecha, cliente, monto total y estado (`PENDIENTE`, `COMPLETADA` o `CANCELADA`).
* Al seleccionar una orden en la tabla, verás el detalle de los productos comprados a la derecha.
* Si una orden quedó como `PENDIENTE`, puedes seleccionarla y presionar **Completar Orden Seleccionada** cuando el cliente decida pagarla.

---

## 4. Ejemplo Práctico: Tu Primera Venta Completa en 4 Pasos

1. **Crear Empleado y Bodega**:
   * En *Empleados*, registra a `EMP-01`, `Carlos Gómez`, puesto `Encargado`.
   * En *Bodegas*, crea `BOD-01` con capacidad `100` y asigna a Carlos.
2. **Crear Producto e Ingresar Stock**:
   * En *Productos*, crea `PROD-01` (`Laptop HP`, Precio `Q 5,000.00`).
   * En *Inventario*, selecciona `BOD-01`, elige `Laptop HP` y agrega `10` unidades.
3. **Registrar al Cliente**:
   * En *Clientes*, registra a `Ana Morales` con DPI `1234567890101`.
4. **Realizar la Venta**:
   * En *Órdenes de Compra*, pon No. de Orden `ORD-01`, elige a Ana y la bodega `BOD-01`.
   * Agrega `2` Laptops y presiona **Completar Venta**.
   * **Resultado**: La orden pasará a `COMPLETADA`, el stock en `BOD-01` bajará de 10 a 8 unidades, y en el **Dashboard** verás reflejados los `Q 10,000.00` de ingreso.

---

## 5. Preguntas Frecuentes y Solución de Problemas

| Situación / Mensaje | ¿Por qué ocurre? | ¿Cómo se soluciona? |
| :--- | :--- | :--- |
| **"Capacidad excedida en bodega..."** | El lote que intentas ingresar supera las unidades libres de la bodega. | Revisa el espacio disponible en la barra de inventario e ingresa una cantidad menor. |
| **"Stock insuficiente en bodega..."** | Estás intentando vender más unidades de las que existen en esa sede. | Elige otra bodega con existencias o abastece inventario primero. |
| **"No se puede cancelar una orden COMPLETADA"** | La venta ya fue cerrada y el stock ya fue descontado. | Por seguridad contable, una orden completada no se puede anular directamente. |
| **"El precio / salario / cantidad debe ser mayor a 0"** | Se ingresó un cero, un número negativo o letras en un campo numérico. | Escribe un número positivo válido. Usa punto `.` para los decimales (ej. `150.50`). |
| **"Ya existe un registro con ese código/DPI"** | Los códigos y DPIs deben ser únicos en todo el sistema. | Escribe un código o DPI distinto, o edita el registro existente desde la tabla. |
