# Manual Técnico de Arquitectura e Implementación

**Sistema**: Control de Inventario y Ventas  
**Arquitectura**: Modelo - Vista - Controlador (MVC)  
**Plataforma**: Java SE (Compatible con JDK 17, 21 LTS y superiores)  
**Persistencia**: JSON plano (Google Gson 2.10.1 con UTF-8)  
**Librería Gráfica**: Java Swing + FlatLaf 3.4 (Modern Look and Feel)  

---

## 1. Arquitectura del Sistema (Patrón MVC)

El sistema implementa el patrón **Modelo - Vista - Controlador (MVC)** complementado con una **Capa de Persistencia** dedicada:

```text
       ┌────────────────────────┐
       │     VISTA (Swing)      │
       │ (Paneles y Formularios)│
       └───────────┬────────────┘
                   │ Eventos de Usuario (ActionListener, etc.)
                   ▼
       ┌────────────────────────┐
       │      CONTROLADOR       │ ◄─────── Reglas de negocio y validación
       │  (SistemaController)   │
       └─────┬────────────┬─────┘
             │            │
  Actualiza  │            │ Persiste listas en JSON
             ▼            ▼
┌──────────────────┐ ┌───────────────────────┐
│  MODELO (Entidades)│ │    PERSISTENCIA       │
│ (Objetos POO)    │ │ (JsonRepository / Gson)│
└──────────────────┘ └───────────────────────┘
```

### 1.1. Responsabilidades Técnicas por Capa

#### 1. Capa de Modelo (`com.sistema.model`)
* Contiene las entidades puras del negocio: `Persona`, `Cliente`, `Empleado`, `Producto`, `Bodega`, `Inventario`, `ItemInventario`, `OrdenCompra` y `DetalleOrden`.
* **Aislamiento**: No posee dependencias de interfaz gráfica (`javax.swing.*`) ni de persistencia (`com.google.gson.*`).
* Encapsula su lógica interna mediante constructores sobrecargados, métodos accesores/mutadores y autocalculos (como el subtotal de detalle y total de orden).

#### 2. Capa de Vista (`com.sistema.view`)
* Construida mediante componentes estándar de `javax.swing` y `java.awt`, orquestados mediante un layout principal `CardLayout` dentro de `MainFrame.java`.
* **Event Dispatch Thread (EDT)**: Todas las vistas y diálogos modales se ejecutan dentro del hilo EDT mediante `SwingUtilities.invokeLater()` en `Main.java` para prevenir condiciones de carrera y congelamientos de interfaz.
* **Separación de responsabilidades**: Las vistas no realizan validaciones de unicidad ni calculan stock; capturan los datos de los formularios y delegan la ejecución al `SistemaController`.

#### 3. Capa de Controlador (`com.sistema.controller`)
* Clase central: `SistemaController.java`.
* Mantiene en memoria las colecciones principales en estructuras `List<T>` (`ArrayList`):
  * `List<Producto> productos`
  * `List<Cliente> clientes`
  * `List<Empleado> empleados`
  * `List<Bodega> bodegas`
  * `List<Inventario> inventarios`
  * `List<OrdenCompra> ordenes`
* Ejecuta las validaciones de unicidad (DPI, códigos de producto, bodegas), el control de capacidad física y la transacción de descuento de inventario.

#### 4. Capa de Persistencia (`com.sistema.persistence`)
* Clase utilitaria: `JsonRepository.java`.
* Utiliza una instancia estática de `Gson` configurada con `setPrettyPrinting()` para generar archivos legibles en disco bajo el directorio `data/`.
* Utiliza `StandardCharsets.UTF_8` en todos los flujos `FileReader` y `FileWriter` para garantizar compatibilidad multiplataforma entre Windows, macOS y Linux sin corrupción de tildes o caracteres especiales.

---

## 2. Sistema de Diseño y Tema Visual (`UITheme.java`)

La interfaz visual utiliza la librería **FlatLaf 3.4**, la cual reemplaza los estilos arcaicos de Swing por componentes vectoriales limpios inspirados en interfaces modernas:

```java
// Inicializacion en Main.java
FlatLightLaf.setup();
UIManager.put("Button.arc", 8);
UIManager.put("Component.arc", 8);
UIManager.put("TextComponent.arc", 8);
```

### 2.1. Tokens de Color y Tipografía en `UITheme.java`

| Token | Código Hex / RGB | Uso en el Sistema |
| :--- | :--- | :--- |
| `FONDO_MENU` | `#0F172A` / `rgb(15, 23, 42)` | Fondo del menú lateral de navegación (Slate 900). |
| `MENU_ACTIVO` | `#334155` / `rgb(51, 65, 85)` | Fondo del botón del módulo actualmente seleccionado. |
| `FONDO_PRINCIPAL` | `#F4F7FB` / `rgb(244, 247, 251)` | Fondo del área de trabajo y paneles de contenido. |
| `COLOR_PRIMARIO` | `#2563EB` / `rgb(37, 99, 235)` | Botones de acción principal (Guardar, Buscar). |
| `COLOR_EXITO` | `#10B981` / `rgb(16, 185, 129)` | Botón "Completar Venta" e indicadores de alta disponibilidad. |
| `COLOR_PELIGRO` | `#DC2626` / `rgb(220, 38, 38)` | Botones de eliminación y alertas de sobrecupo. |
| `COLOR_ADVERTENCIA`| `#D97706` / `rgb(217, 119, 6)` | Estados `PENDIENTE` e indicadores de stock bajo. |

* **Propiedades de Cliente FlatLaf**: Los paneles de tarjetas utilizan `componente.putClientProperty(FlatClientProperties.STYLE, "arc: 12")` para renderizar esquinas redondeadas suaves con borde compuesto.

---

## 3. Diccionario de Datos y Esquemas JSON (`data/`)

Los datos se guardan en 6 archivos JSON planos en el directorio `data/`:

### 3.1. `data/productos.json`
```json
[
  {
    "codigo": "PROD-001",
    "nombre": "Laptop HP Pavilion 15",
    "descripcion": "Intel Core i5, 16GB RAM, 512GB SSD",
    "marca": "HP",
    "precio": 5200.0
  }
]
```
* `codigo` (`String`, Obligatorio, Único): Clave primaria alfanumérica.
* `nombre` (`String`, Obligatorio): Nombre descriptivo del producto.
* `descripcion` (`String`, Opcional): Características técnicas.
* `marca` (`String`, Obligatorio): Marca del fabricante.
* `precio` (`double`, Obligatorio): Valor monetario positivo en Quetzales ($> 0.0$).

### 3.2. `data/clientes.json`
```json
[
  {
    "fechaNacimiento": "1994-06-22",
    "tipo": "VIP",
    "dpi": "2541896320101",
    "nombre": "Carlos Estuardo Gómez",
    "direccion": "Zona 10, Ciudad de Guatemala"
  }
]
```
* `dpi` (`String`, Obligatorio, Único): Documento Personal de Identificación (13 dígitos).
* `nombre` (`String`, Obligatorio): Nombre y apellidos completos.
* `direccion` (`String`, Obligatorio): Domicilio fiscal o residencial.
* `fechaNacimiento` (`String`, Obligatorio): Formato `AAAA-MM-DD`.
* `tipo` (`String`, Obligatorio): Valor enum exacto: `"REGULAR"` o `"VIP"`.

### 3.3. `data/empleados.json`
```json
[
  {
    "idEmpleado": "EMP-01",
    "puesto": "Encargado de Bodega Central",
    "salario": 4500.0,
    "dpi": "1985472130101",
    "nombre": "Manuel Antonio Rivera",
    "direccion": "Zona 7, Mixco"
  }
]
```
* `idEmpleado` (`String`, Obligatorio, Único): Código laboral interno.
* `dpi` (`String`, Obligatorio, Único): Identificación civil no duplicable.
* `puesto` (`String`, Obligatorio): Cargo dentro de la organización.
* `salario` (`double`, Obligatorio): Salario mensual en Quetzales ($> 0.0$).

### 3.4. `data/bodegas.json`
```json
[
  {
    "idBodega": "BOD-01",
    "nombre": "Bodega Central Mixco",
    "direccion": "Km 15 Calzada Roosevelt",
    "capacidad": 500,
    "encargado": {
      "idEmpleado": "EMP-01",
      "puesto": "Encargado de Bodega Central",
      "salario": 4500.0,
      "dpi": "1985472130101",
      "nombre": "Manuel Antonio Rivera",
      "direccion": "Zona 7, Mixco"
    }
  }
]
```
* `idBodega` (`String`, Obligatorio, Único): Clave primaria de la sede.
* `capacidad` (`int`, Obligatorio): Capacidad total en unidades enteras ($> 0$).
* `encargado` (`Object`, Obligatorio): Objeto serializado del empleado responsable (Agregación).

### 3.5. `data/inventarios.json`
```json
[
  {
    "idBodega": "BOD-01",
    "items": [
      {
        "producto": {
          "codigo": "PROD-001",
          "nombre": "Laptop HP Pavilion 15",
          "descripcion": "Intel Core i5, 16GB RAM",
          "marca": "HP",
          "precio": 5200.0
        },
        "stock": 45
      }
    ]
  }
]
```
* `idBodega` (`String`, Obligatorio): Llave de relación foránea con `bodegas.json`.
* `items` (`Array<ItemInventario>`): Lista de productos almacenados y su existencia actual ($\text{stock} \ge 0$).

### 3.6. `data/ordenes.json`
```json
[
  {
    "idOrden": "ORD-01",
    "cliente": {
      "fechaNacimiento": "1994-06-22",
      "tipo": "VIP",
      "dpi": "2541896320101",
      "nombre": "Carlos Estuardo Gómez",
      "direccion": "Zona 10"
    },
    "idBodega": "BOD-01",
    "items": [
      {
        "producto": {
          "codigo": "PROD-001",
          "nombre": "Laptop HP Pavilion 15",
          "marca": "HP",
          "precio": 5200.0
        },
        "cantidad": 2,
        "precioUnitario": 5200.0,
        "subtotal": 10400.0
      }
    ],
    "fecha": "2026-11-06 21:30:00",
    "total": 10400.0,
    "estado": "COMPLETADA"
  }
]
```

---

## 4. Algoritmos Críticos del Negocio

### 4.1. Algoritmo de Control de Capacidad Máxima de Almacén
Ubicado en `SistemaController.registrarStock(String idBodega, Producto producto, int cantidad)`:

1. **Precondición**: Valida que $\text{cantidad} > 0$.
2. **Cálculo de Ocupación Acumulada**:
   $$\text{Ocupación Actual} = \sum_{i=1}^{n} \text{item}_i.\text{stock}$$
3. **Verificación de Límite**:
   $$\text{Ocupación Proyectada} = \text{Ocupación Actual} + \text{cantidad}$$
   Si $\text{Ocupación Proyectada} > \text{bodega}.\text{capacidad}$:
   * Se interrumpe la ejecución mediante el lanzamiento de una excepción descriptiva:
     ```java
     int espacioDisponible = bodega.getCapacidad() - totalActual;
     throw new Exception("Capacidad excedida en bodega " + idBodega + 
                         ". Espacio restante: " + espacioDisponible + " unidades.");
     ```
4. **Mutación y Persistencia**: Si hay espacio, se invoca `inventario.registrarOActualizarStock(producto, cantidad)` y se guardan los datos en `data/inventarios.json`.

---

### 4.2. Algoritmo Transaccional en Dos Fases para Descuento de Stock
Ubicado en `SistemaController.completarOrden(String idOrden)`:

Para garantizar la consistencia de datos (evitar que una orden descuente parcialmente existencias si falta un producto), se implementa un mecanismo análogo al *Two-Phase Commit (2PC)*:

```java
// ========================================================
// FASE 1: VERIFICACION PREVENTIVA COMPLETA (Read-Only)
// ========================================================
Inventario inventario = obtenerOCrearInventario(orden.getIdBodega());

for (DetalleOrden detalle : orden.getItems()) {
    String codigoProd = detalle.getProducto().getCodigo();
    int stockDisponible = inventario.obtenerStock(codigoProd);
    
    // Si tan solo un producto no alcanza, aborta la transaccion
    if (stockDisponible < detalle.getCantidad()) {
        throw new Exception("Stock insuficiente en bodega " + orden.getIdBodega() + 
                            " para el producto: " + detalle.getProducto().getNombre() + 
                            ". Disponible: " + stockDisponible + 
                            ", Solicitado: " + detalle.getCantidad());
    }
}

// ========================================================
// FASE 2: DEDUCCION EFECTIVA Y PERSISTENCIA (Commit)
// ========================================================
// Habiendo garantizado que el 100% de los items estan disponibles:
for (DetalleOrden detalle : orden.getItems()) {
    inventario.descontarStock(detalle.getProducto().getCodigo(), detalle.getCantidad());
}

orden.setEstado(EstadoOrden.COMPLETADA);

// Persistencia atomica en disco
JsonRepository.guardar(RUTA_INVENTARIOS, this.inventarios);
JsonRepository.guardar(RUTA_ORDENES, this.ordenes);
```

* **Garantía Técnica**: Si la Fase 1 falla, ninguna variable en memoria cambia y ningún archivo en disco es sobrescrito. La orden permanece en su estado anterior.

---

## 5. Análisis de Complejidad Computacional (Big-O)

| Operación | Método en `SistemaController` | Estructura de Datos | Complejidad Temporal | Justificación |
| :--- | :--- | :--- | :---: | :--- |
| Buscar Producto | `buscarProducto(codigo)` | `List<Producto>` | $O(N)$ | Búsqueda lineal secuencial por código. |
| Buscar Cliente | `buscarCliente(dpi)` | `List<Cliente>` | $O(C)$ | Recorrido lineal comparando DPIs. |
| Verificar Capacidad | `registrarStock(...)` | `List<ItemInventario>` | $O(M)$ | Sumatoria de existencias de los $M$ items de la bodega. |
| Descuento en 2 Fases | `completarOrden(...)` | $K$ items en orden, $M$ en bodega | $O(K \times M)$ | Por cada item del carrito ($K$), busca el stock en la bodega ($M$). Para tamaños habituales ($K < 50, M < 1000$), la ejecución toma $< 1$ ms. |
| Persistencia a Disco | `JsonRepository.guardar(...)` | Archivo I/O | $O(N)$ | Serialización lineal del árbol de objetos a texto JSON. |

---

## 6. Procedimientos de Compilación y Ejecución

### 6.1. Ejecución con Scripts

* **Windows**:
  ```cmd
  run.bat
  ```
  *(El script detecta la ruta local `%~dp0`, verifica la presencia de `javac` / `java`, compila con codificación UTF-8 hacia `bin/` y ejecuta `com.sistema.Main` con el classpath adecuado).*

* **macOS / Linux**:
  ```bash
  chmod +x run.sh
  ./run.sh
  ```

### 6.2. Compilación Manual por Terminal

**Windows (CMD / PowerShell)**:
```cmd
if not exist "bin" mkdir bin
javac -cp "lib/*" -encoding UTF-8 -d bin src/main/java/com/sistema/model/*.java src/main/java/com/sistema/persistence/*.java src/main/java/com/sistema/controller/*.java src/main/java/com/sistema/view/*.java src/main/java/com/sistema/*.java
java -cp "bin;lib/*" com.sistema.Main
```

**macOS / Linux (zsh / bash)**:
```bash
mkdir -p bin
javac -cp "lib/*" -encoding UTF-8 -d bin $(find src/main/java -name "*.java")
java -cp "bin:lib/*" com.sistema.Main
```

### 6.3. Construcción y Empaquetado Maven
```bash
mvn clean package
java -jar target/sistema-gestion-ventas-1.0.0-jar-with-dependencies.jar
```
*(El archivo `pom.xml` utiliza el plugin `maven-assembly-plugin` para generar un `.jar` independiente con FlatLaf y Gson empaquetados dentro).*
