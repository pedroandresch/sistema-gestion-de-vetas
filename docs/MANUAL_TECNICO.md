# Manual Técnico del Sistema

**Sistema**: Control de Inventario y Ventas  
**Arquitectura**: Modelo - Vista - Controlador (MVC)  
**Lenguaje**: Java SE (JDK 17 / 21+)  
**Librerías externas**: 
- `Google Gson 2.10.1` (Serialización y persistencia JSON)
- `FlatLaf 3.4` (Estilos y tema visual moderno para Swing)

---

## 1. Estructura del Código y Patrón MVC

El sistema está organizado bajo el paquete raíz `com.sistema`:

```text
src/main/java/com/sistema/
├── Main.java                       # Punto de entrada y configuración del tema FlatLaf
├── model/                          # Entidades del dominio (datos y reglas internas)
│   ├── Persona.java                # Superclase abstracta (DPI, nombre, dirección)
│   ├── Cliente.java                # Subclase con fechaNacimiento y TipoCliente
│   ├── TipoCliente.java            # Enum (REGULAR, VIP)
│   ├── Empleado.java               # Subclase con idEmpleado, puesto y salario
│   ├── Producto.java               # Código, nombre, marca y precio
│   ├── Bodega.java                 # Capacidad física y Empleado encargado
│   ├── ItemInventario.java         # Renglón de stock de un producto
│   ├── Inventario.java             # Existencias asociadas a una bodega
│   ├── DetalleOrden.java           # Renglón de compra (producto, cantidad, subtotal)
│   ├── OrdenCompra.java            # Cabecera de venta (cliente, items, total, estado)
│   └── EstadoOrden.java            # Enum (PENDIENTE, COMPLETADA, CANCELADA)
├── persistence/                    
│   └── JsonRepository.java         # Lectura y escritura genérica de archivos JSON
├── controller/                     
│   └── SistemaController.java      # Validaciones, colecciones en memoria y lógica comercial
└── view/                           # Interfaz gráfica en Java Swing
    ├── MainFrame.java              # Ventana principal con barra lateral y CardLayout
    ├── UITheme.java                # Colores, fuentes y componentes estilizados
    ├── DashboardPanel.java         # Panel con métricas de ventas, stock y clientes
    ├── ProductoPanel.java          # Mantenimiento de catálogo de productos
    ├── ClientePanel.java           # Registro y búsqueda de clientes
    ├── EmpleadoPanel.java          # Registro de empleados
    ├── BodegaPanel.java            # Registro de bodegas
    ├── InventarioPanel.java        # Entrada de stock por bodega con barra de capacidad
    └── OrdenCompraPanel.java       # Carrito de facturación e historial de órdenes
```

### Flujo de Trabajo entre Capas
1. **La Vista (`view`)**: Captura la interacción del usuario (botones, tablas, formularios). Nunca lee ni escribe archivos directamente. Cuando el usuario realiza una acción, la vista llama a un método del `SistemaController`.
2. **El Controlador (`controller`)**: Valida reglas de negocio (ej. que haya espacio libre en la bodega o que el producto no esté duplicado), actualiza las listas en memoria y coordina la persistencia.
3. **El Modelo (`model`)**: Contiene los datos y la lógica atómica (como calcular subtotales).
4. **La Persistencia (`persistence`)**: `JsonRepository` convierte las listas de Java a archivos `.json` en la carpeta `data/` usando la librería Gson.

---

## 2. Reglas de Negocio Implementadas

### 2.1. Control de Capacidad Máxima de Bodegas
En `SistemaController.registrarStock(...)`:
* Cada bodega tiene una capacidad máxima en unidades (ej. 500).
* Antes de ingresar un nuevo lote, el sistema suma las existencias actuales de todos los productos en esa bodega (`getTotalUnidadesAlmacenadas()`).
* Si la suma de las existencias actuales más la nueva cantidad supera el límite, el sistema rechaza el ingreso, lanza una excepción y muestra cuántas unidades libres quedan:
  $$\text{Espacio Disponible} = \text{Capacidad} - \text{Ocupación Actual}$$

### 2.2. Algoritmo de Descuento de Stock en Dos Fases
Para evitar errores donde se descuenta un producto pero otro no tiene existencias, el método `completarOrden(idOrden)` en `SistemaController` opera en dos fases estrictas:

* **Fase 1 (Verificación completa previa)**:  
  Recorre todos los productos del carrito. Si **tan solo uno** de los productos no tiene stock suficiente en la bodega de despacho, el proceso se aborta de inmediato con una alerta clara y **no se descuenta absolutamente nada**.
* **Fase 2 (Descuento y confirmación)**:  
  Habiendo verificado que hay existencias para el 100% del pedido, se procede a descontar las unidades de la bodega, se cambia el estado de la orden a `COMPLETADA` y se guardan los cambios en `data/inventarios.json` y `data/ordenes.json`.

---

## 3. Almacenamiento y Estructura JSON (`data/`)

Los datos se guardan en formato JSON plano con codificación UTF-8:

* `data/productos.json`: Catálogo de artículos con código, nombre, descripción, marca y precio.
* `data/clientes.json`: Clientes con DPI, nombre, dirección, fecha y tipo (`REGULAR` o `VIP`).
* `data/empleados.json`: Nómina con ID, DPI, nombre, puesto y salario mensual.
* `data/bodegas.json`: Sedes con capacidad y objeto del empleado encargado.
* `data/inventarios.json`: Lista de existencias agrupadas por `idBodega`.
* `data/ordenes.json`: Historial de ventas con cliente, items, fecha, total y estado (`PENDIENTE`, `COMPLETADA`, `CANCELADA`).

---

## 4. Guía de Ejecución y Compilación

### 4.1. Opción 1: Con los scripts automáticos (Recomendado)

* **En Windows**: Haz doble clic en `run.bat` o ejecútalo desde CMD/PowerShell:
  ```cmd
  run.bat
  ```
* **En macOS / Linux**:
  ```bash
  chmod +x run.sh
  ./run.sh
  ```

---

### 4.2. Opción 2: Desde NetBeans
1. Abre NetBeans y selecciona **File ➔ Open Project**.
2. Selecciona la carpeta `sistema-gestion-ventas` (la reconocerá automáticamente con el ícono de Maven).
3. Haz clic derecho sobre el proyecto y elige **Clean and Build**.
4. Haz clic derecho sobre `src/main/java/com/sistema/Main.java` y selecciona **Run File** (o `Shift + F6`).

---

### 4.3. Opción 3: Desde Visual Studio Code
1. Abre la carpeta del proyecto en VS Code.
2. Asegúrate de tener instalada la extensión oficial **Extension Pack for Java**.
3. Abre el archivo `Main.java` y haz clic en el botón **Run** que aparece arriba del método `main`.

---

### 4.4. Opción 4: Compilación manual por terminal

**En Windows (CMD):**
```cmd
if not exist "bin" mkdir bin
javac -cp "lib/*" -encoding UTF-8 -d bin src/main/java/com/sistema/model/*.java src/main/java/com/sistema/persistence/*.java src/main/java/com/sistema/controller/*.java src/main/java/com/sistema/view/*.java src/main/java/com/sistema/*.java
java -cp "bin;lib/*" com.sistema.Main
```

**En macOS / Linux:**
```bash
mkdir -p bin
javac -cp "lib/*" -encoding UTF-8 -d bin $(find src/main/java -name "*.java")
java -cp "bin:lib/*" com.sistema.Main
```
