# Manual Técnico - Sistema de Gestión de Inventario y Ventas

## 1. Especificaciones del Entorno y Tecnologías

- **Lenguaje de Programación**: Java SE (Compatible con JDK 17, JDK 21 o superior).
- **Paradigma**: Programación Orientada a Objetos (POO).
- **Patrón de Arquitectura**: Modelo - Vista - Controlador (MVC).
- **Interfaz Gráfica de Usuario (GUI)**: Biblioteca estándar `javax.swing` y `java.awt`, estilizada con el tema moderno `FlatLaf 3.4` (`lib/flatlaf-3.4.jar`).
- **Mecanismo de Persistencia**: Archivos de texto plano en formato JSON serializados mediante la biblioteca `Google Gson 2.10.1` (`lib/gson-2.10.1.jar`).
- **Codificación de Caracteres**: `UTF-8` estricto en todas las operaciones de entrada/salida (`StandardCharsets.UTF_8`).
- **Sistemas de Construcción**:
  - Compilación nativa por lotes mediante scripts (`run.sh` en UNIX/macOS y `run.bat` en Windows).
  - Gestión de dependencias y construcción estándar con `Apache Maven` (`pom.xml`).

---

## 2. Arquitectura del Software y Estructura de Paquetes (MVC)

El código fuente se aloja bajo el paquete raíz `com.sistema` (`src/main/java/com/sistema/`), organizado de acuerdo con el patrón Modelo-Vista-Controlador:

```text
src/main/java/com/sistema/
├── Main.java                       # Punto de entrada de la aplicación y arranque en el EDT
├── model/                          # CAPA DE MODELO: Entidades del dominio y enumeraciones
│   ├── Persona.java                # Superclase abstracta con atributos dpi, nombre y direccion
│   ├── Cliente.java                # Subclase de Persona con fechaNacimiento y TipoCliente
│   ├── TipoCliente.java            # Enumeración de tipos de cliente: REGULAR, VIP
│   ├── Empleado.java               # Subclase de Persona con idEmpleado, puesto y salario
│   ├── Producto.java               # Entidad de catálogo con codigo, nombre, marca y precio
│   ├── Bodega.java                 # Entidad de almacén físico con Empleado encargado y capacidad
│   ├── ItemInventario.java         # Renglón de existencia asociando Producto y cantidad en stock
│   ├── Inventario.java             # Colección de items de stock por cada sede de bodega
│   ├── DetalleOrden.java           # Renglón de orden de compra con Producto, cantidad y subtotal
│   ├── OrdenCompra.java            # Cabecera de venta con Cliente, idBodega, items, fecha y total
│   └── EstadoOrden.java            # Enumeración de estados de orden: PENDIENTE, COMPLETADA, CANCELADA
├── persistence/                    # CAPA DE PERSISTENCIA: Entrada/Salida a disco
│   └── JsonRepository.java         # Serialización y deserialización genérica en formato JSON
├── controller/                     # CAPA DE CONTROLADOR: Lógica de negocio y orquestación
│   └── SistemaController.java      # Gestión de colecciones en memoria, reglas de negocio y stock
└── view/                           # CAPA DE VISTA: Interfaz gráfica en Java Swing
    ├── MainFrame.java              # Ventana principal con menú lateral y CardLayout
    ├── UITheme.java                # Paleta de colores, tipografías y estilos de tarjetas/tablas
    ├── DashboardPanel.java         # Panel General con métricas e indicadores en tiempo real
    ├── ProductoPanel.java          # Formulario y tabla de catálogo de productos
    ├── ClientePanel.java           # Formulario y directorio comercial de clientes
    ├── EmpleadoPanel.java          # Formulario y nómina de colaboradores
    ├── BodegaPanel.java            # Formulario de registro de bodegas y asignación de encargado
    ├── InventarioPanel.java        # Formulario de abastecimiento y existencias por bodega
    └── OrdenCompraPanel.java       # Carrito de ventas, facturación e historial de órdenes
```

### 2.1. Responsabilidades por Capa

1. **Capa de Modelo (`com.sistema.model`)**:
   Representa las entidades del negocio. Encapsula sus atributos en variables privadas o protegidas y ofrece métodos mutadores y accesores controlados. Implementa lógica interna de cálculo (como `calcularSubtotal()` en `DetalleOrden`, `calcularTotal()` en `OrdenCompra` y `restarStock()` en `ItemInventario`). No posee dependencias con librerías gráficas ni con el almacenamiento en disco.

2. **Capa de Vista (`com.sistema.view`)**:
   Construida con Java Swing. Su función es capturar los eventos del usuario (clics, texto introducido, selecciones en combos y tablas) y mostrar información procesada. Las vistas nunca manipulan directamente los archivos JSON ni validan unicidad de negocio; delegan todas las operaciones al controlador mediante llamadas a métodos públicos.

3. **Capa de Controlador (`com.sistema.controller`)**:
   La clase `SistemaController` centraliza las colecciones maestras del sistema en memoria RAM (`List<Producto>`, `List<Cliente>`, etc.). Valida que no se dupliquen identificadores únicos (DPI, códigos de producto, ID de bodega, ID de orden), verifica que no se sobrepasen los límites físicos de almacenamiento y aplica el descuento atómico de inventario al completar ventas.

4. **Capa de Persistencia (`com.sistema.persistence`)**:
   La clase `JsonRepository` provee métodos estáticos y genéricos (`<T>`) para leer y escribir colecciones de cualquier tipo hacia archivos JSON, abstrayendo a las demás capas de los detalles de bajo nivel de flujos de archivos (*streams*) y codificación de caracteres.

---

## 3. Diccionario de Datos (Estructura de Archivos JSON)

La información persistente se almacena en el directorio `data/` en seis archivos JSON independientes con formato legible (*pretty printing*):

### 3.1. Archivo `data/productos.json`
Almacena el catálogo de artículos comercializados.
| Campo | Tipo JSON | Tipo Java | Restricciones / Reglas | Descripción |
| :--- | :--- | :--- | :--- | :--- |
| `codigo` | `string` | `String` | Obligatorio, clave única, no vacío | Identificador alfanumérico del producto (ej. `PROD-001`) |
| `nombre` | `string` | `String` | Obligatorio, no vacío | Denominación comercial del artículo |
| `descripcion` | `string` | `String` | Opcional | Detalle de características del producto |
| `marca` | `string` | `String` | Opcional | Marca o fabricante |
| `precio` | `number` | `double` | Obligatorio, `precio > 0` | Precio unitario de venta en Quetzales (Q) |

*Ejemplo JSON:*
```json
[
  {
    "codigo": "PROD-001",
    "nombre": "Laptop HP Pavilion",
    "descripcion": "Core i5, 16GB RAM, 512GB SSD",
    "marca": "HP",
    "precio": 5500.0
  }
]
```

### 3.2. Archivo `data/clientes.json`
Almacena el directorio de clientes registrados.
| Campo | Tipo JSON | Tipo Java | Restricciones / Reglas | Descripción |
| :--- | :--- | :--- | :--- | :--- |
| `dpi` | `string` | `String` | Obligatorio, clave única, no vacío | Documento Personal de Identificación (13 dígitos) |
| `nombre` | `string` | `String` | Obligatorio, no vacío | Nombre completo del cliente |
| `direccion` | `string` | `String` | Opcional | Domicilio de residencia o entrega |
| `fechaNacimiento` | `string` | `String` | Formato estándar `AAAA-MM-DD` | Fecha de nacimiento |
| `tipo` | `string` | `TipoCliente` | Valores permitidos: `"REGULAR"`, `"VIP"` | Clasificación comercial del cliente |

*Ejemplo JSON:*
```json
[
  {
    "dpi": "1001200230101",
    "nombre": "Carlos Mendez",
    "direccion": "Zona 10, Ciudad de Guatemala",
    "fechaNacimiento": "1990-05-15",
    "tipo": "VIP"
  }
]
```

### 3.3. Archivo `data/empleados.json`
Almacena la nómina de colaboradores de la empresa.
| Campo | Tipo JSON | Tipo Java | Restricciones / Reglas | Descripción |
| :--- | :--- | :--- | :--- | :--- |
| `idEmpleado` | `string` | `String` | Obligatorio, clave única, no vacío | Código de colaborador (ej. `EMP-01`) |
| `nombre` | `string` | `String` | Obligatorio, no vacío | Nombre completo del colaborador |
| `dpi` | `string` | `String` | Obligatorio, clave única | DPI del empleado |
| `direccion` | `string` | `String` | Opcional | Dirección de residencia |
| `puesto` | `string` | `String` | Obligatorio | Puesto laboral desempeñado |
| `salario` | `number` | `double` | Obligatorio, `salario > 0` | Sueldo mensual en Quetzales (Q) |

*Ejemplo JSON:*
```json
[
  {
    "idEmpleado": "EMP-01",
    "puesto": "Jefe de Bodega",
    "salario": 4500.0,
    "dpi": "2001300240101",
    "nombre": "Mario Lopez",
    "direccion": "Zona 1, Mixco"
  }
]
```

### 3.4. Archivo `data/bodegas.json`
Almacena las sedes físicas de almacenamiento. Contiene una relación de **Agregación** con el empleado encargado embebido.
| Campo | Tipo JSON | Tipo Java | Restricciones / Reglas | Descripción |
| :--- | :--- | :--- | :--- | :--- |
| `idBodega` | `string` | `String` | Obligatorio, clave única | Código identificador de la bodega (ej. `BOD-01`) |
| `empleado` | `object` | `Empleado` | Obligatorio, referencia válida | Objeto con los datos del colaborador responsable |
| `capacidad` | `number` | `int` | Obligatorio, `capacidad > 0` | Capacidad máxima en unidades físicas |
| `direccion` | `string` | `String` | Obligatorio | Ubicación física del almacén |

*Ejemplo JSON:*
```json
[
  {
    "idBodega": "BOD-01",
    "empleado": {
      "idEmpleado": "EMP-01",
      "puesto": "Jefe de Bodega",
      "salario": 4500.0,
      "dpi": "2001300240101",
      "nombre": "Mario Lopez",
      "direccion": "Zona 1, Mixco"
    },
    "capacidad": 500,
    "direccion": "Parque Industrial Zona 12"
  }
]
```

### 3.5. Archivo `data/inventarios.json`
Almacena el detalle de existencias por bodega. Contiene una relación de **Composición** con el arreglo de ítems de stock.
| Campo | Tipo JSON | Tipo Java | Restricciones / Reglas | Descripción |
| :--- | :--- | :--- | :--- | :--- |
| `idBodega` | `string` | `String` | Obligatorio, clave foránea hacia Bodega | ID de la bodega física |
| `items` | `array` | `List<ItemInventario>` | Colección de ítems de stock | Lista de renglones de existencias |
| `items[].producto` | `object` | `Producto` | Referencia al catálogo | Objeto del producto almacenado |
| `items[].stock` | `number` | `int` | Obligatorio, `stock >= 0` | Unidades disponibles en la bodega |

*Ejemplo JSON:*
```json
[
  {
    "idBodega": "BOD-01",
    "items": [
      {
        "producto": {
          "codigo": "PROD-001",
          "nombre": "Laptop HP Pavilion",
          "descripcion": "Core i5, 16GB RAM",
          "marca": "HP",
          "precio": 5500.0
        },
        "stock": 45
      }
    ]
  }
]
```

### 3.6. Archivo `data/ordenes.json`
Almacena el registro histórico de órdenes de compra y facturación.
| Campo | Tipo JSON | Tipo Java | Restricciones / Reglas | Descripción |
| :--- | :--- | :--- | :--- | :--- |
| `idOrden` | `string` | `String` | Obligatorio, clave única | Número de orden (ej. `ORD-001`) |
| `cliente` | `object` | `Cliente` | Obligatorio, no nulo | Cliente que emite la compra |
| `idBodega` | `string` | `String` | Obligatorio | Bodega física desde donde se despacha |
| `items` | `array` | `List<DetalleOrden>` | Obligatorio, al menos 1 renglón | Lista de productos y cantidades |
| `items[].cantidad` | `number` | `int` | `cantidad > 0` | Unidades compradas |
| `items[].precioUnitario` | `number` | `double` | `precioUnitario > 0` | Precio unitario al momento de la venta |
| `items[].subtotal` | `number` | `double` | `precioUnitario * cantidad` | Subtotal del renglón en Quetzales |
| `fecha` | `string` | `String` | Formato `yyyy-MM-dd HH:mm:ss` | Fecha y hora de creación de la orden |
| `total` | `number` | `double` | Suma de subtotales | Monto total acumulado de la venta |
| `estado` | `string` | `EstadoOrden` | `"PENDIENTE"`, `"COMPLETADA"`, `"CANCELADA"` | Estado de la orden en el sistema |

*Ejemplo JSON:*
```json
[
  {
    "idOrden": "ORD-001",
    "cliente": {
      "fechaNacimiento": "1990-05-15",
      "tipo": "VIP",
      "dpi": "1001200230101",
      "nombre": "Carlos Mendez",
      "direccion": "Zona 10"
    },
    "idBodega": "BOD-01",
    "items": [
      {
        "producto": {
          "codigo": "PROD-001",
          "nombre": "Laptop HP Pavilion",
          "descripcion": "Core i5, 16GB RAM",
          "marca": "HP",
          "precio": 5500.0
        },
        "cantidad": 2,
        "precioUnitario": 5500.0,
        "subtotal": 11000.0
      }
    ],
    "fecha": "2026-11-04 10:30:00",
    "total": 11000.0,
    "estado": "COMPLETADA"
  }
]
```

---

## 4. Reglas de Negocio y Mecanismos de Control

### 4.1. Control de Capacidad Máxima de Almacenamiento
Ubicado en `SistemaController.registrarStock(idBodega, producto, cantidad)`:
1. Valida que la cantidad a ingresar sea mayor a cero (`cantidad <= 0` dispara excepción).
2. Obtiene el inventario de la bodega y calcula el total de unidades almacenadas actualmente sumando las existencias de todos sus ítems (`inventario.getTotalUnidadesAlmacenadas()`).
3. Comprueba si la ocupación actual más la nueva cantidad supera la capacidad máxima de la sede:
   ```java
   if (totalActual + cantidad > bodega.getCapacidad()) {
       int espacioDisponible = bodega.getCapacidad() - totalActual;
       throw new Exception("Capacidad excedida en bodega " + idBodega + 
                           ". Espacio restante: " + espacioDisponible + " unidades.");
   }
   ```
4. Si hay espacio suficiente, incrementa el stock con `inventario.registrarOActualizarStock(producto, cantidad)` y persiste los cambios inmediatamente en `data/inventarios.json`.

### 4.2. Algoritmo Atómico de Descuento de Stock en Dos Fases
Ubicado en `SistemaController.procesarDescuentoStock(OrdenCompra orden)`:
1. **Fase 1 (Verificación preventiva completa)**: Recorre cada uno de los ítems de la orden y verifica que en la bodega de despacho existan unidades suficientes (`stockDisponible >= cantidadSolicitada`). Si al menos un producto tiene existencias insuficientes, se arroja una excepción detallada y se detiene el proceso sin modificar ninguna variable en memoria.
2. **Fase 2 (Deducción efectiva)**: Habiendo garantizado que el 100% de la orden cuenta con disponibilidad física, se recorre nuevamente la lista ejecutando `inventario.descontarStock(codigo, cantidad)`.
3. **Fase 3 (Persistencia atómica)**: Se vuelcan los cambios a disco en `data/inventarios.json` y `data/ordenes.json`.

### 4.3. Validación de Números Negativos y Datos Inválidos
En todas las interfaces gráficas se implementa una captura defensiva antes de interactuar con el controlador:
- `ProductoPanel`: `Double.parseDouble(txtPrecio.getText().trim())` con `if (precio <= 0)`.
- `EmpleadoPanel`: `Double.parseDouble(txtSalario.getText().trim())` con `if (salario <= 0)`.
- `BodegaPanel`: `Integer.parseInt(txtCapacidad.getText().trim())` con `if (capacidad <= 0)`.
- `InventarioPanel`: `Integer.parseInt(txtCantidad.getText().trim())` con `if (cantidad <= 0)`.
- `OrdenCompraPanel`: `Integer.parseInt(txtCantidadItem.getText().trim())` con `if (cantidad <= 0)`.
- En todos los casos, cualquier entrada alfabética o carácter especial es neutralizado mediante `catch (NumberFormatException ex)`, mostrando un diálogo informativo al usuario sin caídas del sistema.

---

## 5. Instrucciones de Compilación y Ejecución en Distintos Entornos

### 5.1. Ejecución con Scripts Automatizados

#### En macOS y Linux:
```bash
chmod +x run.sh
./run.sh
```

#### En Windows (Símbolo del Sistema / CMD o PowerShell):
Hacer doble clic en `run.bat` o ejecutar desde la consola:
```bat
run.bat
```
*(El script `run.bat` se ubica automáticamente en la ruta correcta mediante `cd /d "%~dp0"`, verifica si Java JDK está en el PATH, ejecuta el binario precompilado `bin/sistema.jar` y, si este no existe, compila los archivos `.java` en codificación UTF-8 hacia la carpeta `bin/`).*

---

### 5.2. Ejecución en Entornos de Desarrollo Integrados (IDE)

#### En Apache NetBeans:
1. Abrir NetBeans y seleccionar **File** ➔ **Open Project...**
2. Navegar hasta la carpeta `sistema-gestion-ventas` (aparecerá con el icono de un proyecto Maven o una taza de café) y hacer clic en **Open Project**.
3. En el panel izquierdo de proyectos, hacer **clic derecho** sobre el nombre del proyecto y seleccionar **Clean and Build**. En la pestaña de salida (*Output*) aparecerá el mensaje `BUILD SUCCESS`.
4. Para ejecutar:
   - **Opción Rápida:** En el árbol del proyecto, desplegar `Source Packages` ➔ `com.sistema`, hacer **clic derecho sobre `Main.java`** y seleccionar **Run File** (o presionar `Shift + F6`).
   - **Opción Permanente con F6:** Clic derecho sobre el proyecto ➔ **Properties** ➔ seleccionar la categoría **Run** ➔ en **Main Class** presionar **Browse...**, seleccionar `com.sistema.Main` y hacer clic en **OK**. A partir de ese momento, presionar **F6** iniciará el sistema directamente.

#### En IntelliJ IDEA:
1. **File** ➔ **Open...** ➔ Seleccionar el archivo `pom.xml` o la carpeta raíz del proyecto y elegir **Open as Project**.
2. Esperar a que IntelliJ sincronice las dependencias Maven.
3. Abrir `src/main/java/com/sistema/Main.java` y hacer clic en el botón verde de reproducción (*Run*) junto a la declaración del método `main`.

#### En Visual Studio Code:
1. Abrir la carpeta del proyecto en VS Code.
2. Contar con la extensión *Extension Pack for Java*.
3. Abrir `Main.java` y hacer clic en la opción **Run** que aparece encima del método `main`.

---

### 5.3. Compilación y Ejecución Manual por Terminal (Línea de Comandos)

#### En macOS / Linux:
```bash
mkdir -p bin
javac -cp "lib/*" -encoding UTF-8 -d bin $(find src/main/java -name "*.java")
java -cp "bin:lib/*" com.sistema.Main
```

#### En Windows:
```cmd
if not exist "bin" mkdir bin
javac -cp "lib/*" -encoding UTF-8 -d bin src/main/java/com/sistema/model/*.java src/main/java/com/sistema/persistence/*.java src/main/java/com/sistema/controller/*.java src/main/java/com/sistema/view/*.java src/main/java/com/sistema/*.java
java -cp "bin;lib/*" com.sistema.Main
```
*(Nota importante: En Windows el separador de classpath es punto y coma `;`, mientras que en Linux/macOS es dos puntos `:`).*
