# Sistema de Gestión de Inventario y Ventas

Proyecto de escritorio desarrollado en **Java 21** para el curso de **Programación Orientada a Objetos**. Implementa una arquitectura **Modelo-Vista-Controlador (MVC)**, interfaz gráfica con **Java Swing** estilizada con **FlatLaf**, y almacenamiento persistente en archivos **JSON** utilizando **Google Gson**.

### Integrantes del Equipo
* **Pedro Andrés Chajón** 
* **Sharon Guacamaya** (`sharonley112`)
* **Dannii Pirir** (`dannii-12`)

---

## 1. Módulos del Sistema

El sistema centraliza las operaciones comerciales y logísticas a través de 7 módulos:

* **Dashboard (Panel General)**: Resumen operativo en tiempo real con tarjetas de métricas (total de ingresos, productos, clientes y bodegas activas), tabla de ocupación de bodegas y ventas recientes.
* **Catálogo de Productos**: Mantenimiento de artículos con validación de código único, marca, descripción y precio unitario en Quetzales (`Q`).
* **Directorio de Clientes**: Registro de clientes con DPI único (13 dígitos), datos personales y clasificación comercial (`REGULAR` o `VIP`).
* **Nómina de Empleados**: Registro de trabajadores con código laboral, puesto y salario mensual. Sirve de base para asignar los encargados de cada bodega.
* **Gestión de Bodegas**: Configuración de almacenes físicos con asignación de un empleado responsable y límite de capacidad máxima en unidades.
* **Control de Inventario**: Abastecimiento de existencias por bodega con control automático de sobrecupo y visualización de unidades libres y ocupadas.
* **Facturación y Órdenes de Compra**: Carrito de compras con cálculo automático de subtotales y total general. Permite guardar cotizaciones (`PENDIENTE`) y emitir ventas (`COMPLETADA`) con descuento automático y atómico de existencias en el inventario.

---

## 2. Estructura del Proyecto

```text
sistema-gestion-ventas/
├── pom.xml                 # Configuración de construcción y dependencias Maven
├── run.bat                 # Script de compilación y ejecución directa en Windows
├── run.sh                  # Script de ejecución para macOS y Linux
├── lib/                    # Librerías externas (.jar)
│   ├── gson-2.10.1.jar     # Google Gson para persistencia JSON
│   └── flatlaf-3.4.jar     # Tema visual moderno FlatLaf para Java Swing
├── data/                   # Persistencia local en archivos JSON
│   ├── productos.json
│   ├── clientes.json
│   ├── empleados.json
│   ├── bodegas.json
│   ├── inventarios.json
│   └── ordenes.json
├── docs/                   # Documentación oficial del proyecto
│   ├── DIAGRAMA_UML.md     # Diagrama de clases y justificación de principios POO
│   ├── MANUAL_TECNICO.md   # Arquitectura MVC, algoritmos y reglas de negocio
│   ├── MANUAL_USUARIO.md   # Guía paso a paso y solución de problemas
│   └── PLAN_DE_PRUEBAS.md  # Matriz de casos de prueba
└── src/main/java/com/sistema/
    ├── Main.java           # Clase principal y arranque de la interfaz gráfica
    ├── model/              # Clases de dominio (Entidades POO y enumeraciones)
    │   ├── Persona.java    # Clase abstracta base
    │   ├── Cliente.java & TipoCliente.java
    │   ├── Empleado.java
    │   ├── Producto.java
    │   ├── Bodega.java
    │   ├── ItemInventario.java & Inventario.java
    │   └── DetalleOrden.java, OrdenCompra.java & EstadoOrden.java
    ├── persistence/        # Acceso a datos
    │   └── JsonRepository.java  # Serializador genérico con Gson
    ├── controller/         # Lógica central del sistema
    │   └── SistemaController.java  # Validaciones, colecciones y descuento de stock
    └── view/               # Interfaz gráfica en Java Swing
        ├── MainFrame.java
        ├── UITheme.java
        ├── DashboardPanel.java
        ├── ProductoPanel.java
        ├── ClientePanel.java
        ├── EmpleadoPanel.java
        ├── BodegaPanel.java
        ├── InventarioPanel.java
        └── OrdenCompraPanel.java
```

---

## 3. Instrucciones de Ejecución

### Opción 1: Scripts Automáticos (La forma más rápida)
* **En Windows**: Haz doble clic sobre `run.bat` o ejecútalo desde CMD/PowerShell:
  ```cmd
  run.bat
  ```
* **En macOS o Linux**:
  ```bash
  chmod +x run.sh
  ./run.sh
  ```

### Opción 2: Desde tu IDE de preferencia
* **Apache NetBeans**: Abrir el proyecto (`File > Open Project`), hacer clic derecho en el proyecto > **Clean and Build**, y luego presionar **F6** o ejecutar `Main.java` (`Shift + F6`).
* **Visual Studio Code**: Abrir la carpeta del proyecto, contar con el *Extension Pack for Java*, abrir `Main.java` y hacer clic en **Run**.
* **IntelliJ IDEA**: Abrir la carpeta o el archivo `pom.xml`, esperar a que sincronice Maven y ejecutar `Main.java`.

### Opción 3: Compilación con Apache Maven
```bash
mvn clean package
java -jar target/sistema-gestion-ventas-1.0.0-jar-with-dependencies.jar
```

---

## 4. Distribución del Trabajo y Flujo en Git

El desarrollo del proyecto se organizó mediante ramas por funcionalidad (*feature branches*) en GitHub, integrando los módulos a la rama principal `main` a través de Pull Requests revisados y aprobados:

### Módulos y Responsabilidades:

1. **Pedro Andrés Chajón**:
   * **Módulo**: Arquitectura Base, Núcleo del Sistema y Ventas.
   * **Componentes desarrollados**: Controlador central (`SistemaController.java`), Persistencia genérica JSON (`JsonRepository.java`), Entidades de Ventas (`OrdenCompra`, `DetalleOrden`, `EstadoOrden`), Panel General (`DashboardPanel.java`), Facturación (`OrdenCompraPanel.java`) y ventana principal (`MainFrame.java`).
   * **Integración**: Configuración del repositorio y fusiones en `main`.

2. **Dannii Pirir (`dannii-12`)**:
   * **Módulo**: Gestión Comercial y Recursos Humanos.
   * **Componentes desarrollados**: Jerarquía de clases (`Persona.java` abstracta, `Cliente.java`, `TipoCliente.java` y `Empleado.java`) y sus respectivas interfaces gráficas (`ClientePanel.java` y `EmpleadoPanel.java`).
   * **Rama de trabajo**: `feature/modulo-clientes-empleados` ➔ **Pull Request #3** (Aprobado y fusionado).

3. **Sharon Guacamaya (`sharonley112`)**:
   * **Módulo**: Almacén, Catálogo e Inventario.
   * **Componentes desarrollados**: Catálogo de productos (`Producto.java`), Sedes físicas (`Bodega.java`), existencias (`Inventario.java`, `ItemInventario.java`) y sus respectivas interfaces gráficas (`ProductoPanel.java`, `BodegaPanel.java` y `InventarioPanel.java`).
   * **Rama de trabajo**: `feature/modulo-inventario-bodegas` ➔ **Pull Request #2** (Aprobado y fusionado).

---

## 5. Documentación Adicional

En la carpeta `docs/` se encuentran los documentos detallados del proyecto:
* **[Diagrama de Clases UML y Fundamentos POO](docs/DIAGRAMA_UML.md)**: Diagrama completo en notación Mermaid y justificación de abstracción, herencia, encapsulamiento, polimorfismo, composición y agregación.
* **[Manual Técnico](docs/MANUAL_TECNICO.md)**: Explicación de la arquitectura MVC, el algoritmo de dos fases para descuento de stock y diccionario de datos.
* **[Manual de Usuario](docs/MANUAL_USUARIO.md)**: Guía ilustrada paso a paso, orden de captura recomendado y solución de problemas comunes.
* **[Plan de Pruebas](docs/PLAN_DE_PRUEBAS.md)**: Matriz de pruebas funcionales y de casos límite.
