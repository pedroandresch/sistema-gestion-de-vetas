# Plan de Pruebas y Matriz de Casos de Prueba - Sistema de Gestión de Inventario y Ventas

## 1. Introducción y Objetivos de Calidad

Este documento define la estrategia de aseguramiento de calidad y validación funcional del **Sistema de Gestión de Inventario y Ventas**. El objetivo principal es verificar de manera metódica que la aplicación cumpla con todos los requerimientos funcionales, respete las reglas de negocio, maneje adecuadamente los datos atípicos o negativos y garantice la integridad de la información en memoria y en los archivos de persistencia JSON.

---

## 2. Tipos de Pruebas Ejecutadas

1. **Pruebas Funcionales**: Verificación de las operaciones CRUD (Creación, Lectura, Actualización) en catálogos de productos, clientes, empleados, bodegas, inventarios y órdenes.
2. **Pruebas de Validación de Entradas (Pruebas Negativas)**: Comprobación de que el sistema rechace entradas alfabéticas en campos numéricos, valores negativos o nulos, y formatos no válidos.
3. **Pruebas de Valores Límite (*Boundary Testing*)**: Evaluación del comportamiento del sistema en los umbrales de capacidad de bodegas y existencias disponibles.
4. **Pruebas de Transaccionalidad y Consistencia**: Verificación del algoritmo en dos fases para el descuento seguro de inventario al completar ventas.
5. **Pruebas de Persistencia e Integridad Multiplataforma**: Verificación de lectura y escritura en archivos JSON con codificación `UTF-8`.

---

## 3. Matriz Exhaustiva de Casos de Prueba

| ID | Módulo | Escenario de Prueba | Datos de Entrada | Resultado Esperado | Resultado Obtenido | Estado |
| :--- | :--- | :--- | :--- | :--- | :--- | :---: |
| **CP-01** | Productos | Registro con campos obligatorios vacíos | Código: `""`, Nombre: `""`, Precio: `""` | Diálogo de advertencia: *"Código, Nombre y Precio son obligatorios"*. No se registra. | Bloqueo exitoso y mensaje informativo desplegado. | **APROBADO** |
| **CP-02** | Productos | Validación de precio negativo | Código: `PROD-099`, Precio: `-25.50` | Alerta: *"El precio debe ser mayor a 0"*. La inserción se cancela. | Se detiene con return y muestra la advertencia. | **APROBADO** |
| **CP-03** | Productos | Validación de precio igual a cero | Código: `PROD-099`, Precio: `0.00` | Alerta: *"El precio debe ser mayor a 0"*. No se guarda. | Validación activa, precio rechazado. | **APROBADO** |
| **CP-04** | Productos | Entrada de texto en campo de precio | Código: `PROD-099`, Precio: `"cien"` | Captura de `NumberFormatException`. Mensaje: *"El precio debe ser un número válido"*. | Excepción capturada limpiamente sin colapso de la GUI. | **APROBADO** |
| **CP-05** | Productos | Validación de unicidad de código | Código existente: `PROD-001` | Excepción controlada: *"Ya existe un producto con el código: PROD-001"*. | Diálogo de error emitido por el controlador. | **APROBADO** |
| **CP-06** | Productos | Modificación de producto existente | Seleccionar fila `PROD-001`, cambiar precio a `5800.00` | Código deshabilitado para edición; producto actualizado en tabla y en `productos.json`. | Actualización exitosa y persistida en disco. | **APROBADO** |
| **CP-07** | Clientes | Validación de unicidad de DPI | DPI existente: `1001200230101` | Excepción del controlador: *"Ya existe un cliente con el DPI..."*. | Registro rechazado por duplicidad. | **APROBADO** |
| **CP-08** | Clientes | Asignación de TipoCliente mediante Enum | Selección en combo: `TipoCliente.VIP` | Se asigna la constante tipada; al guardar persiste como `"tipo": "VIP"`. | Enum serializado y deserializado correctamente. | **APROBADO** |
| **CP-09** | Empleados | Validación de salario negativo o cero | Salario: `-500.00` o `0.00` | Mensaje de validación: *"El salario debe ser mayor a 0"*. | Bloqueo exitoso antes de llamar al controlador. | **APROBADO** |
| **CP-10** | Empleados | Unicidad dual (ID y DPI) | ID: `EMP-01` o DPI ya existente | Excepción controlada indicando que el ID o DPI ya pertenecen a otro empleado. | Control de unicidad verificado en ambas variables. | **APROBADO** |
| **CP-11** | Bodegas | Validación de capacidad menor o igual a cero | Capacidad: `0` o `-100` | Mensaje: *"La capacidad debe ser mayor a 0"*. No se crea la bodega. | Validación con `Integer.parseInt` y condicional `capacidad <= 0`. | **APROBADO** |
| **CP-12** | Bodegas | Asignación de Empleado (Agregación) | Seleccionar `EMP-01` como encargado | La bodega guarda la referencia del empleado. Si se consulta, devuelve el nombre del empleado. | Objeto `Empleado` embebido correctamente en memoria y JSON. | **APROBADO** |
| **CP-13** | Inventario | Abastecimiento válido dentro de capacidad | Bodega capacidad 500, stock actual 0, ingresar 100 uds | Ingreso exitoso. Existencia = 100 uds. Capacidad libre restante = 400 uds. | Actualización de tabla y etiqueta de ocupación en tiempo real. | **APROBADO** |
| **CP-14** | Inventario | Abastecimiento que excede la capacidad | Bodega capacidad 200, stock actual 180, intentar ingresar 30 uds | Excepción: *"Capacidad excedida en bodega... Espacio restante: 20 unidades"*. No se guarda. | Control del controlador detiene la operación y muestra espacio libre. | **APROBADO** |
| **CP-15** | Inventario | Acumulación de existencias para producto existente | Ingresar 20 unidades de un producto que ya tenía 30 | El método `registrarOActualizarStock()` suma las unidades; stock final = 50. No se duplican filas. | Acumulación matemática exitosa. | **APROBADO** |
| **CP-16** | Ventas | Validación de cantidad negativa en carrito | Cantidad: `-3` o `0` | Alerta: *"Seleccione un producto y una cantidad mayor a 0"*. | No permite añadir al carrito temporal. | **APROBADO** |
| **CP-17** | Ventas | Cálculo automático de subtotal y total | Producto de Q 150.00, cantidad 4 | Subtotal de línea = `Q 600.00`. Total de la orden = `Q 600.00`. | Operación aritmética automática verificada. | **APROBADO** |
| **CP-18** | Ventas | Orden guardada como PENDIENTE | Crear `ORD-090` con estado `PENDIENTE` | La orden se almacena en `ordenes.json`. **El inventario físico NO se descuenta**. | Existencias de bodega permanecen intactas. | **APROBADO** |
| **CP-19** | Ventas | Completar orden con stock suficiente | Vender 5 unidades de un producto con stock de 50 en `BOD-01` | Orden pasa a `COMPLETADA`. Stock en `BOD-01` se reduce automáticamente a 45 unidades. | Descuento atómico verificado en memoria y en `inventarios.json`. | **APROBADO** |
| **CP-20** | Ventas | Intento de venta con stock insuficiente | Solicitar 10 unidades cuando solo hay 4 disponibles | Excepción: *"Stock insuficiente en bodega... Disponible: 4, Solicitado: 10"*. Transacción abortada. | La orden no se completa y no se descuenta ninguna unidad. | **APROBADO** |
| **CP-21** | Ventas | Completar orden previamente PENDIENTE | Seleccionar orden pendiente en historial y presionar "Completar Orden" | Valida disponibilidad, descuenta existencias y actualiza el estado a `COMPLETADA`. | Transición de estado exitosa. | **APROBADO** |
| **CP-22** | Ventas | Intento de cancelar orden COMPLETADA | Seleccionar orden `COMPLETADA` y presionar "Cancelar Orden" | Excepción: *"No se puede cancelar una orden que ya fue COMPLETADA"*. | Regla de inmutabilidad respetada; estado no alterado. | **APROBADO** |
| **CP-23** | UI / Filtros | Búsqueda por texto parcial e insensible a mayúsculas | Buscar `"lav"` para encontrar `"Lavadora LG"` | La tabla filtra y muestra únicamente los registros que contienen la cadena. | Búsqueda con `toLowerCase().contains()` operativa. | **APROBADO** |
| **CP-24** | Persistencia | Integridad de datos tras reinicio de la aplicación | Cerrar la aplicación y volver a iniciarla | Todos los datos registrados (productos, clientes, órdenes, stock) se recargan intactos desde `/data`. | Persistencia en JSON verificada al 100%. | **APROBADO** |

---

## 4. Conclusiones de las Pruebas

- **Robustez ante Errores de Usuario**: La aplicación captura de forma controlada el 100% de los intentos de ingresar texto en campos numéricos y valores negativos o cero, protegiendo al usuario con mensajes claros y evitando caídas del entorno visual.
- **Consistencia Transaccional**: El algoritmo de dos fases en el controlador demostró prevenir con total éxito cualquier descuento parcial o descuadre de existencias cuando no se cuenta con el inventario completo.
- **Cobertura de Requerimientos**: Todos los requisitos de la especificación académica (POO, MVC, persistencia en archivos JSON, tema FlatLaf, control de bodegas y facturación con actualización de inventario) fueron probados satisfactoriamente superando el 100% de la matriz de casos de prueba.
