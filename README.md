# Sistema de Gestión de Inventario y Ventas

## 1. Resumen del Proyecto

El presente proyecto consiste en una aplicación de escritorio desarrollada en el lenguaje de programación Java como proyecto final del curso de Programación Orientada a Objetos. Su objetivo es gestionar las operaciones comerciales y de almacenamiento de una empresa dedicada a la venta de productos, abarcando el control de catálogo, registro de clientes, administración de personal, gestión de bodegas físicas, control de existencias y emisión de órdenes de compra.

La solución aplica el patrón arquitectónico Modelo-Vista-Controlador (MVC), implementa una interfaz gráfica de usuario construida con la biblioteca estándar Java Swing complementada con el tema visual FlatLaf, y utiliza archivos en formato JSON mediante la biblioteca Google Gson para garantizar la persistencia local de la información entre sesiones de ejecución.

---

## 2. Alcance del Sistema

El sistema comprende los siguientes módulos funcionales:

- **Panel General**: Visualización de indicadores operativos sobre el total de productos registrados, clientes activos, bodegas configuradas y monto acumulado de ventas completadas.
- **Gestión de Productos**: Registro y consulta del catálogo de artículos con validación de código único y precio unitario expresado en quetzales (Q).
- **Gestión de Clientes**: Administración del directorio de clientes a partir de sus datos personales (heredados de la clase abstracta `Persona`), fecha de nacimiento y clasificación comercial (`REGULAR` o `VIP`).
- **Gestión de Empleados**: Registro de la nómina de colaboradores con código de empleado, número de Documento Personal de Identificación (DPI), dirección, puesto y salario.
- **Gestión de Bodegas**: Configuración de sedes físicas de almacenamiento, asignación de un empleado encargado y definición de la capacidad máxima en unidades.
- **Control de Inventario**: Abastecimiento de productos por bodega con verificación automática de la capacidad máxima disponible e indicadores porcentuales de ocupación.
- **Órdenes de Compra y Facturación**: Creación de órdenes asociadas a un cliente y a una bodega de despacho, permitiendo agregar uno o varios productos con cálculo automático de subtotales y total general. Incluye el manejo de estados (`PENDIENTE`, `COMPLETADA`, `CANCELADA`) y la actualización automática del inventario al completar una venta.

---

## 3. Estructura del Directorio

```text
sistema-gestion-ventas/
├── run.sh                  # Script de compilación y ejecución en sistemas Unix/macOS
├── pom.xml                 # Configuración de construcción y dependencias para Apache Maven
├── lib/                    # Bibliotecas externas requeridas en tiempo de compilación y ejecución
│   ├── gson-2.10.1.jar     # Biblioteca Google Gson para serialización JSON
│   └── flatlaf-3.4.jar     # Biblioteca FlatLaf para el estilo visual de Java Swing
├── bin/                    # Directorio de salida para archivos de bytecode compilados (.class)
├── data/                   # Almacenamiento persistente en archivos JSON
│   ├── productos.json
│   ├── clientes.json
│   ├── empleados.json
│   ├── bodegas.json
│   ├── inventarios.json
│   └── ordenes.json
├── docs/                   # Documentación académica y técnica del proyecto
│   ├── DIAGRAMA_UML.md     # Diagrama de clases UML y justificación de principios POO
│   ├── MANUAL_TECNICO.md   # Arquitectura MVC, diccionario de datos JSON y reglas de negocio
│   ├── MANUAL_USUARIO.md   # Manual operativo paso a paso y solución de problemas
│   └── PLAN_DE_PRUEBAS.md  # Matriz de 24 casos de prueba funcionales y de límites
├── run.bat                 # Script de compilación y ejecución directa en Windows
├── run.sh                  # Script de compilación y ejecución en sistemas Unix/macOS
├── pom.xml                 # Configuración de construcción para Maven y NetBeans
├── lib/                    # Bibliotecas externas requeridas (.jar)
│   ├── gson-2.10.1.jar     # Google Gson para persistencia JSON
│   └── flatlaf-3.4.jar     # FlatLaf para tema visual moderno en Swing
├── bin/                    # Bytecode compilado (.class) y sistema.jar autocontenido
    ├── Main.java           # Punto de entrada de la aplicación
    ├── model/              # Clases del dominio y enumeraciones
    │   ├── Persona.java
    │   ├── Cliente.java
    │   ├── TipoCliente.java
    │   ├── Empleado.java
    │   ├── Producto.java
    │   ├── Bodega.java
    │   ├── ItemInventario.java
    │   ├── Inventario.java
    │   ├── DetalleOrden.java
    │   ├── OrdenCompra.java
    │   └── EstadoOrden.java
    ├── persistence/        # Capa de acceso y serialización de datos
    │   └── JsonRepository.java
    ├── controller/         # Controlador central y validaciones de negocio
    │   └── SistemaController.java
    └── view/               # Componentes y paneles de la interfaz gráfica Swing
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

## 4. Requisitos e Instrucciones de Ejecución

### Requisitos Previos
- **Java Development Kit (JDK)**: Versión 17 o superior instalada y configurada en las variables de entorno del sistema.
- **Bibliotecas externas**: Incluidas de forma local en el directorio `lib/` (`gson-2.10.1.jar` y `flatlaf-3.4.jar`), por lo que no se requiere descarga adicional si se utiliza el script de arranque.

### Opción 1: Ejecución mediante el script automatizado
Desde una terminal ubicada en la raíz del proyecto, otorgar permisos de ejecución (únicamente la primera vez) e iniciar el script:

```bash
chmod +x run.sh
./run.sh
```

### Opción 2: Compilación y ejecución manual con herramientas del JDK
Para compilar los archivos fuente hacia el directorio `bin/` y ejecutar la clase principal indicando el `classpath`:

```bash
mkdir -p bin
javac -cp "lib/*" -d bin $(find src/main/java -name "*.java")
java -cp "bin:lib/*" com.sistema.Main
```

*(Nota: En entornos Windows, el separador del argumento `-cp` debe sustituirse por punto y coma `;`, utilizando `"bin;lib/*"`).*

### Opción 3: Construcción y ejecución con Apache Maven
Si el entorno cuenta con Apache Maven instalado, es posible compilar y empaquetar el proyecto mediante el archivo `pom.xml`:

```bash
mvn clean package
java -jar target/sistema-gestion-ventas-1.0.0-jar-with-dependencies.jar
```

---

## 5. Flujo de Trabajo Colaborativo en Git

El desarrollo del proyecto está estructurado para un equipo de tres integrantes utilizando Git y GitHub bajo un esquema de ramas por módulo funcional (`feature branches`), evitando conflictos de edición directa sobre la rama principal `main`.

### Distribución de Responsabilidades Sugerida
1. **Integrante 1 (Dominio y Persistencia)**:
   - Implementación de la jerarquía de clases en `com.sistema.model` (`Persona`, `Cliente`, `Empleado`, `Producto`, `Bodega`, `Inventario`, `ItemInventario`, `OrdenCompra`, `DetalleOrden` y enumeraciones).
   - Implementación de la lectura y escritura genérica en `com.sistema.persistence.JsonRepository`.
   - Rama asignada: `feature/modelo-persistencia`.

2. **Integrante 2 (Controlador y Reglas de Negocio)**:
   - Desarrollo de `com.sistema.controller.SistemaController`.
   - Implementación de validaciones de unicidad (DPI, códigos de producto, bodegas y órdenes), control de capacidad máxima de bodegas y lógica de descuento de existencias al completar órdenes de compra.
   - Elaboración de lotes de datos iniciales en `data/*.json`.
   - Rama asignada: `feature/controlador-logica`.

3. **Integrante 3 (Interfaz Gráfica y Documentación)**:
   - Construcción de la ventana principal y los paneles en `com.sistema.view` (`MainFrame`, `DashboardPanel`, `ProductoPanel`, `ClientePanel`, `EmpleadoPanel`, `BodegaPanel`, `InventarioPanel`, `OrdenCompraPanel`).
   - Integración de eventos de usuario con el controlador y redacción de manuales en `docs/`.
   - Rama asignada: `feature/interfaz-swing`.

### Procedimiento de Trabajo con Ramas

1. **Clonación del repositorio e inicialización local**:
   ```bash
   git clone <url-del-repositorio>
   cd sistema-gestion-ventas
   ```

2. **Creación y cambio a la rama de trabajo correspondiente**:
   ```bash
   git checkout -b feature/nombre-del-modulo
   ```

3. **Registro de avances locales (commits atómicos y descriptivos)**:
   ```bash
   git status
   git add src/main/java/com/sistema/...
   git commit -m "Implementa validacion de stock en SistemaController"
   ```

4. **Sincronización con la rama principal antes de publicar**:
   ```bash
   git fetch origin
   git rebase origin/main
   ```

5. **Publicación de la rama e integración mediante Pull Request**:
   ```bash
   git push -u origin feature/nombre-del-modulo
   ```
   Una vez publicada la rama en GitHub, se genera un *Pull Request* hacia `main` para que los demás integrantes revisen que el código compile sin errores antes de fusionar los cambios.
