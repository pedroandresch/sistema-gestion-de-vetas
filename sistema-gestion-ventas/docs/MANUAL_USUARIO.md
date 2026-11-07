# Manual de Usuario - Sistema de Gestión de Inventario y Ventas

## 1. Introducción y Estructura General de la Interfaz

El **Sistema de Gestión de Inventario y Ventas** es una aplicación de escritorio diseñada para centralizar las operaciones de catálogo, almacenamiento físico, control de existencias y facturación de una empresa comercial.

La pantalla principal se organiza en dos zonas de interacción visual:
1. **Menú Lateral de Navegación (Izquierda)**: Permite alternar instantáneamente entre los 7 módulos del sistema (`Panel General`, `Productos`, `Clientes`, `Empleados`, `Bodegas`, `Inventario` y `Órdenes de Compra`). El botón del módulo activo se resalta visualmente.
2. **Área de Trabajo Principal (Centro / Derecha)**: Muestra en la parte izquierda o superior el formulario de captura y acciones, y en el resto del espacio la tabla de datos sincronizada con buscador en tiempo real.

---

## 2. Descripción de Pantallas y Módulos

### 2.1. Panel General (Dashboard)
Es la pantalla de bienvenida que ofrece una vista global e indicadores clave de rendimiento (KPIs) del negocio en tiempo real.
- **Tarjetas Superiores de Métricas**:
  - **PRODUCTOS EN CATÁLOGO**: Cantidad de artículos dados de alta.
  - **CLIENTES REGISTRADOS**: Total de compradores en la base de datos.
  - **BODEGAS HABILITADAS**: Número de sedes físicas activas.
  - **INGRESOS COMPLETADOS**: Monto acumulado en Quetzales (`Q`) únicamente de ventas finalizadas (`COMPLETADA`).
- **Tabla de Ocupación de Bodegas**: Muestra por cada bodega el empleado encargado, el espacio ocupado respecto a la capacidad total (ej. `45 / 500`) y las unidades disponibles restantes.
- **Tabla de Órdenes Recientes**: Lista las transacciones más recientes con su número, cliente, monto total y estado.

---

### 2.2. Módulo de Productos
Permite administrar los artículos que comercializa la empresa.

#### Campos del Formulario:
- **Código**: Identificador alfanumérico único (ej. `PROD-005`). No se permiten duplicados.
- **Nombre**: Nombre comercial del artículo (ej. `Monitor Gamer 27 pulgadas`).
- **Marca**: Fabricante o marca (ej. `Samsung`).
- **Precio Unitario (Q)**: Precio de venta al público en Quetzales (debe ser un número mayor a cero).
- **Descripción**: Detalle de especificaciones técnicas.

#### Botones y Acciones:
- **Guardar / Modificar**: Valida que los datos obligatorios no estén vacíos y que el precio sea mayor a cero. Si el código es nuevo, lo registra; si se seleccionó un producto de la tabla, guarda las modificaciones.
- **Nuevo / Limpiar**: Restablece los campos de texto y habilita la caja de código para dar de alta un nuevo producto.
- **Buscador**: Permite escribir parte del código, nombre o marca y presionar **Buscar** para filtrar la tabla en tiempo real. El botón **Ver Todos** restablece la lista completa.
- **Selección de Fila**: Al hacer clic en un producto de la tabla, sus datos se cargan automáticamente en el formulario izquierdo para su edición.

---

### 2.3. Módulo de Clientes
Permite gestionar la cartera de compradores con su información personal y clasificación comercial.

#### Campos del Formulario:
- **DPI**: Número de Documento Personal de Identificación (13 dígitos). Actúa como identificador único.
- **Nombre Completo**: Nombre y apellidos del cliente.
- **Dirección**: Dirección de entrega o residencia.
- **Fecha de Nacimiento**: Formato estándar `AAAA-MM-DD` (ej. `1992-08-25`).
- **Tipo de Cliente**: Selector desplegable con dos opciones exclusivas: `REGULAR` o `VIP`.

#### Acciones:
- Permite registrar nuevos clientes o modificar su dirección, fecha o tipo seleccionándolos en la tabla.
- Barra de búsqueda por DPI o nombre completo.

---

### 2.4. Módulo de Empleados
Administra la nómina de colaboradores que pueden ser asignados como encargados de bodegas.

#### Campos del Formulario:
- **ID Empleado**: Código laboral único (ej. `EMP-04`).
- **Nombre Completo**: Nombre del colaborador.
- **DPI**: Documento Personal de Identificación (debe ser único en la nómina).
- **Dirección**: Domicilio del trabajador.
- **Puesto**: Cargo que desempeña (ej. `Supervisor de Almacén`).
- **Salario Mensual (Q)**: Remuneración en Quetzales (debe ser mayor a cero).

#### Acciones:
- Registro de colaboradores con control estricto de no duplicidad de DPI ni de ID de empleado.
- Barra de búsqueda por ID, nombre o puesto.

---

### 2.5. Módulo de Bodegas
Configura las instalaciones físicas de almacenamiento de la empresa.

#### Campos del Formulario:
- **ID Bodega**: Código identificador único del almacén (ej. `BOD-03`).
- **Empleado Encargado**: Menú desplegable que muestra a los colaboradores registrados en el sistema.
- **Capacidad Máxima (Uds)**: Límite físico en número de unidades que soporta la bodega (debe ser un número entero mayor a cero).
- **Dirección**: Ubicación geográfica de la sede.

#### Acciones:
- **Guardar**: Registra la bodega e inicializa de forma automática su inventario asociado.
- La tabla muestra la capacidad máxima configurada y la ocupación actual en unidades.

---

### 2.6. Módulo de Inventario
Permite abastecer productos en una bodega determinada con control automático de espacio físico.

#### Campos del Formulario:
- **Bodega**: Selector con los identificadores de bodegas activas. Al cambiar de bodega, la etiqueta superior indica inmediatamente el estado de ocupación (ej. *"Ocupado: 120 / 500 uds (Libre: 380 uds)"*).
- **Producto**: Menú desplegable con todos los productos del catálogo.
- **Cantidad**: Número de unidades a ingresar al almacén (entero mayor a cero).

#### Acciones:
- **Ingresar Existencias**: Verifica que las unidades a ingresar no sobrepasen la capacidad libre de la bodega. Si el producto ya existía en esa bodega, suma las existencias; si es el primer ingreso, crea el renglón de stock.
- La tabla inferior lista los productos almacenados en la bodega seleccionada con su stock disponible.

---

### 2.7. Módulo de Órdenes de Compra y Facturación
Contiene el flujo transaccional de ventas dividido en dos pestañas:

#### Pestaña 1: "Nueva Orden de Compra"
1. **Encabezado**:
   - **No. Orden**: Código único de la transacción (ej. `ORD-005`).
   - **Cliente**: Selector del cliente que realiza la compra.
   - **Bodega**: Sede de donde se despachará la mercancía. Al seleccionarla, se actualiza el indicador de stock disponible del producto seleccionado.
2. **Carrito de Artículos**:
   - Selector de **Producto** con visualizador de stock disponible en la bodega elegida.
   - Caja de **Cantidad** a comprar.
   - Botón **Agregar Producto**: Añade el artículo a la tabla temporal del carrito y calcula automáticamente el subtotal (`precio * cantidad`) y el gran total general.
   - Botón **Quitar Seleccionado**: Remueve un artículo del carrito temporal y recalcula el monto.
3. **Acciones de Finalización**:
   - **Guardar PENDIENTE**: Guarda la orden como cotización o pedido en espera. **No descuenta existencias del inventario**.
   - **COMPLETAR VENTA**: Verifica la disponibilidad física de todos los productos y descuenta automáticamente el inventario de la bodega asignada.

#### Pestaña 2: "Historial de Órdenes"
- Lista todas las órdenes registradas históricamente con su fecha, cliente, bodega, total y estado (`PENDIENTE`, `COMPLETADA`, `CANCELADA`).
- Al seleccionar una orden de la tabla principal, la tabla derecha **Detalle de la Orden** muestra de inmediato los productos, cantidades y subtotales de dicha compra.
- Botón **Completar Orden Seleccionada**: Permite tomar una orden que estaba `PENDIENTE`, validar las existencias y pasarla a `COMPLETADA` descontando el stock correspondiente.
- Botón **Cancelar Orden**: Anula una orden pendiente. El sistema impide cancelar órdenes que ya fueron completadas.

---

## 3. Guía Paso a Paso de Flujos Operativos Reales

### Flujo A: Cómo registrar un nuevo producto y un cliente
1. Vaya al módulo **Productos** en el menú lateral.
2. Ingrese los datos: Código `PROD-010`, Nombre `Mouse Inalámbrico`, Marca `Logitech`, Precio `150.00`, Descripción `Conexión Bluetooth y receptor USB`.
3. Presione **Guardar**. El sistema confirmará con un mensaje y el artículo aparecerá en la tabla.
4. Vaya al módulo **Clientes**.
5. Ingrese el DPI `1001200230105`, Nombre `Ana Morales`, Dirección `Zona 14`, Fecha `1995-10-12` y seleccione Tipo `VIP`.
6. Presione **Guardar**. El cliente quedará disponible para facturar.

---

### Flujo B: Cómo crear una bodega y abastecerle inventario
1. Vaya a **Empleados** y asegúrese de que haya al menos un empleado registrado (ej. `EMP-01`).
2. Vaya a **Bodegas**. Ingrese ID `BOD-02`, elija el empleado encargado, defina la capacidad en `200` y escriba la dirección `Carretera a El Salvador`.
3. Presione **Guardar**.
4. Vaya al módulo **Inventario**.
5. En el combo Bodega seleccione `BOD-02`. Observe que la etiqueta indica `Libre: 200 uds`.
6. Seleccione el producto `Mouse Inalámbrico` e ingrese cantidad `50`.
7. Presione **Ingresar Existencias**. La tabla mostrará 50 unidades disponibles y la capacidad libre descenderá a `150 uds`.

---

### Flujo C: Cómo realizar una venta con descuento automático de inventario
1. Vaya al módulo **Órdenes de Compra**.
2. En la pestaña *Nueva Orden de Compra*, escriba el número de orden `ORD-010`.
3. Seleccione la clienta `Ana Morales` y la bodega `BOD-02`.
4. En la barra de producto elija `Mouse Inalámbrico`. El sistema mostrará `Stock: 50 uds`.
5. En cantidad escriba `5` y presione **Agregar Producto**. En el carrito aparecerá el subtotal `Q 750.00` y el TOTAL `Q 750.00`.
6. Presione el botón verde **COMPLETAR VENTA**.
7. El sistema confirmará que la orden se registró como `COMPLETADA` y abrirá la pestaña de historial.
8. Regrese al módulo **Inventario**: la existencia de `Mouse Inalámbrico` en `BOD-02` habrá bajado exactamente de 50 a **45 unidades**.
9. Vaya al **Panel General**: el indicador de ingresos totales reflejará los `Q 750.00` de la venta.

---

## 4. Guía de Mensajes del Sistema y Solución de Problemas

| Mensaje de Diálogo | Causa Técnica | Solución para el Usuario |
| :--- | :--- | :--- |
| *"El precio debe ser mayor a 0."* | Se intentó guardar un producto con precio negativo o cero. | Ingrese un valor monetario positivo mayor a 0.00. |
| *"El salario debe ser mayor a 0."* | Se ingresó un salario negativo o en cero para un colaborador. | Especifique un salario mensual mayor a cero. |
| *"La capacidad debe ser mayor a 0."* | Se ingresó una capacidad de bodega menor o igual a cero. | Ingrese una cantidad entera de unidades reales que soporta el almacén. |
| *"Debe ser un número válido"* | Se introdujeron letras, espacios o caracteres no numéricos en un campo numérico. | Escriba solo dígitos numéricos (usando punto `.` para decimales). |
| *"Ya existe un producto con el código..."* | El código ingresado ya pertenece a otro artículo del catálogo. | Ingrese un código diferente o seleccione el producto en la tabla para modificarlo. |
| *"Ya existe un cliente con el DPI..."* | El número de DPI ya se encuentra registrado. | Verifique el número de DPI o consulte la tabla de clientes. |
| *"Capacidad excedida en bodega..."* | La suma de unidades actuales más el nuevo lote supera el límite de la bodega. | Ingrese un lote menor que quepa en el espacio restante indicado por el mensaje. |
| *"Stock insuficiente en bodega..."* | Se intentó vender una cantidad mayor a la existencia física en la bodega de despacho. | Reduzca la cantidad solicitada en la orden o abastezca inventario en la bodega antes de completar la venta. |
| *"No se puede cancelar una orden que ya fue COMPLETADA."* | Se intentó anular una orden que ya había descontado inventario y cerrado la facturación. | Las órdenes completadas son definitivas para proteger la consistencia contable del sistema. |
